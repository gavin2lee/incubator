package com.lachesis.mnis.core.identity.entity;


/***
 * 
 * 医院科室、病区信息表
 *
 * @author yuliang.xu
 * @date 2015年6月9日 下午3:01:59 
 *
 */
public class SysDept {

	private int id;
	
    private String code;

    private String name;

    private String searchCode;

    private boolean isDept;

    private boolean flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public boolean isDept() {
		return isDept;
	}

	public void setDept(boolean isDept) {
		this.isDept = isDept;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}