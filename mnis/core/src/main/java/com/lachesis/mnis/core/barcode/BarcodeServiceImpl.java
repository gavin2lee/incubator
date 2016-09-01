package com.lachesis.mnis.core.barcode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.BarcodeUtil;
import com.lachesis.mnis.core.util.BarcodeUtil.BarcodeType;
import com.lachesis.mnis.core.util.StringUtil;

@Service("barcodeService")
public class BarcodeServiceImpl implements BarcodeService {
	@Autowired
	private PatientService patientService;
	@Autowired
	private OrderService orderService;

	public static final String PATIENT_BARCODE_PREFIX = "P";
	public static final String INFUSION_BARCODE_PREFIX = "I";
	public static final String ORALDRUG_BARCODE_PREFIX = "O";
	public static final String BED_NO_PREFIX = "B";

	@Override
	public InpatientWithOrders getPatientInfoByBarcode(String barcode) {
		if (barcode == null || barcode.isEmpty()) {
			throw new IllegalArgumentException("条码号不能为空");
		}

		// 条码不属于病人条码或病床条码
		if (!validateBarcode(barcode, BarcodeType.PATIENT_ID)
				&& !validateBarcode(barcode, BarcodeType.PATIENT_BEDCODE)) {
			return null;
		}

		String patId = decode(barcode);
		if (patId == null) {
			return null;
		}
		
		Patient inpatientInfo = patientService.getPatientByPatId(patId);
		if (inpatientInfo == null) {
			return null;
		}

		InpatientWithOrders inpatientWithOrders = new InpatientWithOrders();
		inpatientWithOrders.setInpatientInfo(inpatientInfo);
		if (StringUtil.isANumber(patId)) {
			// 待执行医嘱
			inpatientWithOrders.setPendingOrders(orderService.getPendingOrderGroups(patId, null,null,null,null));

			// 执行中医嘱（可能为多组）
//			List<OrderExecGroup> orderGroupsInExecution = orderService.getOngoingOrderGroups(patId, null);
			List<OrderExecGroup> orderGroupsInExecution = new ArrayList<>();
			if (orderGroupsInExecution != null && orderGroupsInExecution.size() > 0) {
				inpatientWithOrders.setOrderInExecution(orderGroupsInExecution);
			}
		}
		return inpatientWithOrders;
	}

	@Override
	public OrderExecGroup getOrderGroupByBarcode(String barcode, String patientId) {
		if (barcode == null || barcode.isEmpty()) {
			throw new IllegalArgumentException("条码号不能为空");
		}
		if (!validateBarcode(barcode)) {
			return null;
		}
		return orderService.getOrderGroupByExecId(barcode,patientId);
	}

	@Override
	public OrderExecGroup matchPatientAndOrderBarcodes(String pBarcode,
			String oBarcode) {
		InpatientWithOrders inpatientWithOrders = getPatientInfoByBarcode(pBarcode);
		OrderExecGroup orderGroup = getOrderGroupByBarcode(oBarcode, inpatientWithOrders.getInpatientInfo().getPatId());
		if (matchPatientAndOrder(inpatientWithOrders, orderGroup)) {
			return orderGroup;
		} else {
			return null;
		}
	}

	public boolean matchPatientAndOrder(
			InpatientWithOrders inpatientWithOrders, OrderExecGroup orderGroup) {
		if (inpatientWithOrders == null || orderGroup == null) {
			return false;
		}
		return inpatientWithOrders.getPatientId()
				.equalsIgnoreCase(orderGroup.getPatientId());
	}

	public String decode(String barcode) {
		if (barcode == null || barcode.length() == 0) {
			return null;
		} else if (Character.isAlphabetic(barcode.charAt(0))) {
			return barcode.substring(1);
		}
		return barcode;
	}

	/**
	 * 验证条码是否是有效条码的某一种
	 * 
	 * @param barcode
	 * @return
	 */
	private boolean validateBarcode(String barcode) {
		/*if (validateBarcode(barcode, BarcodeType.DRUG_INFUSION)
				|| validateBarcode(barcode, BarcodeType.LAB_TEST)
				|| validateBarcode(barcode, BarcodeType.DRUG_ORAL)) {
			return true;
		}
		return false;*/
		return true;
	}

	private boolean validateBarcode(String barcode, BarcodeType barcodeType) {
		return barcode.substring(0, 1).equalsIgnoreCase(
				BarcodeUtil.barcodeTypes()[barcodeType.ordinal()]);
	}

}
