package com.lachesis.mnis.core.whiteBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardItemInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordFreqInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;

public class NurseWhiteBoardUtil {
	
	/**
	 * 分解小白板记录
	 * 
	 * @param inbo
	 * @return
	 */
	public static List<NurseWhiteBoardRecordInfo> getRecordInfoList(
			NurseWhiteBoardInBo inbo, Map<String, String> metadataMap, List<String> editCodes , SysUser user) {
		List<NurseWhiteBoardRecordInfo> recordList = new ArrayList<NurseWhiteBoardRecordInfo>();
		if (null == inbo) {
			return recordList;
		}
		
		//是否只读模式
		if(!editCodes.contains(inbo.getRecordCode())){
			return recordList;
		}
		
		// 患者ID列表
		List<String> patIdsList = new ArrayList<>();
		NurseWhiteBoardItemInBo patIdDetail = inbo
				.getSubItem(NurseWhiteBoardConstants.SAVE_RECORD_CODE_PATIENT);
		if (null != patIdDetail) {
			// 病人信息组合 [p1|p2|p3][p4|p5]
			patIdsList = patIdDetail.getItemValues();
		}

		// 内容
		List<String> contentList = new ArrayList<>();
		NurseWhiteBoardItemInBo valueDetail = inbo
				.getSubItem(NurseWhiteBoardConstants.SAVE_RECORD_CODE_CONTENT);
		if (null != valueDetail) {
			// 病人信息组合 [p1|p2|p3][p4|p5]
			contentList = valueDetail.getItemValues();
		}

		int orderNo = 1;
 		
		if (patIdsList.size() == 0) {
			// 主记录
			// 项目内容
			for (String content : contentList) {
				if(StringUtils.isBlank(content)){
					continue;
				}
				NurseWhiteBoardRecordInfo record = new NurseWhiteBoardRecordInfo();
				record.setDeptCode(inbo.getDeptCode());// 科室编号
				record.setRecordDate(new Date());// 记录时间
				record.setRecordCode(inbo.getRecordCode());// 记录项目编号
				record.setRecordName(inbo.getRecordName());// 记录项目名称
				if(StringUtils.isBlank(inbo.getRecordNurseCode())
						|| StringUtils.isBlank(inbo.getRecordNurseName())){
					record.setRecordNurseCode(user.getCode());// 记录人编号
					record.setRecordNurseName(user.getName());// 记录人姓名
				}else{
					record.setRecordNurseCode(inbo.getRecordNurseCode());// 记录人编号
					record.setRecordNurseName(inbo.getRecordNurseName());// 记录人姓名
				}
				record.setRandomId(inbo.getRecordCode());
				record.setOrderNo(orderNo);
				if (valueDetail != null)
					record.setRecordValue(content);
				recordList.add(record);
			}
		} else {
			// 项目内容
			// 获取项目编号
			List<String> itemList = new ArrayList<String>();
			NurseWhiteBoardItemInBo itemsDetail = inbo
					.getSubItem(NurseWhiteBoardConstants.SAVE_RECORD_CODE_ITEM);
			if (null != itemsDetail) {
				// 获取体征项
				itemList = itemsDetail.getItemValues();
			}

			// 频次
			List<String> freqList = new ArrayList<String>();
			NurseWhiteBoardItemInBo freqDetail = inbo
					.getSubItem(NurseWhiteBoardConstants.SAVE_RECORD_CODE_FREQ);
			if (null != freqDetail) {
				freqList = freqDetail.getItemValues();
			}

			// 根据病人组数{patient:[p1|p2|p3][p4|p5],item:[item1|item2][item3],freq:[f1][f2]
			List<String> patInfos = new ArrayList<String>();
			List<String> itemInfos = new ArrayList<String>();
			for (int i = 0; i < patIdsList.size(); i++) {
				if(StringUtils.isBlank(patIdsList.get(i))){
					continue;
				}
				patInfos.clear();
				itemInfos.clear();
				// 患者信息[p1,p2,p3]
				Collections.addAll(patInfos, patIdsList.get(i).split(
						MnisConstants.VERTICAL_LINE));
				// 项目组合[item1,item2]
				if (itemList.size() > 0) {
					Collections.addAll(itemInfos, itemList.get(i).split(
							MnisConstants.VERTICAL_LINE));
				}
				
				for (int j = 0; j < patInfos.size(); j++) {
					//randomId值
					StringBuffer randomIdBuffer = new StringBuffer();
					String[] patInfo = patInfos.get(j).split(
							MnisConstants.COMMA);
					// 主记录
					NurseWhiteBoardRecordInfo record = new NurseWhiteBoardRecordInfo();
					record.setDeptCode(inbo.getDeptCode());// 科室编号
					record.setRecordDate(DateUtil.parse(inbo.getRecordDate()));// 记录时间
					record.setRecordCode(inbo.getRecordCode());// 记录项目编号
					record.setRecordName(inbo.getRecordName());// 记录项目名称
					if(StringUtils.isBlank(inbo.getRecordNurseCode())
							|| StringUtils.isBlank(inbo.getRecordNurseName())){
						record.setRecordNurseCode(user.getCode());// 记录人编号
						record.setRecordNurseName(user.getName());// 记录人姓名
					}else{
						record.setRecordNurseCode(inbo.getRecordNurseCode());// 记录人编号
						record.setRecordNurseName(inbo.getRecordNurseName());// 记录人姓名
					}
					if(itemList.size()>0){
						randomIdBuffer.append(itemList.get(i));
					}else{
						randomIdBuffer.append(inbo.getRecordCode());
					}
					//设置频次
					if(freqList.size() > 0){
						randomIdBuffer.append(freqList.get(i));
						record.setPerformSchedule(freqList.get(i));
					}
					//设置内容
					if (contentList.size() > 0){
						randomIdBuffer.append(i);
						record.setRecordValue(contentList.get(i));
					}
					record.setRandomId(randomIdBuffer.toString());
					record.setOrderNo(orderNo);// 序号
					if(patInfo.length == 1){
						record.setPatInfo(getPatInfo(patInfo[0]));
					}else if(patInfo.length == 2){
						record.setPatId(patInfo[0]);// 患者ID
						record.setPatInfo(getPatInfo(patInfo[1]));
					}

					// 明细列表
					List<NurseWhiteBoardRecordItemInfo> details = new ArrayList<NurseWhiteBoardRecordItemInfo>();

					for (String itemCode : itemInfos) {
						if(StringUtils.isBlank(itemCode)){
							continue;
						}
						String itemName = metadataMap.get(itemCode);// 项目名称
						NurseWhiteBoardRecordItemInfo detail = new NurseWhiteBoardRecordItemInfo();
						if(patInfo.length > 0)
							detail.setItemPatId(patInfo[0]);// 患者编号
						detail.setStatus(NurseWhiteBoardConstants.WHITE_BOARD_STATUS_C);// 未完成
						detail.setRecordItemCode(itemCode);// 项目编号
						detail.setRecordItemName(itemName);// 项目名称
						//设置频次
						if(freqList.size() > 0){
							detail.setRecordItemValue(freqList.get(i));
						}
						// detail.setExecItemDate(DateUtil.parse(inbo.getRecordDate()
						// + " " + time));//执行时间
						details.add(detail);// 明细信息
					}
					record.setNurseWhiteBoardRecordItemInfos(details);// 添加明细
					recordList.add(record);// 主记录列表
					orderNo++;

				}
			}
		}
		// 数据返回
		return recordList;
	}
	
	
	/**
	 * 清空不必要原始
	 * @param nurseWhiteBoardRecordInfos
	 * @return
	 */
	public static List<NurseWhiteBoardRecordInfo> clearNurseWhiteBoardRecordItemInfos(
			List<NurseWhiteBoardRecordInfo> nurseWhiteBoardRecordInfos) {
		if(nurseWhiteBoardRecordInfos == null || nurseWhiteBoardRecordInfos.size() == 0)
			return nurseWhiteBoardRecordInfos;
		List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos = new ArrayList<NurseWhiteBoardRecordItemInfo>();
		for (NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo : nurseWhiteBoardRecordInfos) {
			nurseWhiteBoardRecordItemInfos.clear();
			for (NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo : nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos()) {
				if(nurseWhiteBoardRecordItemInfo.getRecordItemId() == 0){
					nurseWhiteBoardRecordItemInfos.add(nurseWhiteBoardRecordItemInfo);
				}
			}
			
			nurseWhiteBoardRecordInfo.getNurseWhiteBoardRecordItemInfos().removeAll(nurseWhiteBoardRecordItemInfos);
		}
		return nurseWhiteBoardRecordInfos;
	}
	
	/**
	 * 设置标准的床位格式
	 * @param patInfo
	 * @return
	 */
	private static String getPatInfo(String patInfo){
		if(StringUtils.isBlank(patInfo)){
			return patInfo;
		}
		if(!patInfo.contains("床")){
			patInfo = patInfo.replace(MnisConstants.LINE, "床"+MnisConstants.LINE);
		}
		return patInfo;
	}
}
