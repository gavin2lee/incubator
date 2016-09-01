package com.lachesis.mnis.core.barcode;

import com.lachesis.mnis.core.order.entity.OrderExecGroup;


public interface BarcodeService {
	InpatientWithOrders getPatientInfoByBarcode(String barcode);
	
	OrderExecGroup getOrderGroupByBarcode(String barcode, String patientId);
	
	/**
	 * 匹配病人条码和医嘱条码，同时返回匹配的医嘱条码对应的医嘱信息。
	 * 如果不匹配，返回null
	 * @param pBarcode
	 * @param oBarcode
	 * @return 匹配则返回匹配的医嘱信息，不匹配则返回null
	 */
	OrderExecGroup matchPatientAndOrderBarcodes(String pBarcode, String oBarcode);

}
