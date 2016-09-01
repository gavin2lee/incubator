package com.lachesis.mnis.core.patient.entity;


/**
 * 医院床位信息表
 *
 * @author yuliang.xu
 * @date 2015年6月11日 上午9:59:09
 */
public class Bed {

	/** 床位自增长号 */
	private int id;
	/** 床位代码 */
	private String code;
	/** COM_WARD(病区代码) */
	private String wardCode;
	/** 床位类型 */
	private String bedType;
	/** 床位类型名称 */
	private String bedTypeName;
	/** 床位价格 */
	private double bedPrice;
	/** 附加说明 */
	private String tags;
	/** 标记(0:无效,1:有效) */
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

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public String getBedTypeName() {
		return bedTypeName;
	}

	public void setBedTypeName(String bedTypeName) {
		this.bedTypeName = bedTypeName;
	}

	public double getBedPrice() {
		return bedPrice;
	}

	public void setBedPrice(double bedPrice) {
		this.bedPrice = bedPrice;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
