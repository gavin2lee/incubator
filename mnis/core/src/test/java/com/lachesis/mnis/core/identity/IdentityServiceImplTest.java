package com.lachesis.mnis.core.identity;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IdentityServiceImplTest extends SpringTest {

	@Autowired 
	IdentityService identityService;
	
	@Test
	public void testQueryUserByCode() {
		UserInformation user = identityService.queryUserByCode("1478");
		Assert.assertEquals("1478", user.getUser().getCode());
	}
	
	@Test
	public void testVerifyUser() {
		identityService.verifyUser("000016", "123",null,false);
	}

	@Test
	public void testLoginWithFingerPrint() {
		identityService.verifyUser("LH", "123",null,false);
	}
    
	@Test
	public void testQueryConfig(){
		Map<String, Map<String, String>> map = identityService.queryConfig("1478", "H250005");
		Assert.assertEquals(map.size(), 3);
	}
	
	@Test
	public void testQueryDeptments(){
		List<SysDept> list = identityService.queryDeptments();
		Assert.assertTrue(list.size() > 0);
	}
	
	@Test
	public void testQueryFingerByDeptCodeAndDate(){
		 identityService.queryFingerByDeptCodeAndDate("H250005", null);
	}
	
	@Test
	public void testSaveUserFinger(){
		int result = identityService.saveUserFinger("1478", "H250005", "xxxxx", "");
		System.out.println(result);
		Assert.assertTrue(result > 0);
	}
	
	@Test
	public void testGetHospitalName(){
		System.out.println(identityService.getHospitalName());
	}

	@Test
	public void testGetTempratureTimeIntervalOfSpecifiedTime(){
		String date = "2016-01-06";
		String time = "00:30";
		String[] timeInterval = identityService.getTempratureTimeIntervalOfSpecifiedTime(date, time);
		System.out.println(Arrays.asList(timeInterval));
		Assert.assertNotNull(timeInterval);
	}

	@Test
	public void testGetTemperatureInputTimeByTimeInterval(){
		String[] timeInterval = new String[4];
		timeInterval[0] = "2016-01-06";
		timeInterval[1] = "21:00";
		timeInterval[2] = "2016-01-06";
		timeInterval[3] = "21:00";
		String[] inputTime = identityService.getTemperatureInputTimeByTimeInterval(timeInterval);
		System.out.println(Arrays.asList(inputTime));
		Assert.assertNotNull(inputTime);
	}
}
