package com.harmazing.openbridge.mod.operations.elasticsearch.bean.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String filterpic;
	
	private List<FilterInfoVo> filterc = new ArrayList<FilterInfoVo>();
	
	private List<FilterInfoVo> filternc = new ArrayList<FilterInfoVo>();

	public String getFilterpic() {
		return filterpic;
	}

	public void setFilterpic(String filterpic) {
		this.filterpic = filterpic;
	}

	public List<FilterInfoVo> getFilterc() {
		return filterc;
	}

	public void setFilterc(List<FilterInfoVo> filterc) {
		this.filterc = filterc;
	}

	public List<FilterInfoVo> getFilternc() {
		return filternc;
	}

	public void setFilternc(List<FilterInfoVo> filternc) {
		this.filternc = filternc;
	}
	
	

}
