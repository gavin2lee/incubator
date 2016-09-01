package com.lachesis.mnis.core.bodysign;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.lachesis.mnis.core.bodysign.BodySignConstants.BodySignIndexStrategy;
import com.lachesis.mnis.core.bodysign.BodySignConstants.BodySignTimePointStrategy;

public class BodySignStrategyTest {

	@Test
	public void testZERO() {
		Date time = new Date();
		List<Date> list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));
	}

	@Test
	public void testFIRST() {
		Date time = new Date();
		List<Date> list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

	}

	@Test
	public void testSECOND() {
		Date time = new Date();
		List<Date> list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

	}

	@Test
	public void testTHREE() {
		Date time = new Date();
		List<Date> list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

		list = BodySignUtil.getTimePointsByHour(time,
				BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY);
		System.out.println(list);
		System.out.println(BodySignUtil.getIndexByDivideHour(time, list, 6));

	}
	
	@Test
	public void testBoolean(){
		boolean[] three = new boolean[3];
		System.out.println(three);
	}
}
