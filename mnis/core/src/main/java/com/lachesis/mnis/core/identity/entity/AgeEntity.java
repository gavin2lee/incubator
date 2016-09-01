package com.lachesis.mnis.core.identity.entity;

import java.util.Date;

import com.lachesis.mnis.core.util.DateUtil;


public class AgeEntity {
	public static final int AGE_UNIT_YEAR = 0;
	public static final int AGE_UNIT_MONTH = 1;
	public static final int AGE_UNIT_DAY = 2;
	private static final String[] AGE_UNIT  = new String[]{"岁","月","天"};

	private int age;	
	private int ageUnit;

	public AgeEntity(int age, int ageUnit) {
		super();
		this.age = age;
		this.ageUnit = ageUnit;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(int ageUnit) {
		this.ageUnit = ageUnit;
	}
	
	public int compareTo(int age) {
		return compareToCustom(new AgeEntity(age, AGE_UNIT_YEAR));
	}
	
	public int compareTo(int age, int ageUnit) {
		return compareToCustom(new AgeEntity(age, ageUnit));
	}

	/**
	 *  获取年龄的文字表示
	 * @return
	 */
	public String getAgeString() {
		if( age<=0 ) {
			return "";
		}
		return age+AGE_UNIT[ageUnit];
	}
	
	public int compareToCustom(AgeEntity ageEntity) {
		if( getAgeUnit() != ageEntity.getAgeUnit()) {
			return 0;
		}
		return this.getAge() - ageEntity.getAge();
	}
	
	/**
	 * 根据生日计算生日
	 * @param birthday
	 * @return
	 */
	public static AgeEntity calculateAge(Date inDate, Date birthday) {
		Date currentDate = inDate;
		if(currentDate == null){
			currentDate = new Date();
		}
		int days = DateUtil.calDatePoor(birthday, currentDate);
		if (days < DateUtil.DAY_PER_YEAR) {
			if(days >= DateUtil.DAY_PER_MONTH) {
				return new AgeEntity(days / DateUtil.DAY_PER_MONTH, AgeEntity.AGE_UNIT_MONTH);
			} else {
				return new AgeEntity(days, AgeEntity.AGE_UNIT_DAY);
			}
		} else {
			return new AgeEntity(days / DateUtil.DAY_PER_YEAR, AgeEntity.AGE_UNIT_YEAR);
		}
	}
	
}
