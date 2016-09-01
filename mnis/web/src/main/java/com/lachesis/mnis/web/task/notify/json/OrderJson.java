package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.order.entity.OrderGroup;
import com.lachesis.mnis.core.patient.entity.Patient;

public class OrderJson extends BaseJson{

	private String orderTypeCode;
	private String orderStatusCode;
	private boolean isEmergent;
	
	public OrderJson(){}
	
	public OrderJson(String bedCode, String patientName, String patientId,  String orderTypeCode, String orderStatusCode, boolean isEmergent) {
		super(bedCode, patientName, patientId);
		this.orderTypeCode = orderTypeCode;
		this.orderStatusCode = orderStatusCode;
		this.isEmergent = isEmergent;
	}

	public static String createJson(OrderGroup orderBaseGroup, Patient patient) {
		return new OrderJson(
				patient.getBedCode(),
				patient.getName(),
				patient.getPatId(), 
				orderBaseGroup.getOrderTypeCode(),
				orderBaseGroup.getOrderStatusCode(),
				orderBaseGroup.isEmergent()).toJson();
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public String getOrderStatusCode() {
		return orderStatusCode;
	}

	public boolean isEmergent() {
		return isEmergent;
	}
}
