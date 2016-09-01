package com.lachesis.mnis.core.barcode.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.barcode.BarcodeServiceImpl;
import com.lachesis.mnis.core.barcode.InpatientWithOrders;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderGroup;
import com.lachesis.mnis.core.patient.PatientServiceImpl;
import com.lachesis.mnis.core.patient.entity.Patient;

public class BarcodeServiceImplTest extends SpringTest {
	@Autowired
	BarcodeServiceImpl barcodeService;
	@Autowired
	PatientServiceImpl patientService;

	private static Patient inpatientInfo;
//	@Autowired OrderService orderService;
	
	@BeforeClass
	public static void init() {
		inpatientInfo = new Patient();
		inpatientInfo.setDeptCode("1005");
		inpatientInfo.setName("JUnit");
	}
	
	
	//@Test(expected=IllegalArgumentException.class)
	public void testGetInfoByNullBarcode() {
		barcodeService.getPatientInfoByBarcode(null);
		barcodeService.getPatientInfoByBarcode("");		
		barcodeService.getOrderGroupByBarcode(null, null);
		barcodeService.getOrderGroupByBarcode("", null);
	}
	
	@Test
	public void testGetPatientInfoByBarcode() {
		// 条码以字母开头
		assertNull( barcodeService.getPatientInfoByBarcode("1") ); 

		// 有数据时返回非空列表
		//String patId = patientService.addPatientBase( patientBaseInfo );
		assertNull( barcodeService.getPatientInfoByBarcode("ZY010000060257") ); 
		// patient should have been admitted into hospital
		//String inhospId = patientService.addInpatient( inpatientInfo );
		//assertNotNull( barcodeService.getPatientInfoByBarcode("P"+ patId) ); 
//		barcodeService.getPatientInfoByBarcode("122");
	}

	//@Test
	public void testGetOrderGroupByBarcode() {

		assertNull( barcodeService.getOrderGroupByBarcode("23", null) );	// 条码以字母开头
	}

	//@Test
	public void testMatchPatientAndOrder() {
		InpatientWithOrders iw = new InpatientWithOrders();
		iw.setInpatientInfo( new Patient() ); 
		iw.getInpatientInfo().setPatId("1");
		
		OrderExecGroup og = new OrderExecGroup();
		og.setOrderGroup( new OrderGroup() );
		og.getOrderGroup().setOrderGroupNo( "122" );
		og.getOrderGroup().setPatientId( iw.getInpatientInfo().getPatId() );
		assertTrue( barcodeService.matchPatientAndOrder(iw, og) ); 		
	}

}
