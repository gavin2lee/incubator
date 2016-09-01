package com.anyi.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanPerson {
	private String name; // 姓名
	private String sex; // 性别
	private int age; // 年龄
	private String hometown;// 籍贯
	private String phone; // 电话号码

	public JavaBeanPerson() {
	}

	public JavaBeanPerson(String name, String sex, int age, String hometown,
			String phone) {
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.hometown = hometown;
		this.phone = phone;
	}

	/**
	 * 姓名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 姓名
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 性别
	 * 
	 * @return
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 性别
	 * 
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 年龄
	 * 
	 * @return
	 */
	public int getAge() {
		return age;
	}

	/**
	 * 年龄
	 * 
	 * @param age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * 籍贯
	 * 
	 * @return
	 */
	public String getHometown() {
		return hometown;
	}

	/**
	 * 籍贯
	 * 
	 * @param hometown
	 */
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	/**
	 * 电话号码
	 * 
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 电话号码
	 * 
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static List<JavaBeanPerson> getList() {
		List<JavaBeanPerson> list = new ArrayList<JavaBeanPerson>();
		list.add(new JavaBeanPerson("Lily", "female", 22, "Hubei", "10086"));
		list.add(new JavaBeanPerson("Macro", "male", 33, "Beijing",
				"13800000000"));
		list.add(new JavaBeanPerson("Andy", "male", 44, "HongKong",
				"13812345678"));
		list.add(new JavaBeanPerson("Linder", "female", 28, "Guangxi",
				"18677778888"));
		list.add(new JavaBeanPerson("中国人11111111111111111111111111111111", "female", 26, "Gansu",
				"18219177720"));
		return list;
	}
	
	public static ArrayList<Map<String, ?>> getList2() {
		ArrayList<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		for (int i = 0; i < 100; i++) {
			Map<String, Object> p2 = new HashMap<String, Object>();
			p2.put("name", "测试人员" + i);
			p2.put("sex", "女");
			p2.put("age", 20);
			p2.put("hometown", "福建省");
			p2.put("phone", "sdfasdfasd");
			p2.put("name2", "123");
			list.add(p2);
			
			if(i==10){
				Map<String, Object> p3 = new HashMap<String, Object>();
				p3.put("hometown", "福建省");
				list.add(p3);
			}
		}
		return list;
	}
}
