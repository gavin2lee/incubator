package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.List;

/**
 * 护理小白板数据录入传入对象
 *
 */
public class NurseWhiteBoardInBo {
	
	/**
	 * 科室编号
	 */
	private String deptCode;
	/**
	 * 记录项目编号
	 */
	private String recordCode;
	/**
	 * 记录项目名称
	 */
	private String recordName;
	/**
	 * 创建记录用户编号
	 */
	private String recordNurseCode;
	/**
	 * 创建人名称
	 */
	private String recordNurseName;
	//记录日期
	private String recordDate;
	//子项纪录
	private List<NurseWhiteBoardItemInBo> itemList;

	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordNurseCode() {
		return recordNurseCode;
	}

	public void setRecordNurseCode(String recordNurseCode) {
		this.recordNurseCode = recordNurseCode;
	}

	public String getRecordNurseName() {
		return recordNurseName;
	}

	public void setRecordNurseName(String recordNurseName) {
		this.recordNurseName = recordNurseName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public List<NurseWhiteBoardItemInBo> getItemList() {
		return itemList;
	}

	public void setItemList(List<NurseWhiteBoardItemInBo> itemList) {
		this.itemList = itemList;
	}
	
	/**
	 * 根据指定code获取明细item数据
	 * @param itemCode
	 * @return
	 */
	public NurseWhiteBoardItemInBo getSubItem(String itemCode){
		NurseWhiteBoardItemInBo rs = null;
		if(null != itemList){
			for (NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo : itemList) {
				if(itemCode.equals(nurseWhiteBoardItemInBo.getItemCode())){
					rs = nurseWhiteBoardItemInBo;
					break;
				}
			}
		}
		return rs;
	}
	
	/**
	 * 获取所有项目的编号
	 * @return
	 *//*
	public List<String> getItemCodes(){
		List<String> rs = new ArrayList<String>();
		if(null != itemList){
			for(int i=0;i<itemList.size();i++){
				NurseWhiteBoardItemInBo detail = itemList.get(i);
				if(!"patient".equals(detail.getItemCode()) && 
						!"time".equals(detail.getItemCode())
						&& !StringUtils.isEmpty(detail.getItemValueCode())){
					String[] codes = detail.getItemValueCode().split("\\|");
					for(String code : codes){
						rs.add(code);
					}
				}
			}
		}
		return rs;
	}
	
	*//**
	 * 获取所有项目的编号
	 * @return
	 *//*
	public List<String> getItemNames(){
		List<String> rs = new ArrayList<String>();
		if(null != itemList){
			for(int i=0;i<itemList.size();i++){
				NurseWhiteBoardItemInBo detail = itemList.get(i);
				if(!"patient".equals(detail.getItemCode()) && 
						!"time".equals(detail.getItemCode())
						&& !StringUtils.isEmpty(detail.getItemValue())){
					String[] codes = detail.getItemValue().split("\\|");
					for(String code : codes){
						rs.add(code);
					}
				}
			}
		}
		return rs;
	}*/
}
