package com.lachesis.mnis.core.patient;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.patient.entity.Patient;

public class PatientServiceImplTest extends SpringTest {
	
	@Autowired 
	private PatientService patientService;
	
	@Test
	public void testGetPatientByPatId(){
		Patient p = patientService.getPatientByPatId("YB0155213_2");
		Assert.assertNotNull(p);
	}
	
	@Test
	public void  testGetPatientByDeptCodeAndBedCode(){
		Assert.assertNotNull(patientService.getPatientByDeptCodeAndBedCode("H250005","505"));
	}

	@Test
	public void  testGetWardPatientList(){
		List<Patient> list = patientService.getWardPatientList(0, "1478");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 3);
	}
	
	@Test
	public void  testGetWardPatientList1(){
		List<Patient> list = patientService.getWardPatientList(2, "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 5);
	}
	
	@Test
	public void  testGetWardBedList(){
		Assert.assertNotNull(patientService.getWardBedList("H250005"));
	}
	
	@Test
	public void  testGetAttention(){
		List<String> list = patientService.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 3);
	}

	@Test
	public void  testAddAttention(){
		patientService.addAttention("1478", "H250005", "601");
		List<String> list = patientService.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 4);
	}
	
	@Test
	public void  testAddAttention1(){
		patientService.addAttention("1478", "H250005", "511");
		List<String> list = patientService.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 3);
	}
	
	@Test
	public void  testDelAttention(){
		patientService.delAttention("1478", "H250005", "511");
		List<String> list = patientService.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 2);
	}
	
	@Test
	public void  testgetPatientByDeptCodeOrUserCode(){
		patientService.getPatientByDeptCodeOrUserCode("1478", "H250005");
	}

	@Test
	public void  testgetPatientByDeptCodeOrUserCode1(){
		patientService.getPatientByDeptCodeOrUserCode( "", "H250005");
	}
}
