package com.lachesis.mnis.core.order.entity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 医嘱公共信息
 * @author ThinkPad
 *
 */
public class OrderExecDocOrderInfo  implements Comparator<OrderExecDocOrderInfo>{
	private String id;
	private String patId;
	/**
	 * 医嘱用法
	 */
	@SerializedName("usgCod")
	private String orderUsageCode;
	@SerializedName("usgNm")
	private String orderUsageName;
	@SerializedName("ordExecCod")
	private String orderExecTypeCode;
	@SerializedName("ordExecNm")
	private String orderExecTypeName;
	/**
	 * 医嘱类型(长嘱,临嘱)
	 */
	@SerializedName("ordType")
	private String orderType;
	@SerializedName("ordExecDate")
	private String orderExecDate;
	
	/**
	 * 是否打印
	 */
	@SerializedName("isPrt")
	private boolean isPrinted;
	/**
	 *执行单打印属性
	 */
	@SerializedName("ordPrtInf")
	private OrderExecDocumentPrintInfo orderExecDocumentPrintInfo;
	/**
	 * 执行单详细信息
	 */
	@SerializedName("ordDetailInfs")
	private List<OrderExecDocumentDetailInfo> orderExecDocumentDetailInfos;
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getOrderUsageCode() {
		return orderUsageCode;
	}
	public void setOrderUsageCode(String orderUsageCode) {
		this.orderUsageCode = orderUsageCode;
	}
	public String getOrderUsageName() {
		return orderUsageName;
	}
	public void setOrderUsageName(String orderUsageName) {
		this.orderUsageName = orderUsageName;
	}
	public String getOrderExecTypeCode() {
		return orderExecTypeCode;
	}
	public void setOrderExecTypeCode(String orderExecTypeCode) {
		this.orderExecTypeCode = orderExecTypeCode;
	}
	public String getOrderExecTypeName() {
		return orderExecTypeName;
	}
	public void setOrderExecTypeName(String orderExecTypeName) {
		this.orderExecTypeName = orderExecTypeName;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public boolean isPrinted() {
		return isPrinted;
	}
	public void setPrinted(boolean isPrinted) {
		this.isPrinted = isPrinted;
	}
	public OrderExecDocumentPrintInfo getOrderExecDocumentPrintInfo() {
		return orderExecDocumentPrintInfo;
	}
	public void setOrderExecDocumentPrintInfo(
			OrderExecDocumentPrintInfo orderExecDocumentPrintInfo) {
		this.orderExecDocumentPrintInfo = orderExecDocumentPrintInfo;
	}
	public List<OrderExecDocumentDetailInfo> getOrderExecDocumentDetailInfos() {
		return orderExecDocumentDetailInfos;
	}
	public void setOrderExecDocumentDetailInfos(
			List<OrderExecDocumentDetailInfo> orderExecDocumentDetailInfos) {
		this.orderExecDocumentDetailInfos = orderExecDocumentDetailInfos;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOrderExecDate() {
		return orderExecDate;
	}
	public void setOrderExecDate(String orderExecDate) {
		this.orderExecDate = orderExecDate;
	}
	@Override
	public int compare(OrderExecDocOrderInfo o1, OrderExecDocOrderInfo o2) {
		if(null == o1
				||null == o2
				|| StringUtils.isBlank(o1.getOrderType())
				|| StringUtils.isBlank(o2.getOrderType())
				||o1.getOrderType().compareTo(o2.getOrderType())>= 0){
			return 0;
		}
		return 1;
	}
}
