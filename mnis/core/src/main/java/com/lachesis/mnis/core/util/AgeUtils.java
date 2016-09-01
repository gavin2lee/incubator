package com.lachesis.mnis.core.util;

import java.util.Calendar;
import java.util.Date;

public class AgeUtils {
	private AgeUtils() {}
	
	public static int getAge(Date birthDate) {
		return getAge(birthDate, new Date());
	}

	public static int getAge(Date birthDate, Date toDate) {
		if (birthDate.after(toDate)) {
			throw new IllegalArgumentException("the parameter birthDate["
					+ birthDate + "] can not be later than toDate[" + toDate
					+ "]");
		}
		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		cal.setTime(birthDate);
		now.setTime(toDate);
		int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
				|| (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal
						.get(Calendar.DAY_OF_MONTH) > now
						.get(Calendar.DAY_OF_MONTH))) {
			res--;
		}
		return res;
	}
}
