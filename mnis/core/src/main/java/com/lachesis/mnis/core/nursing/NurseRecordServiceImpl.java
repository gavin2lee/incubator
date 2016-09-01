package com.lachesis.mnis.core.nursing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.util.DateUtil;
@Service("nurseRecordService")
public class NurseRecordServiceImpl implements NurseRecordService{
	private static final Logger LOGGER = Logger.getLogger( NurseRecordServiceImpl.class );
	@Autowired
	private NurseRecordRepository nurseRecordRepository;
	
	private static final String INTAKE_CODE = "rl";
	private static final String OUTPUT_CODE = "cl";
	
	@Override
	public List<NurseRecord> getNurseRecord(String day, String patientId) throws ParseException{
		List<NurseRecord> nrRecordList = null; 
		String[] timeEndPoints = null;

		timeEndPoints = DateUtil.getTimeEndPoints(day);
		if( timeEndPoints!=null && timeEndPoints.length>1 ) {
			List<NurseRecordEntity> nrRecordEntityList = 
					nurseRecordRepository.selectNurseRecord(patientId, timeEndPoints[0], timeEndPoints[1]);
			nrRecordList = new ArrayList<NurseRecord>();
			for(NurseRecordEntity nrEntity:nrRecordEntityList) {
				nrRecordList.add( getNurseRecordFrom( nrEntity) );
			}
		}
		return nrRecordList;
	}

	@Override
	public NurseRecordConfig getNurseRecordConfig(String deptCode) {
		List<NurseRecordSpecItem> specItems = nurseRecordRepository.selectNurseRecordSpecItems(null);
		List<NurseRecordSpecItem> specTreeItems = nurseRecordRepository.selectNurseRecordTreeItems(null);
		NurseRecordConfig nrConfig = new NurseRecordConfig();
		nrConfig.setNurseRecordSpecItemList(specItems);
		
		for(NurseRecordSpecItem treeItem:specTreeItems) {
			if( treeItem.getItemCode().equals(INTAKE_CODE) ) {
				nrConfig.setInputItemList( treeItem.getSubItemList() );
			} else if( treeItem.getItemCode().equals(OUTPUT_CODE) ) {
				nrConfig.setOutputItemList( treeItem.getSubItemList() );
			}
		}
		
		return nrConfig;
	}

	@Override
	public int saveNurseRecord(NurseRecord nurseRecord, boolean modifying) {
		NurseRecordEntity nrEntity = new NurseRecordEntity();
		nrEntity.setNurseRecord(nurseRecord);
		
		int flag = MnisConstants.CODE_NULL_ARGUMENT;
		if(!nrEntity.adapt() || !nrEntity.validate() ) { // 数据不符合要求
			LOGGER.error("NurseRecordEntity is does not pass validation!!");
			return flag;
		}
		
		if( !modifying ){
			flag = nurseRecordRepository.insertNurseRecord(nrEntity);
		} else { // 修改 = 删除 + 插入
			String masId = nurseRecord.getMasRecordId();
			if( masId!=null ) {
				nurseRecordRepository.deleteNurseRecord( nurseRecord.getMasRecordId() );
				flag = nurseRecordRepository.insertNurseRecord( nrEntity );								
			}
		}
		return flag;
	}
	
	/**
	 * 从查询得到的生命体征模式的记录还原为护理记录
	 * 分离出生命体征和护理记录特殊项，以便修改
	 * @return 护理记录
	 */
	public NurseRecord getNurseRecordFrom(NurseRecordEntity nrEntity) {	
		NurseRecord nr = nrEntity.getNurseRecord();
		if(nr!=null) {
			// 分离this.itemList中的生命体征项和护理记录特殊项
			for(BodySignItem bsItem: nrEntity.getItemList() ) {
				//if( dictManager.isNurseRecordSpec( bsItem.getBodySignDict().getItemCode() ) ) {
				//	nr.addToNurseRecordSpecItemList( getFromBodySign( bsItem ) );
				//} else {
					nr.addToBodySignItemList( bsItem );
				//}
			}		
		}

		return nr;
	}

	//	从生命体征项目转换为护理记录特殊项
	public NurseRecordSpecItem getFromBodySign(BodySignItem bsItem){
		NurseRecordSpecItem specItem = new NurseRecordSpecItem();
		specItem.setDetailRecordId( String.valueOf( bsItem.getDetailRecordId()) );
		specItem.setItemCode( bsItem.getBodySignDict().getItemCode() );
		specItem.setItemName( bsItem.getItemName() );
		specItem.setItemValue( bsItem.getItemValue() );
		specItem.setItemValueCode( bsItem.getMeasureNoteCode() );
		return specItem;
	}

	@Override
	public TemplateBean getTemplate(String templateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TemplateBean> getTemplateList(String deptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TemplateItemBean> getRecordItemsById(String templateId,
			String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertNurseRecord(TemplateBean requestBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNurseRecord(TemplateBean requestBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TemplateBean> getRecordListByPatientId(String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TemplateItemBean> getRecordItemsById(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocRecordById(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateBean getTableRecordById(Integer templateId, String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateBean getTableRecordById(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssistantBean> getAssistantList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAssisValue(Integer parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateBean getChuangyeTemplateData(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRecord(Integer parseInt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TemplateItemBean getOneDayBodySignDataFromNurseDoc(
			String hospitalNo, String day, String[] elements) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/////////////////////////////////////////////////
	////////////////////////////////////////////////

//	@Override
//	public TemplateBean getTemplate(String templateId) {
//		return nurseRecordMapper.getTemplate(templateId);
//	}
//
//	@Override
//	public List<TemplateBean> getTemplateList(String deptId) {
//		return nurseRecordMapper.getTemplateList(deptId);
//	}
//
//	@Override
//	public List<TemplateItemBean> getRecordItemsById(String templateId,
//			String patientId) {
//		if (patientId == null) {
//			return null;
//		} else {
//			Integer lastRecordId = nurseRecordMapper.getTopRecord(
//					Integer.parseInt(templateId), patientId);
//			// Integer lastRecordId = 156;
//			if (lastRecordId == null) {
//				return null;
//			}
//			return nurseRecordMapper.getRecordItemsById(lastRecordId);
//		}
//	}
//
//	@Override
//	public String insertNurseRecord(TemplateBean requestBean) {
//		Integer oraginRecordId = nurseRecordMapper
//				.getCurrentRecordId("ENR_JL01");
//
//		// 获取ID，杭创在一个表中记录了所有表的ID，每次添加记录都要先更表那个表，取得ID后再执行保存操作
//		nurseRecordMapper.getNewRecordId("ENR_JL01", 1);
//		Integer recordId = nurseRecordMapper.getCurrentRecordId("ENR_JL01");
//		if (recordId.intValue() - 1 != oraginRecordId.intValue()) {
//			throw new RuntimeException("保存失败！请重试");
//		}
//
//		if ("2000048".equals(requestBean.getDocType())) {
//			// 病例类模版
//			Integer lastRecordId = nurseRecordMapper.getTopRecord(
//					requestBean.getTemplateId(), requestBean.getPatientId());
//			if (lastRecordId != null) {
//				throw new RuntimeException("同类模版只能录入一次！");
//			}
//			requestBean.setDocFormat("0");
//			requestBean.setRecordId(recordId);
//			requestBean.setRecordName(requestBean.getTemplateName());
//			requestBean.setRecordRows(0);
//			LOGGER.debug("-------------recordId:" + recordId);
//			this.persistNurseRecord(requestBean);
//			nurseRecordMapper.insertNurseDocRecordItem(
//					requestBean.getRecordId(), requestBean.getValue());
//		} else {
//			// 表格类模版
//			int rows = requestBean.getItemList().size();
//			Integer originRecordItemId = nurseRecordMapper
//					.getCurrentRecordId("ENR_JL02");
//			nurseRecordMapper.getNewRecordId("ENR_JL02", rows);
//			Integer recordItemId = nurseRecordMapper
//					.getCurrentRecordId("ENR_JL02");
//			if (recordItemId - rows != originRecordItemId) {
//				throw new RuntimeException("保存失败！请重试!");
//			}
//			requestBean.setDocFormat("2");
//			requestBean.setRecordId(recordId);
//			requestBean.setRecordName(DateUtil.formatDateToString(
//					requestBean.getRecordTime(), DateUtil.DATE_FORMAT_HM));
//
//			// 计算行数据，杭创不能提供模版中列的计算公式，所以我们新建了一张表来配置，一个个模版查看后找出所有计算列，
//			requestBean.setRecordRows(calRecordRows(requestBean));
//
//			// 保存主记录
//			this.persistNurseRecord(requestBean);
//			List<TemplateItemBean> items = requestBean.getItemList();
//			int i = 1;
//			// 循环保存记录详情
//			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
//				TemplateItemBean templateItemBean = (TemplateItemBean) iterator
//						.next();
//				TemplateItemBean persistItemBean = nurseRecordMapper
//						.getTemplateItemsById(requestBean.getTemplateId(),
//								templateItemBean.getJsnPth());
//				persistItemBean.setRecordItemId(originRecordItemId + i);
//				persistItemBean.setRecordId(requestBean.getRecordId());
//				persistItemBean.setValue(templateItemBean.getValue());
//				LOGGER.debug("----------------tablerecord id: "
//						+ persistItemBean.getRecordId() + ":"
//						+ persistItemBean.getRecordItemId());
//				nurseRecordMapper.insertNurseTableItem(persistItemBean);
//				i++;
//			}
//		}
//
//		return String.valueOf(recordId.intValue());
//	}
//
//	private void persistNurseRecord(TemplateBean requestBean) {
//
//		// 查找杭创的目录表，此表用来组织杭创护理记录的树形，如果不写则杭创不能显示
//		Integer rId = null;
//		if ("2000048".equals(requestBean.getDocType())) {
//			rId = nurseRecordMapper.getNurseTreeMap(requestBean.getPatientId(),
//					0);
//		} else {
//			rId = nurseRecordMapper.getNurseTreeMap(requestBean.getPatientId(),
//					requestBean.getTemplateId());
//		}
//		if (rId == null) {
//			Integer originRecordItemId = nurseRecordMapper
//					.getCurrentRecordId("ENR_JLML");
//			nurseRecordMapper.getNewRecordId("ENR_JLML", 1);
//			Integer recordItemId = nurseRecordMapper
//					.getCurrentRecordId("ENR_JLML");
//			if (recordItemId - 1 != originRecordItemId) {
//				throw new RuntimeException("保存失败！请重试!");
//			}
//
//			nurseRecordMapper.insertNurseTreeMap(recordItemId,
//					requestBean.getPatientId(), requestBean.getDocFormat(),
//					requestBean.getDocType(), requestBean.getTemplateId());
//		}
//
//		// 保存主记录
//		String commitNurseId = nurseRecordMapper.getCommitNurseId(requestBean.getNurseId());
//		requestBean.setCommitNurseId(commitNurseId);
//		requestBean.setCommitTime(new Date());
//		requestBean.setPdaFlag(1);
//		nurseRecordMapper.insertNurseRecord(requestBean);
//
//	}
//
//	// //计算行数据，杭创不能提供模版中列的计算公式，所以我们新建了一张表来配置，一个个模版查看后找出所有计算列，
//	private Integer calRecordRows(TemplateBean requestBean) {
//		Integer tempateId = requestBean.getTemplateId();
//		// 定义行数
//		int rows = 0;
//		for (Iterator iterator = requestBean.getItemList().iterator(); iterator
//				.hasNext();) {
//			TemplateItemBean bean = (TemplateItemBean) iterator.next();
//			if (bean.getColWidth() == null) {
//				continue;
//			}
//			int colWidth = bean.getColWidth().intValue();
//			// 统一换行
//			String a = bean.getValue().replace("\r\n", "\n");
//			try {
//				// 计算行
//				int curRows = calString(colWidth, a);
//
//				// 设置为最大的计算结果
//				if (curRows > rows) {
//					rows = curRows;
//				}
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//
//			bean.setValue(a.replace("\n", "\r\n"));
//		}
//		if (rows == 0) {
//			rows = 1;
//		}
//		LOGGER.debug("--------------rows:" + rows);
//		return rows;
//	}
//
//	private int calString(int length, String str)
//			throws UnsupportedEncodingException {
//		int result = 0;
//		int tempCount = 0;
//
//		for (int i = 0; i < str.length(); i++) {
//			String a = String.valueOf(str.charAt(i));
//			// 如果遇到换行符，则行数加一，计数器设为0
//			if (a.equals("\n")) {
//				System.out.println("换行符");
//				result++;
//				tempCount = 0;
//			} else {
//
//				// 按数据库配置计算行数
//				String tmp = new String(a.getBytes("gb2312"), "iso-8859-1");
//				if ((tempCount + tmp.length()) > length) {
//					result++;
//					tempCount = tmp.length();
//				} else {
//					tempCount += tmp.length();
//				}
//			}
//			System.out.println(tempCount);
//		}
//
//		if (tempCount > 0) {
//			result++;
//		}
//
//		return result;
//	}
//
//	@Override
//	public void updateNurseRecord(TemplateBean requestBean) {
//		TemplateBean queryBean = nurseRecordMapper
//				.getTableRecordById(requestBean.getRecordId());
//		if (queryBean == null) {
//			throw new RuntimeException("记录不存在!");
//		}
//		/*if (queryBean.getRecordStatus().intValue() != 0) {
//			throw new RuntimeException("记录已确认，不能修改!");
//		}*/
//
//		// 病例类模版直接把json字符串保存到数据库
//		if ("2000048".equals(requestBean.getDocType())) {
//			// nurseRecordMapper.updateNurseRecord(requestBean);
//			nurseRecordMapper.updateNurseDocRecordItem(
//					requestBean.getRecordId(), requestBean.getValue());
//		} else {
//
//			// 重新计算行数
//			requestBean.setRecordRows(calRecordRows(requestBean));
//			requestBean.setRecordName(DateUtil.formatDateToString(
//					requestBean.getRecordTime(), DateUtil.DATE_FORMAT_HM));
//			nurseRecordMapper.updateNurseRecord(requestBean);
//
//			// 循环修改记录详情，根据修改标识判断修改还是插入
//			for (Iterator iterator = requestBean.getItemList().iterator(); iterator
//					.hasNext();) {
//				TemplateItemBean bean = (TemplateItemBean) iterator.next();
//				bean.setRecordId(requestBean.getRecordId());
//				if (bean.getEdtFlg() == 0) {
//					continue;
//				} else {
//					TemplateItemBean queryBeanItem = nurseRecordMapper
//							.getRecordItem(requestBean.getRecordId(),
//									bean.getJsnPth());
//					if (queryBeanItem == null) {
//						int rows = 1;
//						Integer originRecordItemId = nurseRecordMapper
//								.getCurrentRecordId("ENR_JL02");
//						nurseRecordMapper.getNewRecordId("ENR_JL02", rows);
//						Integer recordItemId = nurseRecordMapper
//								.getCurrentRecordId("ENR_JL02");
//						if (recordItemId - rows != originRecordItemId) {
//							throw new RuntimeException("保存失败！请重试!");
//						}
//						TemplateItemBean persistItemBean = nurseRecordMapper
//								.getTemplateItemsById(
//										requestBean.getTemplateId(),
//										bean.getJsnPth());
//						persistItemBean.setRecordItemId(originRecordItemId + 1);
//						persistItemBean.setRecordId(requestBean.getRecordId());
//						persistItemBean.setValue(bean.getValue());
//						nurseRecordMapper.insertNurseTableItem(persistItemBean);
//					} else {
//						queryBeanItem.setValue(bean.getValue());
//						nurseRecordMapper.updateNurseRecordItem(queryBeanItem);
//
//					}
//				}
//			}
//
//		}
//	}
//
//	@Override
//	public List<TemplateBean> getRecordListByPatientId(String patientId) {
//
//		return nurseRecordMapper.getRecordListByPatientId(patientId);
//	}
//
//	@Override
//	public List<TemplateItemBean> getRecordItemsById(String recordId) {
//
//		return nurseRecordMapper.getRecordItemsById(Integer.parseInt(recordId));
//	}
//
//	@Override
//	public String getDocRecordById(String recordId) {
//		return nurseRecordMapper.getDocRecordById(Integer.parseInt(recordId));
//	}
//
//	@Override
//	public TemplateBean getTableRecordById(Integer templateId, String patientId) {
//		if (patientId == null) {
//			return null;
//		} else {
//			Integer lastRecordId = nurseRecordMapper.getTopRecord(templateId,
//					patientId);
//			// Integer lastRecordId = 156;
//			if (lastRecordId == null) {
//				return null;
//			}
//			return nurseRecordMapper.getTableRecordById(lastRecordId);
//		}
//	}
//
//	@Override
//	public TemplateBean getTableRecordById(String recordId) {
//		try {
//			return nurseRecordMapper.getTableRecordById(Integer
//					.parseInt(recordId));
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	@Override
//	public List<AssistantBean> getAssistantList() {
//		return nurseRecordMapper.getAssistantList();
//	}
//
//	@Override
//	public String getAssisValue(Integer assId) {
//		return nurseRecordMapper.getAssistValue(assId);
//	}
//
//	@Override
//	public TemplateBean getChuangyeTemplateData(String recordId) {
//		
//		return nurseRecordMapper.getChuangyeTemplateData(recordId);
//	}
//
//	@Override
//	public void deleteRecord(Integer recordId) {
//		nurseRecordMapper.deleteRecord(recordId);
//	}
//
//	@Override
//	public TemplateItemBean getOneDayBodySignDataFromNurseDoc(String hospitalNo, String day, String[] elements){
//		return nurseRecordMapper.getOneDayBodySignDataFromNurseDoc(hospitalNo, day, elements);
//	}
}
