package com.lachesis.mnis.core.patient;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.reposity.PatientRepository;

public class PatientRepositoryTest extends SpringTest {
	
	@Autowired
	private PatientRepository patientRepository;

	@Test
	public void testInit() {
		Assert.assertNotNull(patientRepository);
	}

	@Test
	public void testGetPatientByPatId(){
		Patient p = patientRepository.getPatientByPatId("YB0155213_2");
		Assert.assertNotNull(p);
	}
	
	@Test
	public void  testGetPatientByDeptCodeAndBedCode(){
		Assert.assertNotNull(patientRepository.getPatientByDeptCodeAndBedCode("H250005","505"));
	}

	@Test
	public void  testGetWardPatientList(){
		List<Patient> list = patientRepository.getWardPatientList(0, "1478");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 3);
	}
	
	@Test
	public void  testGetWardPatientList1(){
		List<Patient> list = patientRepository.getWardPatientList(2, "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 5);
	}
	
	@Test
	public void  testGetWardBedList(){
		Assert.assertNotNull(patientRepository.getWardBedList("H250005"));
	}
	
	@Test
	public void  testGetAttention(){
		List<String> list = patientRepository.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 3);
	}

	@Test
	public void  testAddAttention(){
		patientRepository.addAttention("1478", "H250005", "601");
		List<String> list = patientRepository.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 4);
	}
	
	@Test
	public void  testDelAttention(){
		patientRepository.delAttention("1478", "H250005", "511");
		List<String> list = patientRepository.getAttention("1478", "H250005");
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 2);
	}
}
