package com.lachesis.mnis.core.inoutmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.InOutManagerService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.DocReportEvent;
import com.lachesis.mnis.core.event.MnisEventPublisher;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManager;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManagerStatistic;
import com.lachesis.mnis.core.inoutmanager.repository.InOutManagerRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("inOutManagerService")
public class InOutManagerServiceImpl implements InOutManagerService {

	@Autowired
	private InOutManagerRepository inOutManagerRepository;

	@Autowired
	private MnisEventPublisher mnisEventPublisher;

	@Autowired
	private IdentityService identityService;

	@Override
	public List<InOutManager> getInOutManagers(String patId, String deptCode,
			Date startDate, Date endDate) {

		if (StringUtils.isBlank(deptCode)) {
			throw new MnisException("传入参数错误!");
		}

		if (null == startDate || endDate == null) {
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = dates[0];
			endDate = dates[1];
		}
		return inOutManagerRepository.getInOutManagers(patId, deptCode,
				startDate, endDate);
	}

	@Override
	public InOutManager getInOutManagerById(int id) {

		return inOutManagerRepository.getInOutManagerById(id);
	}

	@Override
	public InOutManager insertInOutManager(InOutManager inOutManager) {
		if (null == inOutManager) {
			throw new MnisException("无法进行新增出入量!");
		}

		int saveCount = inOutManagerRepository.insertInOutManager(inOutManager);
		if (saveCount == 0) {
			throw new MnisException("保存失败!");
		}

		// 转抄到护理单据
		if (identityService.isSyncDocReport()) {
			copyInOutManagerToDocReport(inOutManager);
		}

		return inOutManager;
	}

	@Override
	public InOutManager updateInOutManager(InOutManager inOutManager) {

		if (null == inOutManager) {
			throw new MnisException("无法进行修改出入量!");
		}

		int updateCount = inOutManagerRepository
				.updateInOutManager(inOutManager);
		if (updateCount == 0) {
			throw new MnisException("修改失败!");
		}
		return inOutManager;
	}

	/**
	 * 出入量转抄到护理文书
	 * 
	 * @param inOutManager
	 */
	private void copyInOutManagerToDocReport(InOutManager inOutManager) {
		DocReportEventEntity docReportEventEntity = new DocReportEventEntity();
		docReportEventEntity.setCreate_person(inOutManager.getCreateUserCode()
				+ MnisConstants.LINE + inOutManager.getCreateUserName());
		String createDay = DateUtil.format(inOutManager.getCreateDate(),
				DateFormat.YMD);
		String createTime = DateUtil.format(inOutManager.getCreateDate(),
				DateFormat.HMS);
		docReportEventEntity.setCreateTime(inOutManager.getCreateDate());
		docReportEventEntity.setDate_list(createDay);
		docReportEventEntity.setTime_list(createTime);
		docReportEventEntity.setInpatient_no(inOutManager.getPatId());
		List<DocReportEventItemEntity> docReportEventItemEntities = new ArrayList<DocReportEventItemEntity>();
		docReportEventEntity.setData_item(docReportEventItemEntities);
		docReportEventEntity.setDept_code(inOutManager.getDeptCode());
		// 入量转抄
		if (StringUtils.isNotBlank(inOutManager.getInItemCode())) {
			//入量名称
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_INPUT_NAME,inOutManager.getInItemName()));
			//入量值
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_IN_TAKE, StringUtil.getStringValue(inOutManager
							.getInItemVal())));
		}
		// 出量转抄
		if (StringUtils.isNotBlank(inOutManager.getOutItemCode())) {
			
			if(!StringUtils.isNotBlank(inOutManager.getInItemCode()))
			{
				//录入出量时，必须插入一条空的入量，不然前端报错
				docReportEventItemEntities.add(new DocReportEventItemEntity(MnisConstants.COPY_DOC_INPUT_NAME,inOutManager.getInItemName()));
				docReportEventItemEntities.add(new DocReportEventItemEntity(MnisConstants.COPY_DOC_IN_TAKE, ""));							
			}
			
			//出量名称
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_OUT_NAME, inOutManager
							.getOutItemCode()));
			//出量值
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_OUT_TAKE, StringUtil.getStringValue(inOutManager
							.getOutItemVal())));
		}
		
		if(StringUtils.isNotBlank(inOutManager.getOutShapeCode())){
			//性状
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_SHAPE,inOutManager.getOutShapeCode()));
			
		}
		
		if(StringUtils.isNotBlank(inOutManager.getOutColorCode())){
			
			//颜色
			docReportEventItemEntities.add(new DocReportEventItemEntity(
					MnisConstants.COPY_DOC_COLOR,inOutManager.getOutColorCode()));
		}
	

		if (!docReportEventItemEntities.isEmpty()) {
			// 发布护理文书转抄事件
			mnisEventPublisher.publish(new DocReportEvent(this,
					docReportEventEntity));
		}
	}

	@Override
	public int deleteById(int id) {
		return inOutManagerRepository.deleteById(id);
	}

	@Override
	public InOutManagerStatistic getInOutManagerStatistic(String patId,
			String deptCode, Date startDate, Date endDate) {
		if (StringUtils.isBlank(deptCode)) {
			throw new MnisException("传入参数错误!");
		}

		if (null == startDate || endDate == null) {
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = dates[0];
			endDate = dates[1];
		}
		return inOutManagerRepository.getInOutManagerStatistic(patId, deptCode,
				startDate, endDate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getOutDics(int dicType) {
		Map<String, String> dicMap = new TreeMap<String, String>();
		String key = "dicMap" + dicType;
		//缓存数据
		if(SuperCacheUtil.CACHE_DATA.containsKey(key)){
			dicMap = (Map<String,String>)SuperCacheUtil.CACHE_DATA.get(key);
			return dicMap;
		}	
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("dicType", dicType);
		List<Map<String, String>> dics = inOutManagerRepository.getOutDics(params);
		
		for (Map<String, String> map : dics) {
			dicMap.put(map.get("code"), map.get("name"));
		}
		
		SuperCacheUtil.CACHE_DATA.put(key, dicMap);
		return dicMap;
	}

}
