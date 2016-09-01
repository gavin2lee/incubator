package com.lachesis.mnis.core.barcode;

import java.util.List;

import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.patient.entity.Patient;

public class InpatientWithOrders {
	private Patient inpatientInfo;
	private List<OrderExecGroup> pendingOrders;
	private List<OrderExecGroup> orderInExecution;

	public String getPatientId() {
		return getInpatientInfo().getPatId();
	}

	public Patient getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(Patient inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	public List<OrderExecGroup> getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(List<OrderExecGroup> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public List<OrderExecGroup> getOrderInExecution() {
		return orderInExecution;
	}

	public void setOrderInExecution(List<OrderExecGroup> orderInExecution) {
		this.orderInExecution = orderInExecution;
	}
}
