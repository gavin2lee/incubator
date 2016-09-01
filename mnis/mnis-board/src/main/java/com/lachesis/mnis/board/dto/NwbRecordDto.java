package com.lachesis.mnis.board.dto;

import java.util.List;

/**
 * 记录主传输信息
 * @author ThinkPad
 *
 */
public class NwbRecordDto {
	
	/**
	 * 默认his对应的code  -->小白板code
	 */
	private String code;
	private String freq;
	private String name;
	private String deptCode;
	private List<NwbRecordItemDto> nwbRecordItemDtos;
	
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<NwbRecordItemDto> getNwbRecordItemDtos() {
		return nwbRecordItemDtos;
	}
	public void setNwbRecordItemDtos(List<NwbRecordItemDto> nwbRecordItemDtos) {
		this.nwbRecordItemDtos = nwbRecordItemDtos;
	}
}
