package com.lachesis.mnis.core.whiteBoard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.NurseWhiteBoardService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardEditType;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataDic;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordFreqInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordFreqPatInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataTV;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;
import com.lachesis.mnis.core.whiteBoard.repository.NurseWhiteBoardRepository;
@Transactional
@Service("nurseWhiteBoardService")
public class NurseWhiteBoardServiceImpl implements NurseWhiteBoardService {

	private static Logger log = Logger.getLogger(NurseWhiteBoardServiceImpl.class);
	
	@Autowired
	private NurseWhiteBoardRepository nurseWhiteBoardRepository;
	
	/*@Autowired
	private BodySignService bodySignService;*/
	
	/*@Autowired
	private PatientRepository patientRepository;*/

	@Autowired
	private PatientService patientService;

	/************************* 元数据 ***********************************************/

	@Override
	public List<NurseWhiteBoardMetadataDic> getNwbMetadataDics(
			String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			throw new MnisException("部门参数不为空!");
		}
		
		return nurseWhiteBoardRepository.getNwbMetadataDics(deptCode);
	}

	@Override
	public void batchSaveNwbMetadata(
			List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas,
			NurseWhiteBoardTemplate template, boolean isItems) {
		if (null == template
				&& (null == nurseWhiteBoardMetadatas || nurseWhiteBoardMetadatas
						.size() == 0)) {
			throw new MnisException("数据为空!");
		}
		String deptCode = nurseWhiteBoardMetadatas.get(0).getDeptCode();
		String templateId = MnisConstants.EMPTY;
		// 保存标记
		boolean isSave = false;
		// 保存公共信息
		if (null != template) {

			if (StringUtils.isBlank(template.getId())) {
				isSave = true;
				templateId = nurseWhiteBoardRepository.saveNwbTemplate(template);
			} else {
				// 根据id判断模板是否存在
				if (nurseWhiteBoardRepository.getNwbTemplates(
						template.getDeptCode(), template.getId()).size() > 0) {
					nurseWhiteBoardRepository.updateNwbTemplate(template);
					templateId = template.getId();
				} else {
					isSave = true;
					templateId = nurseWhiteBoardRepository.saveNwbTemplate(template);
				}
				//缓存模版数据
				getNwbTemplates(deptCode, templateId, true);
			}
		}

		
		// 待存在的code
		List<String> existCodes = new ArrayList<String>();
		// 待删除记录
		List<String> codes = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		if (isItems) {
			// 判断子项目已删除的元数据
			map = getMetadataName(deptCode, nurseWhiteBoardMetadatas.get(0)
					.getParentId());
		} else {
			// 判断已删除的元数据
			map = getMetadataName(deptCode, null);
		}

		for (NurseWhiteBoardMetadata nurseWhiteBoardMetadata : nurseWhiteBoardMetadatas) {

			existCodes.add(nurseWhiteBoardMetadata.getCode());
		}
		for (String key : map.keySet()) {
			// 已存在的key不包含在待添加的项目中，删除
			if (!existCodes.contains(key)) {
				codes.add(key);
			}
		}

		// 1.删除code
		for (String code : codes) {
			nurseWhiteBoardRepository.deleteNwbMetadata(null, code, deptCode);
		}

		String code = MnisConstants.EMPTY;
		for (NurseWhiteBoardMetadata nurseWhiteBoardMetadata : nurseWhiteBoardMetadatas) {
			code = nurseWhiteBoardMetadata.getCode();
			if (null == nurseWhiteBoardMetadata || StringUtils.isBlank(code)
					|| StringUtils.isBlank(deptCode)) {
				throw new MnisException("数据中code=[" + code + "],deptCode["
						+ deptCode + "]存在空!");
			}

			if (nurseWhiteBoardRepository.getNwbMetadataCount(deptCode, code,
					null) > 0) {
				log.debug("NurseWhiteBoardServiceImpl -> saveNwbMetadata : 保存 编号["
						+ nurseWhiteBoardMetadata.getCode() + "]已存在!");
				continue;
			}
			// 2.新增
			setInputTypeToNwbMetadata(nurseWhiteBoardMetadata);
			nurseWhiteBoardMetadata.setTemplateId(templateId);
			if (isSave) {
				// 设置默认值
				nurseWhiteBoardMetadata.setBedCode(true);
				nurseWhiteBoardMetadata.setEdit(true);
				nurseWhiteBoardMetadata.setShowData(true);
				nurseWhiteBoardMetadata.setShowTitle(true);
				nurseWhiteBoardMetadata.setShowLineR(true);
				nurseWhiteBoardMetadata.setShowLineB(true);
				nurseWhiteBoardMetadata.setShowLineT(true);
			}

			nurseWhiteBoardRepository.saveNwbMetadata(nurseWhiteBoardMetadata);
			List<NurseWhiteBoardEditType> nurseWhiteBoardEditTypes = nurseWhiteBoardMetadata
					.getNurseWhiteBoardEditTypes();
			if (!isItems && null != nurseWhiteBoardEditTypes) {
				// 3.处理编辑类型
				saveNwbMetadataEditType(nurseWhiteBoardEditTypes,
						nurseWhiteBoardMetadata.getCode());
			}
		}
	}
	
	@Override
	public int saveNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata,boolean isAddRow) {
		if(null == nurseWhiteBoardMetadata){
			log.debug("NurseWhiteBoardServiceImpl -> saveNwbMetadata : nurseWhiteBoardMetadata 实体不存在!" );
			throw new MnisException("保存的实体为空!");
		}
		String deptCode = nurseWhiteBoardMetadata.getDeptCode();
		
		//判断元数据code是否存在
		if(nurseWhiteBoardRepository.getNwbMetadataCount(deptCode, nurseWhiteBoardMetadata.getCode(),null) > 0){
			log.debug("NurseWhiteBoardServiceImpl -> saveNwbMetadata : 保存 编号[" + nurseWhiteBoardMetadata.getCode() +"]已存在!" );
			throw new MnisException("保存的实体编号[" + nurseWhiteBoardMetadata.getCode() +"]已存在!");
		}
		
		//判断元数据对应的rowNo 行是否存在,isAddRow(true：添加新行,false:加列)
		if(isAddRow && nurseWhiteBoardRepository.getNwbMetadataCount(deptCode,null,
				String.valueOf(nurseWhiteBoardMetadata.getRowNo())) > 0){
			nurseWhiteBoardRepository.updateNwbMetadataByRowNo(deptCode, nurseWhiteBoardMetadata.getRowNo(),0);
		}
		setInputTypeToNwbMetadata(nurseWhiteBoardMetadata);
		//保存元数据信息
		int saveCount = nurseWhiteBoardRepository.saveNwbMetadata(nurseWhiteBoardMetadata);
		
		List<NurseWhiteBoardEditType> nurseWhiteBoardEditTypes = nurseWhiteBoardMetadata.getNurseWhiteBoardEditTypes();
		if(null != nurseWhiteBoardEditTypes ){
			saveNwbMetadataEditType(nurseWhiteBoardEditTypes,nurseWhiteBoardMetadata.getCode());
		}
		
		return saveCount;
	}

	@Override
	public int updateNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata) {
		if(null == nurseWhiteBoardMetadata){
			log.debug("NurseWhiteBoardServiceImpl -> updateNwbMetadata : nurseWhiteBoardMetadata 实体不存在!" );
			return 0;
		}
		setInputTypeToNwbMetadata(nurseWhiteBoardMetadata);
		int saveCount = nurseWhiteBoardRepository.updateNwbMetadata(nurseWhiteBoardMetadata);
		List<NurseWhiteBoardEditType> nurseWhiteBoardEditTypes = nurseWhiteBoardMetadata.getNurseWhiteBoardEditTypes();
		if(null != nurseWhiteBoardEditTypes ){
			saveNwbMetadataEditType(nurseWhiteBoardEditTypes,nurseWhiteBoardMetadata.getCode());
		}

		List<NurseWhiteBoardMetadata> items = nurseWhiteBoardMetadata.getItems();
		if(null != items && items.size() > 0){
			batchSaveNwbMetadata(items,null,true);
		}
		//缓存tv
		return saveCount;
	}
	
	/**
	 * 保存编辑类型
	 * @param nwbEditTypes
	 * @param code
	 */
	private void saveNwbMetadataEditType(List<NurseWhiteBoardEditType> nwbEditTypes,String code){
		if( null == nwbEditTypes || nwbEditTypes.size() == 0){
			return ;
		}
		//编辑类型个数
		int size = nwbEditTypes.size();
		String templateId = nwbEditTypes.get(0).getTemplateId();
		if(size > 1){
			//大于1删除全部类型
			nurseWhiteBoardRepository.deleteNwbEditType(code,templateId);
		}
		
		for (NurseWhiteBoardEditType nurseWhiteBoardEditType : nwbEditTypes ) {
			nurseWhiteBoardEditType.setMetadataCode(code);
			
			if(size > 1){
				//直接插入
				nurseWhiteBoardRepository.saveNwbEditType(nurseWhiteBoardEditType);
			}else{
				//存在,修改,否则,删除,保存
				if(nurseWhiteBoardRepository.getNwbEditTypeCount(nurseWhiteBoardEditType.getType(), 
						nurseWhiteBoardEditType.getCode(), nurseWhiteBoardEditType.getMetadataCode(),templateId)> 0){
					nurseWhiteBoardRepository.updateNwbEditType(nurseWhiteBoardEditType);
				}else{
					nurseWhiteBoardRepository.deleteNwbEditType(code,templateId);
					nurseWhiteBoardRepository.saveNwbEditType(nurseWhiteBoardEditType);
				}
			}
		}
	}
	
	/**
	 * 设置NDA显示类型
	 * @param nwbMetadata
	 * @return
	 */
	private NurseWhiteBoardMetadata setInputTypeToNwbMetadata(NurseWhiteBoardMetadata nwbMetadata){
		if(null == nwbMetadata){
			return nwbMetadata;
		}
		
		List<NurseWhiteBoardEditType> nwbEditTypes = nwbMetadata.getNurseWhiteBoardEditTypes();
		if(null == nwbEditTypes || nwbEditTypes.size() == 0){
			nwbMetadata.setInputType("TEXT");
		}else{
			/*TEXT
			BEDLIST
			SUBBEDLISTFRQ
			BEDLISTFRQ*/
			StringBuffer sb = new StringBuffer();
			for (NurseWhiteBoardEditType nwbEditType : nwbEditTypes) {
				if("patient".equals(nwbEditType.getCode())){
					sb.append("BEDLIST");
				}else if("item".equals(nwbEditType.getCode())){
					sb.append("SUB");
				}else if("freq".equals(nwbEditType.getCode()) ||
						"bloodGluFreq".equals(nwbEditType.getCode())){
					sb.append("FRQ");
				}else{
					sb.append("TEXT");
				}
			}
			
			if(sb.toString().contains("SUB")){
				nwbMetadata.setInputType("SUBBEDLISTFRQ");
			}else if(sb.toString().contains("FRQ")){
				nwbMetadata.setInputType("BEDLISTFRQ");
			}else if(sb.toString().contains("BEDLIST")){
				nwbMetadata.setInputType("BEDLIST");
			}else{
				nwbMetadata.setInputType("TEXT");
			}
		}
		
		return nwbMetadata;
	}

	@Override
	public int deleteNwbMetadataById(String id) {
		return nurseWhiteBoardRepository.deleteNwbMetadata(id,null,null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NurseWhiteBoardMetadata> getNurseWhiteBoardMetadatas(
			String deptCode, String code , boolean isCache) {
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = null;
		//取缓存数据
		if(!isCache && SuperCacheUtil.CACHE_DATA.get(MnisConstants.NWB_METADATA_CACHE + deptCode) != null){
			nurseWhiteBoardMetadatas = (List<NurseWhiteBoardMetadata>)SuperCacheUtil.CACHE_DATA.
					get(MnisConstants.NWB_METADATA_CACHE + deptCode);
			if(!nurseWhiteBoardMetadatas.isEmpty()){
				return nurseWhiteBoardMetadatas;
			}
		}
		// 获取顶层数据
		List<NurseWhiteBoardMetadata> nurseWhiteBoardTopMetadatas = nurseWhiteBoardRepository
				.getNurseWhiteBoardTopMetadatas(deptCode, code);

		// 获取子项数据
		nurseWhiteBoardMetadatas = getNurseWhiteBoardChildrenMetadata(nurseWhiteBoardTopMetadatas);
		//保存再缓存
		SuperCacheUtil.CACHE_DATA.put(MnisConstants.NWB_METADATA_CACHE + deptCode, nurseWhiteBoardMetadatas);
		return nurseWhiteBoardMetadatas;
	}

	@Override
	public List<NurseWhiteBoardMetadata> getNurseWhiteBoardTopMetadatas(
			String deptCode, String code) {
		List<NurseWhiteBoardMetadata> nurseWhiteBoardTopMetadatas = nurseWhiteBoardRepository
				.getNurseWhiteBoardTopMetadatas(deptCode, code);
		return nurseWhiteBoardTopMetadatas;
	}

	/**
	 * 迭代处理数据源树形结构
	 * 
	 * @param nurseWhiteBoardMetadatas
	 * @return
	 */
	private List<NurseWhiteBoardMetadata> getNurseWhiteBoardChildrenMetadata(
			List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas) {

		for (NurseWhiteBoardMetadata nurseWhiteBoardMetadata : nurseWhiteBoardMetadatas) {
			String id = String.valueOf(nurseWhiteBoardMetadata.getCode());
			List<NurseWhiteBoardMetadata> nurseWhiteBoardChildrenMetadatas = nurseWhiteBoardRepository
					.getNurseWhiteBoardMetadatasByIds(null, id);
			nurseWhiteBoardMetadata.setItems(nurseWhiteBoardChildrenMetadatas);
			// 迭代处理
			getNurseWhiteBoardChildrenMetadata(nurseWhiteBoardChildrenMetadatas);
		}
		return nurseWhiteBoardMetadatas;
	}

	/************************* 小白板记录 ***********************************************/

	@Override
	public List<NurseWhiteBoardRecord> getNurseWhiteBoardRecords(
			String deptCode, String code, String recordDate,String nurseCode) {
		Date[] queryDates = null;
		Date queryDate = null;
		if (StringUtils.isBlank(recordDate)) {
			queryDate = new Date();
		} else {
			queryDate = DateUtil.parse(recordDate.substring(0, 10),DateFormat.YMD);
		}
		queryDates = DateUtil.getQueryRegionDates(queryDate);
		//关注患者
		List<String> patIds = null;
		if(StringUtils.isNotBlank(nurseCode)){
			patIds = patientService.getAttention(nurseCode, deptCode);
		}
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardRepository
				.getNurseWhiteBoardRecords(deptCode, code, patIds, queryDates[0],
						queryDates[1]);
 		
		return nurseWhiteBoardRecords;
	}

	@Override
	public List<NurseWhiteBoardRecordItem> getNurseWhiteBoardRecordItems(
			String deptCode, String recordDate) {
		Date[] queryDates = null;
		Date queryDate = null;
		if (StringUtils.isBlank(recordDate)) {
			queryDate = new Date();
		} else {
			queryDate = DateUtil.parse(recordDate.substring(0, 10),
					DateFormat.YMD);
		}

		queryDates = DateUtil.getQueryRegionDates(queryDate);
		List<NurseWhiteBoardRecordItem> nurseWhiteBoardRecordItems = nurseWhiteBoardRepository
				.getNurseWhiteBoardRecordItems(deptCode, null, queryDates[0],
						queryDates[1]);
		return nurseWhiteBoardRecordItems;
	}


	@Override
	public int insertNurseWhiteBoardRecordInfos(
			List<NurseWhiteBoardRecordInfo> nurseWhiteBoardRecordInfos) {
		int insertCount = 0;
		for (NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo : nurseWhiteBoardRecordInfos) {
			if(null == nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos() || nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos().size() == 0){
				insertCount = nurseWhiteBoardRepository.insertNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
			}else{
				insertCount = nurseWhiteBoardRepository
						.insertNurseWhiteBoardRecordInfoAndItems(nurseWhiteBoardRecordInfo);
			}
		
		}
		return insertCount;
	}
	
	@Override
	public int updateNurseWhiteBoardRecordInfos(
			List<NurseWhiteBoardRecordInfo> nurseWhiteBoardRecordInfos) {
		int insertCount = 0;
		for (NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo : nurseWhiteBoardRecordInfos) {
			if(null == nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos() || nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos().size() == 0){
				insertCount = nurseWhiteBoardRepository.updateNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
			}else{
				insertCount = nurseWhiteBoardRepository
						.insertNurseWhiteBoardRecordInfoAndItems(nurseWhiteBoardRecordInfo);
			}
		
		}
		return insertCount;
	}
	
	@Override
	public int insertNurseWhiteBoardRecordItemInfos(
			List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos) {
		int insertCount = 0;
		for (NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo : nurseWhiteBoardRecordItemInfos) {
			insertCount = nurseWhiteBoardRepository
					.insertNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfo);
		}
		return insertCount;
	}

	/*@Override
	public void execWhiteBoard(List<NurseWhiteBoardRecordInfo> recordList){
		
		//获取所有执行的记录
		for(int i = 0;i<recordList.size();i++){
			NurseWhiteBoardRecordInfo record = recordList.get(i);
			//查询患者信息
			Patient patient = patientRepository.getPatientByPatId(record.getPatId());
			//任务更新
			List<BodySignItem> signItems = new ArrayList<BodySignItem>();//生命体征明细信息
			for(NurseWhiteBoardRecordItemInfo detail : record.getNurseWhiteBoardRecordItemInfos()){
				detail.setStatus(NurseWhiteBoardConstants.WHITE_BOARD_STATUS_F);//完成状态
				nurseWhiteBoardRepository.execWhiteBoard(detail);
				BodySignItem item = new BodySignItem();
				item.setItemCode(detail.getRecordItemCode());//项目编号
				item.setItemName(detail.getRecordItemName());//项目名称
				item.setItemValue(detail.getRecordItemName());//项目值
				item.setMeasureNoteCode("moren");
				item.setMeasureNoteName("默认");
				item.setRecordDate(detail.getRecordItemDate());//记录时间
				item.setPatId(detail.getItemPatId());//患者ID
				signItems.add(item);
			}
			//写入生命体征
			BodySignRecord signRecord = new BodySignRecord();
			signRecord.setDeptCode(record.getDeptCode());//科室编号
			signRecord.setBedCode(patient.getBedCode());//床位编号
			signRecord.setPatientId(record.getPatId());//患者编号
			signRecord.setPatientName(patient.getName());//患者姓名
			signRecord.setRecordDay(DateUtil.format(record.getRecordDate(),DateFormat.YMD));//记录日期
			signRecord.setRecordTime(DateUtil.format(record.getRecordDate(),DateFormat.HMS));//记录时间
			signRecord.setRecordNurseCode(record.getRecordNurseCode());//记录员工编号
			signRecord.setRecordNurseName(record.getRecordNurseName());//记录员工姓名
			signRecord.setBodySignItemList(signItems);
			//反写生命体征
			bodySignService.saveBodySignRecord(signRecord);
		}
	}*/
	
	@Override
	public Map<String,String> getMetadataName(String deptCode,String code){
		return nurseWhiteBoardRepository.getMetadataName(deptCode,code);
	}

	@Override
	public Map<String,Object> getNurseWhiteBoardMetaValues() {
		return nurseWhiteBoardRepository.getMetadataFreq();
	}

	@Override
	public void execWhiteBoardItem(String recordId) {
		nurseWhiteBoardRepository.execWhiteBoardItem(recordId);
	}

	@Override
	public int saveNurseWhiteBoardRecord(
			NurseWhiteBoardInBo nurseWhiteBoardInBo, boolean isOnlySave,
			SysUser user, String deptCode) {
		Map<String, String> metadataMap = getMetadataName(deptCode, null);
		
		//是否可编辑的项目
		List<String> editCodes = nurseWhiteBoardRepository.getMetadataCodes(deptCode, MnisConstants.NWB_EDIT);
		// 解析传入，获取小白板数据
		List<NurseWhiteBoardRecordInfo> recordList = NurseWhiteBoardUtil
				.getRecordInfoList(nurseWhiteBoardInBo, metadataMap, editCodes, user);
		
		/***************收集待删除项目*********************/
		//系统已有code
		List<String> existPatCodes = nurseWhiteBoardRepository.getExistNwbRecordCodes(deptCode);
		//视图记录code
//		List<String> existViewPatCodes = nurseWhiteBoardRepository.getExistNwbViewRecordCodes(deptCode);
		List<String> existViewPatCodes = new ArrayList<String>();
		List<String> newPatCodes = new ArrayList<String>();
		

		// 新增项目
		List<NurseWhiteBoardRecordInfo> saveRecordInfos = new ArrayList<NurseWhiteBoardRecordInfo>();
		// 修改项目
		List<NurseWhiteBoardRecordInfo> updateRecordInfos = new ArrayList<NurseWhiteBoardRecordInfo>();

		//判断项目
		for (NurseWhiteBoardRecordInfo recordInfo : recordList) {
			StringBuffer sb = new StringBuffer();
			sb.append(recordInfo.getRecordCode());
			if(StringUtils.isNotBlank(recordInfo.getPatId())){
				sb.append(MnisConstants.LINE);
				sb.append(recordInfo.getPatId());
			}
			//患者与编号组合
			newPatCodes.add(sb.toString());
			recordInfo.setValid(true);
			if(nurseWhiteBoardRepository.isExistNwbRecord(recordInfo.getRecordCode(), recordInfo.getPatId(),deptCode)){
				updateRecordInfos.add(recordInfo);
			}else{
				saveRecordInfos.add(recordInfo);
			}
		}
		//1. 判断新增项目
		insertNurseWhiteBoardRecordInfos(saveRecordInfos);
		//2. 判断修改项目
		updateNurseWhiteBoardRecordInfos(updateRecordInfos);
		
		//3.删除项目
		for (String existViewePatCode : existViewPatCodes) {
			//新增记录不包含视图中的code
			if(!newPatCodes.contains(existViewePatCode)){
				String[] patCodes = existViewePatCode.split(MnisConstants.LINE);
				
				String patId = null;
				if(patCodes.length > 1){
					patId = patCodes[1];
				}
				//库记录中包含视图中的code
				if(existPatCodes.contains(existViewePatCode)){
					//有效处理
					if(nurseWhiteBoardRepository.isExistNwbRecord(patCodes[0], patId,deptCode)){
						nurseWhiteBoardRepository.delete(patCodes[0], patId, deptCode);
					}
				}else{
					NurseWhiteBoardRecordInfo deleteRecordInfo = new NurseWhiteBoardRecordInfo();
					deleteRecordInfo.setDeptCode(deptCode);
					deleteRecordInfo.setRecordCode(patCodes[0]);
					deleteRecordInfo.setRecordDate(new Date());
					if(StringUtils.isBlank(patId)){
						deleteRecordInfo.setRecordValue(patCodes[0]);
					}else{
						deleteRecordInfo.setPatId(patId);
					}
					deleteRecordInfo.setRandomId(patCodes[0]);
					deleteRecordInfo.setValid(false);
					deleteRecordInfo.setRecordNurseCode(user.getLoginName());
					deleteRecordInfo.setRecordNurseName(user.getName());
					nurseWhiteBoardRepository.insertNurseWhiteBoardRecordInfo(deleteRecordInfo);
				}
			}
		}
		return 0;
	}

	@Override
	public int batchSaveNurseWhiteBoardRecord(
			List<NurseWhiteBoardInBo> nurseWhiteBoardInBos,boolean isOnlySave,SysUser user,String deptCode) {
		
		if(nurseWhiteBoardInBos.isEmpty()){
			return 0;
		}
		Map<String, String> metadataMap = getMetadataName(deptCode,null);
		List<NurseWhiteBoardRecordInfo> recordList = new ArrayList<NurseWhiteBoardRecordInfo>();
		//获取查询的最新时间
		String recordLastDate = MnisConstants.EMPTY;
		if(null != SuperCacheUtil.CACHE_DATA.get(MnisConstants.QUERY_EDIT_NWB_DATE + deptCode)){
			recordLastDate = (String)SuperCacheUtil.CACHE_DATA.get(MnisConstants.QUERY_EDIT_NWB_DATE + deptCode);
		}else{
			recordLastDate = DateUtil.format(DateFormat.FULL);
		}
		//保存时间到现在已停止的医嘱(患者与编号的组合)
		Map<String, String> orderDataMap = getNwbOrderDataMap(deptCode, recordLastDate);
		
		/***************收集待删除项目*********************/
		//系统已有code
		List<String> existPatCodes = nurseWhiteBoardRepository.getExistNwbRecordCodes(deptCode);
		//保存最新时间之前视图数据
		List<String> existViewBeforePatCodes = nurseWhiteBoardRepository.getExistNwbViewRecordCodes(deptCode,recordLastDate);
		List<String> newPatCodes = new ArrayList<String>();
		//是否可编辑的项目
		List<String> editCodes = nurseWhiteBoardRepository.getMetadataCodes(deptCode, MnisConstants.NWB_EDIT);
		/**************对项目进行增删改操作*********************************/
		//新增项目
		List<NurseWhiteBoardRecordInfo> saveRecordInfos = new ArrayList<NurseWhiteBoardRecordInfo>();
		//修改项目
		List<NurseWhiteBoardRecordInfo> updateRecordInfos = new ArrayList<NurseWhiteBoardRecordInfo>();
		int saveCount = 0;
		for (NurseWhiteBoardInBo nurseWhiteBoardInBo : nurseWhiteBoardInBos) {
			recordList.clear();
			saveRecordInfos.clear();
			updateRecordInfos.clear();
			
			
			// 解析传入，获取小白板数据
			recordList = NurseWhiteBoardUtil
					.getRecordInfoList(nurseWhiteBoardInBo, metadataMap, editCodes , user);
			
			//根据已有code获取系统记录code
			if(recordList.isEmpty()){
				continue;
			}
			
			//判断项目
			for (NurseWhiteBoardRecordInfo recordInfo : recordList) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(recordInfo.getRecordCode());
				if(StringUtils.isNotBlank(recordInfo.getPatId())){
					sb.append(MnisConstants.LINE);
					sb.append(recordInfo.getPatId());
				}
				
				if( editCodes .contains(recordInfo.getRecordCode()) 
						&& orderDataMap.containsKey(sb.toString())){
					//表示数据同步已暂停该医嘱
					
					if(nurseWhiteBoardRepository.isExistNwbRecord(recordInfo.getRecordCode(), recordInfo.getPatId(),deptCode)){
						nurseWhiteBoardRepository.delete(recordInfo.getRecordCode(), recordInfo.getPatId(), deptCode);
					}else{
						insertDeleteNwbRecord(recordInfo.getPatId(), recordInfo.getRecordCode(), deptCode, user.getName(),user.getLoginName());
					}
					
					continue;
				}
				
				//患者与编号组合
				newPatCodes.add(sb.toString());
				recordInfo.setValid(true);
				if(nurseWhiteBoardRepository.isExistNwbRecord(recordInfo.getRecordCode(), recordInfo.getPatId(),deptCode)){
					updateRecordInfos.add(recordInfo);
				}else{
					saveRecordInfos.add(recordInfo);
				}
			}
			//1. 判断新增项目
			insertNurseWhiteBoardRecordInfos(saveRecordInfos);
			//2. 判断修改项目
			updateNurseWhiteBoardRecordInfos(updateRecordInfos);
		}
		
		//3.删除项目
		for (String existViewePatCode : existViewBeforePatCodes) {
			//新增记录不包含视图中的code
			if(!newPatCodes.contains(existViewePatCode)){
				String[] patCodes = existViewePatCode.split(MnisConstants.LINE);
				if(!editCodes.contains(patCodes[0])){
					continue;
				}
				String patId = null;
				if(patCodes.length > 1){
					patId = patCodes[1];
				}
				//库记录中包含视图中的code
				if(existPatCodes.contains(existViewePatCode)){
					//有效处理
					nurseWhiteBoardRepository.delete(patCodes[0], patId, deptCode);
				}else{
					insertDeleteNwbRecord(patId,patCodes[0],deptCode,user.getName(),user.getLoginName());
				}
			}
		}
		return saveCount;
	}
	
	/**
	 * 删除数据
	 * @param patId
	 * @param code
	 * @param deptCode
	 * @param userName
	 * @param userCode
	 */
	private void insertDeleteNwbRecord(String patId,String code,String deptCode,String userName,String userCode){
			NurseWhiteBoardRecordInfo deleteRecordInfo = new NurseWhiteBoardRecordInfo();
			deleteRecordInfo.setDeptCode(deptCode);
			deleteRecordInfo.setRecordCode(deptCode);
			deleteRecordInfo.setRecordDate(new Date());
			if(StringUtils.isBlank(patId)){
				deleteRecordInfo.setRecordValue(deptCode);
			}else{
				deleteRecordInfo.setPatId(patId);
			}
			deleteRecordInfo.setRandomId(deptCode);
			deleteRecordInfo.setValid(false);
			deleteRecordInfo.setRecordNurseCode(userCode);
			deleteRecordInfo.setRecordNurseName(userName);
			nurseWhiteBoardRepository.insertNurseWhiteBoardRecordInfo(deleteRecordInfo);
	}
	
	@Override
	public Map<String, Object> getSubMetadatas(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		List<String> parents = nurseWhiteBoardRepository.getParentMetadatas(deptCode);
		Map<String, Object> subMetadatas = new HashMap<String, Object>();
		Map<String, String> items = new HashMap<String, String>();
		for (String parent : parents) {
			items.clear();
			items =  nurseWhiteBoardRepository.getSubMetadatas(deptCode,parent);
			if(!items.isEmpty())
				subMetadatas.put(parent, items);
		}
		
		return subMetadatas;
	}

	@Override
	public List<String> getFreqMetadataCodes(String deptCode) {
		return nurseWhiteBoardRepository.getFreqMetadataCodes(deptCode);
	}

	@Override
	public List<NurseWhiteBoardRecord> getNurseWhiteBoardDynamicRecords(
			String deptCode, String code) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		
		if(StringUtils.isBlank(code)){
			code = null;
		}
		
		List<NurseWhiteBoardRecord> records = nurseWhiteBoardRepository.getNurseWhiteBoardDynamicRecords(deptCode, code);
		//已停止医嘱(上一次查询到现在的停止医嘱)
		Map<String, String> stopOrderMap = getNwbOrderDataMap(deptCode,(String)SuperCacheUtil.CACHE_DATA.
				get(MnisConstants.PRE_QUERY_NWB_DATE + deptCode) );
		List<String> dosageCodes = nurseWhiteBoardRepository.getMetadataCodes(deptCode,MnisConstants.NWB_DOSAGE);
		List<String> editCodes = nurseWhiteBoardRepository.getMetadataCodes(deptCode, MnisConstants.NWB_EDIT);
		List<NurseWhiteBoardRecordFreqPatInfo> nwbFreqPatInfos = new ArrayList<NurseWhiteBoardRecordFreqPatInfo>();
		for (NurseWhiteBoardRecord nurseWhiteBoardRecord : records) {
			
			//排除已停止的医嘱
			
			for (NurseWhiteBoardRecordFreqInfo nurseWhiteBoardRecordFreqInfo : nurseWhiteBoardRecord.getNurseWhiteBoardRecordFreqInfos()) {
				if(null == nurseWhiteBoardRecordFreqInfo.getNurseWhiteBoardRecordFreqPatInfos() 
						|| nurseWhiteBoardRecordFreqInfo.getNurseWhiteBoardRecordFreqPatInfos().isEmpty()){
					continue;
				}
				nwbFreqPatInfos.clear();
				
				for (NurseWhiteBoardRecordFreqPatInfo freqPatInfo : nurseWhiteBoardRecordFreqInfo.getNurseWhiteBoardRecordFreqPatInfos()) {
					if(StringUtils.isBlank(freqPatInfo.getPatId())){
						continue;
					}
					String key = nurseWhiteBoardRecord.getRecordCode() + MnisConstants.LINE + freqPatInfo.getPatId();
					//排除停止医嘱
					if(editCodes.contains(nurseWhiteBoardRecord.getRecordCode()) && stopOrderMap.containsKey(key)){
						nwbFreqPatInfos.add(freqPatInfo);
						continue;
					}
					
					//不包含剂量
					if(!dosageCodes.contains(nurseWhiteBoardRecord.getRecordCode())){
						continue;
					}
					
					String dosage = nurseWhiteBoardRepository.getDosageByData(deptCode, freqPatInfo.getPatId(), nurseWhiteBoardRecord.getRecordCode());
					
					if(StringUtils.isNotBlank(dosage) && StringUtils.isNotBlank(freqPatInfo.getPatInfo())){
						freqPatInfo.setPatInfo(freqPatInfo.getPatInfo().replace("床", "("+ dosage + ")床"));
					}
					
				}
				//重新设置患者列表
				nurseWhiteBoardRecordFreqInfo.getNurseWhiteBoardRecordFreqPatInfos().removeAll(nwbFreqPatInfos);
				for (NurseWhiteBoardRecordFreqPatInfo nurseWhiteBoardRecordFreqPatInfo : nwbFreqPatInfos) {
					nurseWhiteBoardRepository.delete(nurseWhiteBoardRecord.getRecordCode(), nurseWhiteBoardRecordFreqPatInfo.getPatId(), deptCode);
				}
				
			}
		}
		
		return records;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NurseWhiteBoardMetadataTV> getNurseWhiteBoardTVMetadatas(
			String deptCode, String masterId ,boolean isCache) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		List<NurseWhiteBoardMetadataTV> nurseWhiteBoardMetadataTVs  = (List<NurseWhiteBoardMetadataTV>)SuperCacheUtil
				.CACHE_DATA.get(MnisConstants.NWB_METADATA_TV_CACHE + deptCode);
		if(isCache || null == nurseWhiteBoardMetadataTVs || nurseWhiteBoardMetadataTVs.isEmpty()){
			nurseWhiteBoardMetadataTVs = nurseWhiteBoardRepository.getNurseWhiteBoardTVMetadatas(deptCode, masterId);
			SuperCacheUtil.CACHE_DATA.put(MnisConstants.NWB_METADATA_TV_CACHE + deptCode, nurseWhiteBoardMetadataTVs);
		}
		return nurseWhiteBoardMetadataTVs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NurseWhiteBoardTemplate> getNwbTemplates(
			String deptCode, String id ,boolean isCache) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		
		List<NurseWhiteBoardTemplate> templates  = (List<NurseWhiteBoardTemplate>)SuperCacheUtil
				.CACHE_DATA.get(MnisConstants.NWB_METADATA_TEMPLATE_CACHE + deptCode);
		if(isCache || null == templates || templates.isEmpty()){
			templates = nurseWhiteBoardRepository.getNwbTemplates(deptCode, id);
			SuperCacheUtil.CACHE_DATA.put(MnisConstants.NWB_METADATA_TEMPLATE_CACHE + deptCode, templates);
		}
		return templates;
	}
	@Override
	public int saveNwbTemplate(NurseWhiteBoardTemplate template) {
		return 0;
	}
	@Override
	public int updateNwbTemplate(NurseWhiteBoardTemplate template) {
		return 0;
	}
	@Override
	public int deleteNwbTemplate(String deptCode, String id) {
		return 0;
	}

	@Override
	public Map<String, String> getNwbOrderDataMap(String deptCode,
			String lastRecordDate) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if(StringUtils.isBlank(deptCode)){
			return dataMap;
		}
		
		List<Map<String, String>> dataMapList = nurseWhiteBoardRepository
				.getNwbOrderDataMap(deptCode, lastRecordDate);
		
		for (Map<String, String> map : dataMapList) {
			dataMap.put(map.get("code"), map.get("name"));
		}
		return dataMap;
	}
	
}
