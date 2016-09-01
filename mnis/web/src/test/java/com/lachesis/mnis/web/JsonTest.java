package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.util.GsonUtils;

public class JsonTest {

	@Test
	public void testReNameProperty(){
		Bean bean = new Bean("1111", "2222");
		
		System.out.println(GsonUtils.toJson(bean));
		//System.out.println(JacksonUtils.toJSon(bean));
	}
	
	@Test
	public void deserializeObject(){
		UserBaseInfo user = new UserBaseInfo();
		user.setUserId("1000");
		user.setLoginName("2910");
		//user.setEmplCode("2910");
		user.setEmplName("张三"); 
		user.setCurrentDeptCode("316");
		user.setCurrentDeptName("骨二科");
		
		String s = GsonUtils.toJson(user);
		//UserBaseInfo userBaseInfoObj = JacksonUtils.readValue(s, UserBaseInfo.class);
		UserBaseInfo u = GsonUtils.fromJson(s, UserBaseInfo.class);
		Assert.assertNotNull(u);
	}
	
	@Test
	public void serializeArrayObject(){
		UserBaseInfo[] array = new UserBaseInfo[2];
		
		UserBaseInfo user = new UserBaseInfo();
		user.setUserId("1000");
		user.setLoginName("2910");
		//user.setEmplCode("2910");
		user.setEmplName("张三"); 
		user.setCurrentDeptCode("316");
		user.setCurrentDeptName("骨二科");
		array[0] = user;
		
		UserBaseInfo user1 = new UserBaseInfo();
		user1.setUserId("2000");
		user1.setLoginName("2911");
		user1.setEmplCode("2911");
		user1.setEmplName("王五一"); 
		user1.setCurrentDeptCode("316");
		user1.setCurrentDeptName("骨二科");
		array[1] = user1;
		
		String s = GsonUtils.toJson(array);
		System.out.println(s);

		//UserBaseInfo[] arr = JacksonUtils.readValue(s, UserBaseInfo[].class);
		UserBaseInfo[] arr1 = GsonUtils.fromJson(s, UserBaseInfo[].class);
		Assert.assertNotNull(arr1);
		Assert.assertEquals(arr1.length, 2);
	}
	
	
	@Test
	public void testBodySignRecord() throws Exception{
		String item = "{\"bodySignItemList\":[{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"temperature\",\"itemName\":\"体温\",\"itemUnit\":\"℃\"},\"index\":0,\"itemValue\":\"38\",\"measureNoteCode\":\"yw\",\"measureNoteName\":\"腋温\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"pulse\",\"itemName\":\"脉博\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"115\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"heartRate\",\"itemName\":\"心率\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"117\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"breath\",\"itemName\":\"呼吸\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"12\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"stool\",\"itemName\":\"大便\",\"itemUnit\":\"次\"},\"index\":0,\"itemValue\":\"1\",\"measureNoteCode\":\"zxpb\",\"measureNoteName\":\"自行排便\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"bloodPress\",\"itemName\":\"血压\",\"itemUnit\":\"mmHg\"},\"index\":0,\"itemValue\":\"110/90\",\"measureNoteCode\":\"sz\",\"measureNoteName\":\"上肢\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"otherOutput\",\"itemName\":\"出量\",\"itemUnit\":\"ml\"},\"index\":0,\"itemValue\":\"1000\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"}],\"patientId\":\"90318421\",\"patientName\":\"施春福\",\"recordDay\":\"2015-06-02\",\"recordTime\":\"16:00:01\"}";
		
	/*	// 反序列化
		ObjectMapper objectMapper = new ObjectMapper();
		// 设置日期格式
		objectMapper.setDateFormat(DateUtil.FORMAT);
		BodySignRecord bodySignRecord = objectMapper.readValue(item, BodySignRecord.class);
		Assert.assertNotNull(bodySignRecord);*/
		
		BodySignRecord bodySignRecord1 = GsonUtils.fromJson(item, BodySignRecord.class);
		Assert.assertNotNull(bodySignRecord1);
	}
	
	@Test
	public void testBodySignRecords() throws Exception{
		String item = "[{\"bodySignItemList\":[{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"temperature\",\"itemName\":\"体温\",\"itemUnit\":\"℃\"},\"index\":0,\"itemValue\":\"38\",\"measureNoteCode\":\"yw\",\"measureNoteName\":\"腋温\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"pulse\",\"itemName\":\"脉博\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"115\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"heartRate\",\"itemName\":\"心率\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"117\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"breath\",\"itemName\":\"呼吸\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"12\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"stool\",\"itemName\":\"大便\",\"itemUnit\":\"次\"},\"index\":0,\"itemValue\":\"1\",\"measureNoteCode\":\"zxpb\",\"measureNoteName\":\"自行排便\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"bloodPress\",\"itemName\":\"血压\",\"itemUnit\":\"mmHg\"},\"index\":0,\"itemValue\":\"110/90\",\"measureNoteCode\":\"sz\",\"measureNoteName\":\"上肢\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"otherOutput\",\"itemName\":\"出量\",\"itemUnit\":\"ml\"},\"index\":0,\"itemValue\":\"1000\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"}],\"patientId\":\"90318421\",\"patientName\":\"施春福\",\"recordDay\":\"2015-06-02\",\"recordTime\":\"16:00:01\"}"
			+",{\"bodySignItemList\":[{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"temperature\",\"itemName\":\"体温\",\"itemUnit\":\"℃\"},\"index\":0,\"itemValue\":\"38\",\"measureNoteCode\":\"yw\",\"measureNoteName\":\"腋温\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"pulse\",\"itemName\":\"脉博\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"115\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"heartRate\",\"itemName\":\"心率\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"117\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"breath\",\"itemName\":\"呼吸\",\"itemUnit\":\"次/分\"},\"index\":0,\"itemValue\":\"12\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"stool\",\"itemName\":\"大便\",\"itemUnit\":\"次\"},\"index\":0,\"itemValue\":\"1\",\"measureNoteCode\":\"zxpb\",\"measureNoteName\":\"自行排便\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"bloodPress\",\"itemName\":\"血压\",\"itemUnit\":\"mmHg\"},\"index\":0,\"itemValue\":\"110/90\",\"measureNoteCode\":\"sz\",\"measureNoteName\":\"上肢\"},{\"abnormalFlag\":0,\"bodySignDict\":{\"itemCode\":\"otherOutput\",\"itemName\":\"出量\",\"itemUnit\":\"ml\"},\"index\":0,\"itemValue\":\"1000\",\"measureNoteCode\":\"moren\",\"measureNoteName\":\"默认\"}],\"patientId\":\"90318421\",\"patientName\":\"施春福\",\"recordDay\":\"2015-06-02\",\"recordTime\":\"18:00:01\"}]";
		
	/*	// 反序列化
		ObjectMapper objectMapper = new ObjectMapper();
		// 设置日期格式
		objectMapper.setDateFormat(DateUtil.FORMAT);
		BodySignRecord bodySignRecord = objectMapper.readValue(item, BodySignRecord.class);
		Assert.assertNotNull(bodySignRecord);*/
		Type typeOfT = new TypeToken<List<BodySignRecord>>(){}.getType();
		List<BodySignRecord> itemList = GsonUtils.fromJson(item, typeOfT);
		Assert.assertNotNull(itemList);
	}
}


class Bean{
	
	@SerializedName("_id")
	private String id;
	//@JsonProperty("_name")
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bean(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Bean(){}
}

class UserBaseInfo {
	private String userId; 
	private String loginName; 
	private String emplCode; 
	private String emplName; 
	private String currentDeptCode; 
	private String currentDeptName;
    
	public String getUserId() {
		if(StringUtils.isBlank(this.userId)){
			return emplCode;
		}
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public String getEmplName() {
		return emplName;
	}
	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}
	public String getCurrentDeptCode() {
		return currentDeptCode;
	}
	public void setCurrentDeptCode(String currentDeptCode) {
		this.currentDeptCode = currentDeptCode;
	}
	public String getCurrentDeptName() {
		return currentDeptName;
	}
	public void setCurrentDeptName(String currentDeptName) {
		this.currentDeptName = currentDeptName;
	} 

}