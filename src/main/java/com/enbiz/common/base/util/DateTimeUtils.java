package com.enbiz.common.base.util;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateTimeUtils {

	public static final int diffDays(String from, String to) throws ParseException {
		Date fromDate = DateUtils.parseDate(from, "yyyyMMdd");
		Date toDate = DateUtils.parseDate(to, "yyyyMMdd");

		return (int) ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
	}

}
