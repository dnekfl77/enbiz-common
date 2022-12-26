package com.x2bee.common.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 날짜표현 및 계산과 관련된 유틸리티성 기능을 제공한다.
 * </pre>
 * @author M.Y.Heo
 * @version 1.0
 */
public final class DateTimeUtil {
	/*
	 * 2015-10-26 추가
	 * */
	static SimpleDateFormat korDFTF = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
	static SimpleDateFormat dashDFTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat noneDFTF = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 기본 날짜시간 포맷 : yyyyMMddHHmmss
	 */
	public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 기본 날짜 포맷 : yyyyMMdd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

	/**
	 * 그리니치 표준시 time zone : GMT+9
	 */
	public static final String DEFAULT_TIMEZONE = "GMT+9";

	/**
	 * 이 클래스에 대해 인스턴스를 만들지 못 하도록 하는 private 생성자
	 */
	private DateTimeUtil() {
	}

	private static final GregorianCalendar calendar = new GregorianCalendar();

	public static final GregorianCalendar getGregorianCalendar() {
		return (GregorianCalendar) calendar.clone();
	}
	
	public static final GregorianCalendar getGregorianCalendar(int year, int month, int day) {
		return (GregorianCalendar) calendar.clone();
	}

	public static final GregorianCalendar getCurrentGregorianCalendar() {
		GregorianCalendar gc = (GregorianCalendar) calendar.clone();
		gc.setTime(new Date());
		return gc;
	}

	/**
	 * <pre>
	 * java.util.Date 를 java.util.Calendar로 변환.
	 * 
	 * @param date java.util.Date
	 * @return java.util.Calendar
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getCalendar(new Date())
	 * </pre>
	 */
	public static final Calendar getCalendar(Date date) {
		// Calendar calendar = new GregorianCalendar();
		GregorianCalendar calendar = DateTimeUtil.getGregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * <pre>
	 * DEFAULT_TIMESTAMP_FORMAT(yyyyMMddHHmmss) 형태의 날짜 표현 문자열을
	 * java.util.Calendar로 변환한다.
	 * 
	 * @param dateTimeString yyyyMMddHHmmss 형태의 문자열
	 * @return Calendar
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Calendar calendar = DateTimeUtil.getCalendar(&quot;20070711002020&quot;)
	 * </pre>
	 */
	public static final Calendar getCalendar(String dateTimeString) throws java.text.ParseException {
		return getCalendar(dateTimeString, DEFAULT_TIMESTAMP_FORMAT);
	}

	/**
	 * <pre>
	 * DEFAULT_TIMESTAMP_FORMAT(yyyyMMddHHmmss) 형태의 날짜 표현 문자열을
	 * java.util.Calendar로 변환한다.
	 * dateTimeString yyyyMMddHHmmss 형태의 문자열
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Calendar calendar = DateTimeUtil.getCalendar(&quot;20070711002020&quot;)
	 * 리턴타입 : String
	 * @param format java.lang.String
	 * @return String
	 * </pre>
	 */
	public static String getCurrentDate(String format){
		return new SimpleDateFormat(format, Locale.getDefault()).format(System.currentTimeMillis());
	}

	/**
	 * <pre>
	 * yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열과
	 * 날짜 표현 포맷(format)을 입력 조건으로
	 * java.util.Calendar를 반환한다.
	 * 
	 * @param dateTimeString yyyyMMddHHmmss 형태의 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return java.util.Calendar
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Calendar calendar = DateTimeUtil.getCalendar(&quot;20070711&quot;, &quot;yyyyMMdd&quot;)
	 * </pre>
	 */
	public static final Calendar getCalendar(String dateTimeString, String format) throws java.text.ParseException {
		// Calendar calendar = new GregorianCalendar();
		GregorianCalendar calendar = DateTimeUtil.getGregorianCalendar();
		calendar.setTime(getDate(dateTimeString, format));
		return calendar;
	}

	/**
	 * <pre>
	 * DEFAULT_TIMESTAMP_FORMAT(yyyyMMddHHmmss) 형태의 날짜 표현 문자열을
	 * java.util.Date로 변환한다.
	 * 
	 * @param dateTimeString yyyyMMddHHmmss 형태의 문자열
	 * @return java.util.Date
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Date date = DateTimeUtil.getDate(&quot;20070711002020&quot;)
	 * </pre>
	 */
	public static final java.util.Date getDate(String dateTimeString) throws java.text.ParseException {
		return getDate(dateTimeString, DEFAULT_TIMESTAMP_FORMAT);
	}

	/**
	 * <pre>
	 * yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열과
	 * 날짜 표현 포맷(format)을 입력 조건으로
	 * Date를 반환한다.
	 * 
	 * @param dateTimeString yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return java.util.Date
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Date date = DateTimeUtil.getDate(&quot;20070711&quot;, &quot;yyyyMMdd&quot;)
	 * </pre>
	 */
	public static final java.util.Date getDate(String dateTimeString, String format) throws java.text.ParseException {
		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat(format);
		// return getDate(dateTimeString, formatter);
		return DateTimeUtil.parse(dateTimeString, format);
	}

	/**
	 * <pre>
	 * yyyyMMdd 형태의 날짜 표현 문자열과
	 * 날짜 표현 포맷(format)을 입력 조건으로
	 * Date를 반환한다.
	 * 
	 * @param dateTimeInt yyyyMMdd 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return java.util.Date
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Date date = DateTimeUtil.getDate(20070711, &quot;yyyyMMdd&quot;)
	 * </pre>
	 */
	public static final java.util.Date getDate(int dateTimeInt, String format) throws java.text.ParseException {
		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat(format);
		// return getDate(dateTimeString, formatter);
		return DateTimeUtil.parse(dateTimeInt, format);
	}

	/**
	 * <pre>
	 * yyyyMMdd 형태의 날짜 표현 문자열과 날짜 표현 포맷(format)을 입력 조건으로
	 * 변환된 날짜를 문자열로 반환한다.
	 * 
	 * @param sDate yyyyMMdd 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy-MM-dd&quot;.
	 * @return String
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String sDate = DateTimeUtil.getDateFormat(&quot;20070711&quot;, &quot;yyyy-MM-dd&quot;)
	 * </pre>
	 */
	public static final String getDateFormat(String sDate, String format) throws ParseException {
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 * SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		 * 
		 * Date dt = sdf.parse(sDate); return sdFormat.format(dt);
		 */
		Date date = DateTimeUtil.parse(sDate, DEFAULT_DATE_FORMAT);
		return DateTimeUtil.format(date, format);
	}

	/**
	 * <pre>
	 * 지정된형태의 날짜표현 문자열과 변환될 날짜표현 포맷(format)을 입력 조건으로
	 * 변환된 날짜를 문자열로 반환한다.
	 * 
	 * @param beforeFormat 변환 전 날짜포멧
	 * @param sDate 날짜
	 * @param afterFormat  변환 후 날짜포맷
	 * @return String
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String sDate = DateTimeUtil.getDateFormat(&quot;yyyyMMdd&quot;, &quot;20070711&quot;, &quot;yyyy-MM-dd&quot;)
	 * </pre>
	 */
	public static final String getDateFormat(String beforeFormat, String sDate, String afterFormat)
			throws ParseException {
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat(beforeFormat);
		 * SimpleDateFormat sdFormat = new SimpleDateFormat(afterFormat);
		 * 
		 * Date dt = sdf.parse(sDate); return sdFormat.format(dt);
		 */

		Date date = DateTimeUtil.parse(sDate, beforeFormat);
		return DateTimeUtil.format(date, afterFormat);
	}

	/**
	 * <pre>
	 * yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열과
	 * SimpleDateFormat(formatter)을 입력 조건으로
	 * Date를 반환한다.
	 * 
	 * @param dateTimeString yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열
	 * @param formatter SimpleDateFormat
	 * @return java.util.Date
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * Date date = DateTimeUtil.getDate(&quot;20070711&quot;, new SimpleDateFormat(&quot;yyyyMMdd&quot;))
	 * </pre>
	 */
	public static final java.util.Date getDate(String dateTimeString, SimpleDateFormat formatter)
			throws java.text.ParseException {

		if (dateTimeString == null) {
			throw new java.text.ParseException("date string to getDate is null", 0);
		}
		if (formatter == null) {
			throw new java.text.ParseException("formatter to getDate date is null", 0);
		}
		/*
		 * java.util.Date date = null; try { date =
		 * formatter.parse(dateTimeString); } catch (java.text.ParseException e) {
		 * throw new java.text.ParseException(" wrong date:\"" + dateTimeString +
		 * "\" with format \"" + formatter.toPattern() + "\"", 0); }
		 * 
		 * if (!formatter.format(date).equals(dateTimeString)) { throw new
		 * java.text.ParseException("Out of bound date:\"" + dateTimeString +
		 * "\" with format \"" + formatter.toPattern() + "\"", 0); }
		 */

		return DateTimeUtil.parse(dateTimeString, formatter.toPattern());
	}

	/**
	 * <pre>
	 * yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열과
	 * 날짜 표현 포맷(format), 그리고 timeZoneId를 입력 조건으로
	 * long 형의 시간을 반환한다.
	 * 
	 * @param dateTimeString yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;&quot;.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return long 1970/1/1 기준의 long형 시간값(milliseconds)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * long time = DateTimeUtil.getLong(&quot;20070711&quot;, &quot;yyyyMMdd&quot;, &quot;GMT+9&quot;)
	 * </pre>
	 */
	public static final long getLong(String dateTimeString, String format, String timeZoneId)
			throws java.text.ParseException {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return getDate(dateTimeString, formatter).getTime();
	}

	/**
	 * <pre>
	 * yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열과
	 * 날짜 표현 포맷(format)을 입력 조건으로
	 * long 형의 시간을 반환한다.
	 * 
	 * @param dateTimeString yyyyMMdd 또는 yyyyMMddHHmmss 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return long 1970/1/1 기준의 long형 시간값(milliseconds)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * long time = DateTimeUtil.getLong(&quot;20070711&quot;, &quot;yyyyMMdd&quot;)
	 * </pre>
	 */
	public static final long getLong(String dateTimeString, String format) throws java.text.ParseException {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		formatter.setTimeZone(java.util.TimeZone.getDefault());
		return getDate(dateTimeString, formatter).getTime();
	}

	/**
	 * <pre>
	 * 주어진 날짜 표현 문자열에 대해 yyyyMMdd 형태에 부합하며 유효한지 검사한다.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString yyyyMMdd 형태의 날짜 표현 문자열
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *                 false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * boolean check = DateTimeUtil.isValid(&quot;20070711&quot;)
	 * </pre>
	 */
	public static final boolean isValid(String dateString) {
		return isValid(dateString, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <pre>
	 * 주어진 날짜 표현 문자열에 대해 날짜 표현 포맷(format) 형태에 부합하며 유효한지 검사한다.
	 * 
	 * @param dateTimeString yyyyMMddHHmmss 형태의 날짜 표현 문자열
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *                 false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * boolean check = DateTimeUtil.isValid(&quot;20070711&quot;, &quot;yyyyMMdd&quot;)
	 * </pre>
	 */
	public static final boolean isValid(String dateTimeString, String format) {

		if (dateTimeString == null){
			return false;
		}
		if (format == null){
			return false;
		}
		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			// date = formatter.parse(dateTimeString);
			date = DateTimeUtil.parse(dateTimeString, format);

		} catch (java.text.ParseException e) {
			return false;
		}

		String str = null;
		try {
			str = DateTimeUtil.format(date, format);
		} catch (ParseException pe) {
			return false;
		}

		// if (!formatter.format(date).equals(dateTimeString)) {
		return str.equals(dateTimeString);
	}

	/**
	 * <pre>
	 * &quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot; 형태의 날짜 표현 문자열 반환.
	 * 
	 * @return 날짜 표현 문자열(&quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString()
	 * </pre>
	 */
	public static final String getLongDateString() {
		return getLongDateString(java.util.TimeZone.getDefault().getID());
	}

	/**
	 * <pre>
	 * 주어진 GMT timeZoneId에 대해
	 * &quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot; 형태의 날짜 표현 문자열 반환.
	 * 
	 * @param timeZoneId GMT timeZoneId
	 * @return 날짜 표현 문자열(&quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString(&quot;GMT+9&quot;)
	 * </pre>
	 */
	public static final String getLongDateString(String timeZoneId) {
		return getLongDateString(new java.util.Date().getTime(), timeZoneId);
	}

	/**
	 * <pre>
	 * 주어진 Locale에 대해 해당 형태의 날짜 표현 문자열 반환.
	 * 
	 * @param locale Locale
	 * @return 해당 Locale의 날짜 표현 문자열(&quot;11 juillet 2007 14:18:20 KST&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString(java.util.Locale.FRENCH)
	 * </pre>
	 */
	public static final String getLongDateString(java.util.Locale locale) {
		return getLongDateString(new java.util.Date().getTime(), java.util.TimeZone.getDefault().getID(), locale);
	}

	/**
	 * <pre>
	 * 주어진 GMT timeZoneId과 long형 시간값에 대해
	 * &quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot; 형태의 날짜 표현 문자열 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param timeZoneId GMT timeZoneId
	 * @return 날짜 표현 문자열(&quot;2007년 7월 11일 (수) 오후 2시 06분 02초&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString(new Date().getTime(), &quot;GMT+9&quot;)
	 * </pre>
	 */
	public static final String getLongDateString(long t, String timeZoneId) {
		return getLongDateString(t, timeZoneId, java.util.Locale.getDefault());
	}

	/**
	 * <pre>
	 * 주어진 long형 시간값과 GMT timeZoneId 그리고 Locale에 대해 해당 형태의 날짜 표현 문자열 반환.
	 * 
	 * @param timeZoneId GMT timeZoneId
	 * @param locale Locale
	 * @return 해당 Locale의 날짜 표현 문자열(&quot;11 juillet 2007 14:18:20 KST&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString(&quot;GMT+9&quot;, java.util.Locale.FRENCH)
	 * </pre>
	 */
	public static final String getLongDateString(String timeZoneId, java.util.Locale locale) {
		java.text.DateFormat longFormatter = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.LONG,
				java.text.DateFormat.LONG, locale);

		longFormatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return longFormatter.format(new java.util.Date());
	}

	/**
	 * <pre>
	 * 주어진 GMT timeZoneId과 Locale에 대해 해당 형태의 날짜 표현 문자열 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param timeZoneId GMT timeZoneId
	 * @param locale Locale
	 * @return 해당 Locale의 날짜 표현 문자열(&quot;11 juillet 2007 14:18:20 KST&quot;)
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getLongDateString(new Date().getTime(), &quot;GMT+9&quot;, java.util.Locale.FRENCH)
	 * </pre>
	 */
	public static final String getLongDateString(long t, String timeZoneId, java.util.Locale locale) {
		java.text.DateFormat longFormatter = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.LONG,
				java.text.DateFormat.LONG, locale);

		longFormatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return longFormatter.format(new java.util.Date(t));
	}

	/**
	 * <pre>
	 * 현재 날짜의 년(year) 반환.
	 * 
	 * @return int 현재 날짜 년(year)
	 * </pre>
	 */
	public static final int getYear() {
		return getNumberByPattern("yyyy");
	}

	/**
	 * <pre>
	 * 현재 날짜의 월(month) 반환.
	 * 
	 * @return int 현재 날짜 월(month)
	 * </pre>
	 */
	public static final int getMonth() {
		return getNumberByPattern("MM");
	}
	/**
	 * <pre>
	 * 입력한 날짜의 월(month) 반환.
	 * @param date : 날짜 정보(20070101)
	 * @return String : 입력한 날짜의 월(month)
	 * </pre>
	 */
	public static final String getMonthString(String date) {
		if(date != null && !"".equals(date) && date.length() >= 6) {
			return date.substring(4,6);
		} else {
			return "";
		}
	}
	/**
	 * <pre>
	 * 입력한 날짜의 일(day) 반환.
	 * @param date : 날짜 정보(20070101)
	 * @return String : 입력한 날짜의 일(day)
	 * </pre>
	 */
	public static final String getDayString(String date) {
		if(date != null && !"".equals(date) && date.length() >= 8) {
			return date.substring(6,8);
		} else {
			return "";
		}
	}
	/**
	 * <pre>
	 * 입력한 날짜의 년(year) 반환.
	 * @param date : 날짜 정보(20070101)
	 * @return String : 입력한 날짜의 년(year)
	 * </pre>
	 */
	public static final String getYearString(String date) {
		if(date != null && !"".equals(date) && date.length() >= 6) {
			return date.substring(0,4);
		} else {
			return "";
		}
	}

	/**
	 * <pre>
	 * 현재 날짜의 일(day) 반환.
	 * 
	 * @return int 현재 날짜 일(day)
	 * </pre>
	 */
	public static final int getDay() {
		return getNumberByPattern("dd");
	}

	/**
	 * <pre>
	 * 현재 날짜에 대해 날짜 패턴(yyyy, MM, dd, HH, mm, ss 등)에 해당하는 값을 반환.
	 * 
	 * @param pattern 날짜 패턴
	 * @return int
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getNumberByPattern(&quot;dd&quot;)
	 * </pre>
	 */
	public static final int getNumberByPattern(String pattern) {
		return getNumberByPattern(new java.util.Date().getTime(), pattern);
	}

	/**
	 * <pre>
	 * long형 시간값(milliseconds)에 대해 날짜 패턴(yyyy, MM, dd, HH, mm, ss 등)에 해당하는 값을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param pattern 날짜 패턴 (yyyy, MM, dd, HH, mm, ss 등)
	 * @return int
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getNumberByPattern(new Date().getTime(),&quot;dd&quot;)
	 * </pre>
	 */
	public static final int getNumberByPattern(long t, String pattern) {
		/*
		 * java.text.SimpleDateFormat formatter = new
		 * java.text.SimpleDateFormat(pattern); String dateString =
		 * formatter.format(new java.util.Date(t)); return
		 * Integer.parseInt(dateString);
		 */

		String str = null;
		try {
			str = DateTimeUtil.format(new Date(t), pattern);
		} catch (ParseException pe) {
			return 0;
		}
		return Integer.parseInt(str);

		// return Integer.parseInt(DateTimeUtil.format(new Date(t), pattern));
	}

	/**
	 * <pre>
	 * long형 시간값(milliseconds)에 대해 날짜 패턴(yyyy, MM, dd, HH, mm, ss 등)과
	 * TimeZone에 해당하는 값을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param pattern 날짜 패턴 (yyyy, MM, dd, HH, mm, ss 등)
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return int
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * DateTimeUtil.getNumberByPattern(new Date().getTime(),&quot;dd&quot;, &quot;GMT+9&quot;)
	 * </pre>
	 */
	public static final int getNumberByPattern(long t, String pattern, String timeZoneId) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		String dateString = formatter.format(new java.util.Date(t));
		return Integer.parseInt(dateString);
	}

	/**
	 * <pre>
	 * 현재의 날짜와 시간에 대해 날짜 포맷을 적용한 결과 문자열을 반환.
	 * 
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return 현재 날짜의 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(&quot;yyyy-MM-dd HH:mm:ss&quot;);
	 * </pre>
	 */
	public static final String getFormatString(String format) {

		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat(format);
		// String dateString = formatter.format(new java.util.Date());
		// return dateString;

		String str = null;
		try {
			str = DateTimeUtil.format(new Date(), format);
		} catch (ParseException pe) {
			return null;
		}
		return str;
	}

	/**
	 * <pre>
	 * 현재의 날짜와 시간에 대해 날짜 포맷과 TimeZone을 적용한 결과 문자열을 반환.
	 * 
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return 현재 날짜의 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(&quot;yyyy-MM-dd HH:mm:ss&quot;, &quot;GMT+9&quot;);
	 * </pre>
	 */
	public static final String getFormatString(String format, String timeZoneId) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return formatter.format(new java.util.Date());
	}

	/**
	 * <pre>
	 * long형 시간값에 대해 날짜 포맷을 적용한 결과 문자열을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(
	 *      new java.util.Date().getTime(),&quot;yyyy-MM-dd HH:mm:ss&quot;);
	 * </pre>
	 */
	public static final String getFormatString(long t, String format) {
		return getFormatString(t, format, DEFAULT_TIMEZONE);
	}

	/**
	 * <pre>
	 * long형 시간값에 대해 날짜 포맷과 TimeZone을 적용한 결과 문자열을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(new java.util.Date().getTime(),&quot;yyyy-MM-dd HH:mm:ss&quot;, &quot;GMT+9&quot;);
	 * </pre>
	 */
	public static final String getFormatString(long t, String format, String timeZoneId) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return formatter.format(new java.util.Date(t));
	}

	/**
	 * <pre>
	 * long형 시간값에 대해 날짜 포맷과 TimeZone 그리고 Locale을 적용한 결과 문자열을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @param locale Locale
	 * @return 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(
	 *      new java.util.Date().getTime(),&quot;yyyy-MM-dd HH:mm:ss&quot;, &quot;GMT+9&quot;, java.util.Locale.FRENCH);
	 * </pre>
	 */
	public static final String getFormatString(long t, String format, String timeZoneId, Locale locale) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, locale);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return formatter.format(new java.util.Date(t));
	}

	/**
	 * <pre>
	 * long형 시간값에 대해 날짜 포맷과 TimeZone 그리고 Locale을 적용한 결과 문자열을 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @param locale String
	 * @return 날짜 포맷 형태의 문자열
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 * String time = DateTimeUtil.getFormatString(
	 *      new java.util.Date().getTime(),&quot;yyyy-MM-dd HH:mm:ss&quot;, &quot;GMT+9&quot;, java.util.Locale.FRENCH);
	 * </pre>
	 */
	public static final String getFormatString(long t, String format, String timeZoneId, String locale) {

		Locale l = Locale.getDefault();
		int i = 0, j = 0;
		if ((i = locale.indexOf("_")) > 0) {
			if ((j = locale.indexOf(".")) > i) {
				l = new java.util.Locale(locale.substring(0, i), locale.substring(i + 1, j));
			}
		}
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, l);
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		return formatter.format(new java.util.Date(t));
	}

	/**
	 * <pre>
	 * 현재날짜를 yyyyMMdd 형태로 반환.
	 * 
	 * @return yyyyMMdd 형태의 현재 날짜
	 * </pre>
	 */
	public static final String getDateString() {

		String str = null;
		try {
			str = DateTimeUtil.format(new Date(), DEFAULT_DATE_FORMAT);
		} catch (ParseException pe) {
			return null;
		}
		return str;

		/*
		 * SimpleDateFormat shortFormatter = new
		 * java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT); return
		 * shortFormatter.format(new java.util.Date());
		 */
	}

	/**
	 * <pre>
	 * long형 시간값의 날짜를 yyyyMMdd 형태로 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @return yyyyMMdd 형태의 현재 날짜
	 * </pre>
	 */
	public static final String getDateString(long t) {
		return getDateString(t, DEFAULT_TIMEZONE);
	}

	/**
	 * <pre>
	 * TimeZone을 적용한 현재의 날짜를 yyyyMMdd 형태로 반환.
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return yyyyMMdd 형태의 현재 날짜
	 * </pre>
	 */
	public static final String getDateString(String timeZoneId) {

		SimpleDateFormat shortFormatter = new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		shortFormatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		String retStr = shortFormatter.format(new java.util.Date());
		return retStr;
	}

	/**
	 * <pre>
	 * TimeZone을 적용한 long형 시간값의 날짜를 yyyyMMdd 형태로 반환.
	 * 
	 * @param t 1970/1/1 기준의 long형 시간값(milliseconds)
	 * @param timeZoneId 예를들어, &quot;GMT+9&quot;.
	 * @return yyyyMMdd 형태의 현재 날짜
	 * </pre>
	 */
	public static final String getDateString(long t, String timeZoneId) {

		SimpleDateFormat shortFormatter = new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		shortFormatter.setTimeZone(java.util.TimeZone.getTimeZone(timeZoneId));
		String retStr = shortFormatter.format(new java.util.Date(t));
		return retStr;
	}

	/**
	 * <pre>
	 * &quot;yyyy-MM-dd-HH:mm:ss:SSS&quot; 형태의 TimeStamp를 반환한다.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @return &quot;yyyy-MM-dd-HH:mm:ss:SSS&quot; 형태의 TimeStamp
	 * </pre>
	 */
	public static final String getTimeStampString() {

		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
		// return formatter.format(new java.util.Date());

		String str = null;
		try {
			str = DateTimeUtil.format(new Date(), "yyyy-MM-dd-HH:mm:ss:SSS");
		} catch (ParseException pe) {
			return null;
		}
		return str;
	}

	/**
	 * <pre>
	 * 시분초(&quot;HHmmss&quot;) 문자열을 반환한다.
	 * 
	 * @return 시분초(&quot;HHmmss&quot;)
	 * </pre>
	 */
	public static final String getShortTimeString() {

		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat("HHmmss");
		// return formatter.format(new java.util.Date());

		String str = null;
		try {
			str = DateTimeUtil.format(new Date(), "HHmmss");
		} catch (ParseException pe) {
			return null;
		}
		return str;
	}

	/**
	 * <pre>
	 * 시분초(&quot;HH:mm:ss&quot;) 문자열을 반환한다.
	 * 
	 * @return 시분초(&quot;HH:mm:ss&quot;)
	 * </pre>
	 */
	public static final String getTimeString() {

		// java.text.SimpleDateFormat formatter = new
		// java.text.SimpleDateFormat("HH:mm:ss");
		// return formatter.format(new java.util.Date());
		String str = null;
		try {
			str = DateTimeUtil.format(new Date(), "HH:mm:ss");
		} catch (ParseException pe) {
			return null;
		}
		return str;
	}

	/**
	 * <pre>
	 * &quot;yyyyMMdd&quot; 형태의 날짜 표현 문자열(dateString)에 대해 해당 요일 값을 반환.
	 * 1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
	 * 2: 월요일 (java.util.Calendar.MONDAY 와 비교)
	 * 3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
	 * 4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
	 * 5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
	 * 6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
	 * 7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @return int 날짜 형식이 맞고, 존재하는 날짜일 때 요일을 리턴
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 날짜
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 *  int dayOfWeek = DateTimeUtil.whichDay(&quot;20070720&quot;)
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 * </pre>
	 */
	public static final int whichDay(String dateString) throws java.text.ParseException {
		return whichDay(dateString, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <pre>
	 * &quot;yyyyMMdd&quot; 형태의 날짜 표현 문자열(dateString)에 대해 해당 요일 값을 반환.
	 * 
	 * 1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
	 * 2: 월요일 (java.util.Calendar.MONDAY 와 비교)
	 * 3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
	 * 4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
	 * 5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
	 * 6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
	 * 7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @return int 날짜 형식이 맞고, 존재하는 날짜일 때 요일을 리턴
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 날짜
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 *  int dayOfWeek = DateTimeUtil.whichDay(&quot;20070720&quot;)
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 * </pre>
	 */
	public static final String whichDay(String dateString, int printPattern) throws java.text.ParseException {
		String retValue = "";
		switch (whichDay(dateString)) {
		case java.util.Calendar.SUNDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.SUNDAY);
			} else if (printPattern == 2) {
				retValue = "일";
			} else if (printPattern == 3) {
				retValue = "일요일";
			} else if (printPattern == 4) {
				retValue = "SUN";
			} else if (printPattern == 5) {
				retValue = "SUNDAY";
			}
			break;
		case java.util.Calendar.MONDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.MONDAY);
			} else if (printPattern == 2) {
				retValue = "월";
			} else if (printPattern == 3) {
				retValue = "월요일";
			} else if (printPattern == 4) {
				retValue = "MON";
			} else if (printPattern == 5) {
				retValue = "MONDAY";
			}
			break;
		case java.util.Calendar.TUESDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.TUESDAY);
			} else if (printPattern == 2) {
				retValue = "화";
			} else if (printPattern == 3) {
				retValue = "화요일";
			} else if (printPattern == 4) {
				retValue = "TUE";
			} else if (printPattern == 5) {
				retValue = "TUESDAY";
			}
			break;
		case java.util.Calendar.WEDNESDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.WEDNESDAY);
			} else if (printPattern == 2) {
				retValue = "수";
			} else if (printPattern == 3) {
				retValue = "수요일";
			} else if (printPattern == 4) {
				retValue = "WED";
			} else if (printPattern == 5) {
				retValue = "WEDNESDAY";
			}
			break;
		case java.util.Calendar.THURSDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.THURSDAY);
			} else if (printPattern == 2) {
				retValue = "목";
			} else if (printPattern == 3) {
				retValue = "목요일";
			} else if (printPattern == 4) {
				retValue = "THU";
			} else if (printPattern == 5) {
				retValue = "THURSDAY";
			}
			break;
		case java.util.Calendar.FRIDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.FRIDAY);
			} else if (printPattern == 2) {
				retValue = "금";
			} else if (printPattern == 3) {
				retValue = "금요일";
			} else if (printPattern == 4) {
				retValue = "FRI";
			} else if (printPattern == 5) {
				retValue = "FRIDAY";
			}
			break;
		case java.util.Calendar.SATURDAY:
			if (printPattern == 1) {
				retValue = String.valueOf(java.util.Calendar.SATURDAY);
			} else if (printPattern == 2) {
				retValue = "토";
			} else if (printPattern == 3) {
				retValue = "토요일";
			} else if (printPattern == 4) {
				retValue = "SAT";
			} else if (printPattern == 5) {
				retValue = "SATURDAY";
			}
			break;
		}
		return retValue;

	}

	/**
	 * <pre>
	 * 날짜 표현 포맷(format)과 날짜 표현 문자열(dateTimeString)에 대해 해당 요일 값을 반환.
	 * 1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
	 * 2: 월요일 (java.util.Calendar.MONDAY 와 비교)
	 * 3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
	 * 4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
	 * 5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
	 * 6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
	 * 7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
	 * 
	 * @param dateTimeString 날짜 표현 문자열(yyyyMMdd)
	 * @param format 날짜 표현 포맷 예를들어, &quot;yyyy/MM/dd&quot;.
	 * @return int 날짜 형식이 맞고, 존재하는 날짜일 때 요일을 리턴
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 날짜
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 *  int dayOfWeek = DateTimeUtil.whichDay(&quot;20070720&quot;, &quot;yyyyMMdd&quot;)
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 * </pre>
	 */
	public static final int whichDay(String dateTimeString, String format) throws java.text.ParseException {
		java.util.Date date = getDate(dateTimeString, format);
		return getCalendar(date).get(java.util.Calendar.DAY_OF_WEEK);
	}

	/**
	 * <pre>
	 * 두 Date 중 작은 Date를 반환.
	 * @param d1 비교 Date
	 * @param d2 비교 Date
	 * @return Date 작은 날짜
	 * </pre>
	 */
	public static final Date minDate(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return null;
		}
		if ((d1.getTime() - d2.getTime()) > 0) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * <pre>
	 * 두 Date 중 큰 Date를 반환.
	 * @param d1 비교 Date
	 * @param d2 비교 Date
	 * @return Date 큰 Date
	 * </pre>
	 */
	public static final Date maxDate(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return null;
		}
		if ((d1.getTime() - d2.getTime()) < 0) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 일수(day)를 계산하여 Date 형태로 반환.
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param day 더할 일수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addDays(String dateString, int day) throws ParseException {
		return addDays(dateString, day, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 일수(day)를 계산하여 String 형태로 반환.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param day 더할 일수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final String addDaysStr(String dateString, int day) throws ParseException {

		Date date = addDays(dateString, day, DEFAULT_DATE_FORMAT);
		// SimpleDateFormat shortFormatter = new
		// java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		// return shortFormatter.format(date);
		return DateTimeUtil.format(date, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열에 대해 더할 일수(day)를 계산하여 String 형태로 반환.
	 * 
	 * @param dateString 날짜 표현 문자열
	 * @param day 더할 일수
	 * @return Date 계산된 날짜
	 * @return String 날짜형식
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final String addDaysStr(String dateString, int day, String format) throws ParseException {

		Date date = addDays(dateString, day, format);

		/*
		 * SimpleDateFormat shortFormatter = new
		 * java.text.SimpleDateFormat(format); return
		 * shortFormatter.format(date);
		 */

		String str = null;
		try {
			str = DateTimeUtil.format(date, format);
		} catch (ParseException pe) {
			return null;
		}
		return str;
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)과 날짜 표현 포맷(format)에 해당하는 날짜에 대해
	 * 더할 일수(day)를 계산하여 Date 형태로 반환.
	 * 
	 * @param dateTimeString 날짜 표현 문자열(yyyyMMdd)
	 * @param day 더할 일수
	 * @param format  날짜 표현 포맷(format)
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addDays(String dateTimeString, int day, String format) throws ParseException {

		Date date = getDate(dateTimeString, format);
		return addDays(date, day);
	}

	/**
	 * <pre>
	 * 입력 Date에 대해 더할 일수(day)를 계산하여 Date 형태로 반환.
	 * @param date Date
	 * @param day 더할 일수
	 * @return Date 계산된 날짜
	 * </pre>
	 */
	public static final Date addDays(Date date, int day) {
		Date returnDate = null;

		if (day == 0) {
			returnDate = date;
		} else {
			// GregorianCalendar newDate = new GregorianCalendar();
			GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
			newDate.setTime(date);
			newDate.add(GregorianCalendar.DATE, day);
			returnDate = newDate.getTime();
		}
		return returnDate;
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 월수(month)를 계산하여 Date 형태로 반환.(양편)
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param month 더할 월수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addMonths(String dateString, int month) throws ParseException {
		return addMonths(dateString, month, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 월수(month)를 계산하여 String 형태로 반환.(양편)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param month 더할 월수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 *  DateTimeUtil.addMonthsStr(&quot;20080520&quot;,-1) ==&gt; &quot;20080420&quot;
	 *  DateTimeUtil.addMonthsStr(&quot;20080530&quot;,-1) ==&gt; &quot;20080430&quot; 
	 *  DateTimeUtil.addMonthsStr(&quot;20080531&quot;,-1) ==&gt; &quot;20080430&quot;
	 * </pre>
	 */
	public static final String addMonthsStr(String dateString, int month) throws ParseException {

		Date date = addMonths(dateString, month);
		SimpleDateFormat shortFormatter = new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return shortFormatter.format(date);
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 월수(month)를 계산하여 String 형태로 반환.(한편)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param month 더할 월수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * 
	 * &lt;b&gt;Example)&lt;/b&gt;
	 *  DateTimeUtil.addMonthsStr2(&quot;20080520&quot;,-1) ==&gt; &quot;20080521&quot;
	 *  DateTimeUtil.addMonthsStr2(&quot;20080530&quot;,-1) ==&gt; &quot;20080501&quot;
	 *  DateTimeUtil.addMonthsStr2(&quot;20080531&quot;,-1) ==&gt; &quot;20080501&quot;
	 * </pre>
	 */
	public static final String addMonthsStr2(String dateString, int month) throws ParseException {

		Date date;
		if (month < 0){
			date = addDays(addMonths(dateString, month), 1);
		}else if (month > 0){
			date = addDays(addMonths(dateString, month), -1);
		}else{
			date = addMonths(dateString, month);
		}
		SimpleDateFormat shortFormatter = new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return shortFormatter.format(date);
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)과 날짜 표현 포맷(format)에 해당하는 날짜에 대해
	 * 더할 월수(month)를 계산하여 Date 형태로 반환.
	 * @param dateTimeString 날짜 표현 문자열(yyyyMMdd)
	 * @param month 더할 월수
	 * @param format  날짜 표현 포맷(format)
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addMonths(String dateTimeString, int month, String format) throws ParseException {
		Date date = getDate(dateTimeString, format);
		return addMonths(date, month);
	}

	/**
	 * <pre>
	 * 입력 Date에 대해 더할 월수(month)를 계산하여 Date 형태로 반환.
	 * @param date Date
	 * @param month 더할 월수
	 * @return Date 계산된 날짜
	 * </pre>
	 */
	public static final Date addMonths(Date date, int month) {
		Date returnDate = null;

		if (month == 0) {
			returnDate = date;
		} else {
			// GregorianCalendar newDate = new GregorianCalendar();
			GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
			newDate.setTime(date);
			newDate.add(GregorianCalendar.MONTH, month);
			returnDate = newDate.getTime();
		}

		return returnDate;
	}

	public static final Date addMonthsWithLastDays(String dateString, int month, boolean isLastDay)
			throws ParseException {
		Date date = getDate(dateString, DEFAULT_DATE_FORMAT);
		Date returnDate = null;

		if (month == 0) {
			returnDate = date;
		} else {
			// GregorianCalendar newDate = new GregorianCalendar();
			GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
			newDate.setTime(date);
			newDate.add(GregorianCalendar.MONTH, month);

			if (isLastDay) {
				int lastDay = newDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
				newDate.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
			}
			returnDate = newDate.getTime();
		}

		return returnDate;
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 년수(year)를 계산하여 Date 형태로 반환.
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param year 더할 년수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addYears(String dateString, int year) throws java.text.ParseException {
		return addYears(dateString, year, DEFAULT_DATE_FORMAT);
	}

	private final static Map<String, Map<Integer, String>> addYearsStrMap = new ConcurrentHashMap<String, Map<Integer, String>>();

	// ConcurrentHashMap<K, V>
	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)에 대해 더할 년수(year)를 계산하여 String 형태로 반환.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param dateString 날짜 표현 문자열(yyyyMMdd)
	 * @param year 더할 년수
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final String addYearsStr(String dateString, int year) throws java.text.ParseException {
		Map<Integer, String> map = addYearsStrMap.get(dateString);
		if (map != null) {
			String str = map.get(year);
			if (str != null)
				return str;
		}

		Date date = addYears(dateString, year, DEFAULT_DATE_FORMAT);
		String retValue = DateTimeUtil.format(date, DEFAULT_DATE_FORMAT);

		if (map == null){
			map = new ConcurrentHashMap<Integer, String>();
		}
		map.put(year, retValue);
		addYearsStrMap.put(dateString, map);
		return retValue;
	}

	private final static Map<Integer, Map<Integer, Integer>> addYearsIntMap = new ConcurrentHashMap<Integer, Map<Integer, Integer>>();

	public static final int addYearsStr(int dateInt, int year) throws java.text.ParseException {
		Map<Integer, Integer> map = addYearsIntMap.get(dateInt);
		if (map != null) {
			Integer ret = map.get(year);
			if (ret != null) {
				return ret.intValue();
			}
		}

		Date date = addYears(dateInt, year, DEFAULT_DATE_FORMAT);
		int retValue = DateTimeUtil.formatInt(date, DEFAULT_DATE_FORMAT);

		if (map == null){
			map = new ConcurrentHashMap<Integer, Integer>();
		}
		map.put(year, retValue);
		addYearsIntMap.put(dateInt, map);
		return retValue;
	}

	/**
	 * <pre>
	 * 날짜 표현 문자열(yyyyMMdd)과 날짜 표현 포맷(format)에 해당하는 날짜에 대해
	 * 더할 년수(year)를 계산하여 Date 형태로 반환.
	 * @param dateTimeString 날짜 표현 문자열(yyyyMMdd)
	 * @param year 더할 년수
	 * @param format  날짜 표현 포맷(format)
	 * @return Date 계산된 날짜
	 * @throws ParseException 형식이 잘못 되었거나 존재하지 않는 날짜 예외
	 * </pre>
	 */
	public static final Date addYears(String dateTimeString, int year, String format) throws java.text.ParseException {
		Date date = getDate(dateTimeString, format);
		return addYears(date, year);
	}

	public static final Date addYears(int dateTimeInt, int year, String format) throws java.text.ParseException {
		Date date = getDate(dateTimeInt, format);
		return addYears(date, year);
	}

	/**
	 * <pre>
	 * 입력 Date에 대해 더할 년수(year)를 계산하여 Date 형태로 반환.
	 * @param date Date
	 * @param year 더할 년수
	 * @return Date 계산된 날짜
	 * </pre>
	 */
	public static final Date addYears(Date date, int year) {
		Date returnDate = null;

		if (year == 0) {
			returnDate = date;
		} else {
			// GregorianCalendar newDate = new GregorianCalendar();
			GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
			newDate.setTime(date);
			newDate.add(GregorianCalendar.YEAR, year);
			returnDate = newDate.getTime();
		}

		return returnDate;
	}

	public static final Date addYearMonthDaysWithLastDays(Date date, int years, int months, int days, boolean isLastDay) {
		Date addedDate = DateTimeUtil.addYears(date, years);
		addedDate = DateTimeUtil.addMonths(addedDate, months);
		addedDate = DateTimeUtil.addDays(addedDate, days);

		// GregorianCalendar newDate = new GregorianCalendar();
		GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
		newDate.setTime(addedDate);
		if (isLastDay) {
			int lastDay = newDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

			newDate.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
		}

		addedDate = newDate.getTime();
		return addedDate;
	}

	public static final Date addYearMonthDaysWithLastDays(String dateString, int years, int months, int days,
			boolean isLastDay) throws ParseException {
		Date date = getDate(dateString, DEFAULT_DATE_FORMAT);
		return addYearMonthDaysWithLastDays(date, years, months, days, isLastDay);
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 long형 시간값(milliseconds)으로 반환.
	 * @param from 입력 Date
	 * @param to 입력 Date
	 * @return long 1970/1/1 기준의 long형 시간값
	 * </pre>
	 */
	public static final long diffTimes(Date from, Date to) {
		return to.getTime() - from.getTime();
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 초(seconds) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 시간 초(seconds)
	 * </pre>
	 */
	public static final int diffSeconds(Date from, Date to) {
		return (int) (diffTimes(from, to) / (1000));
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 분(minutes) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 시간 분(minutes)
	 * </pre>
	 */
	public static final int diffMinutes(Date from, Date to) {
		return (int) (diffTimes(from, to) / (1000 * 60));
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 시간(hours) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 시간(hours)
	 * </pre>
	 */
	public static final int diffHours(Date from, Date to) {
		return (int) (diffTimes(from, to) / (1000 * 60 * 60));
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 일자(days) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 일자(days)
	 * </pre>
	 */
	public static final int diffDays(Date from, Date to) {
		return (int) (diffTimes(from, to) / (1000 * 60 * 60 * 24));
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 일자(days) 값으로 반환.
	 * @param from  입력 String
	 * @param to 입력 String
	 * @return int 차이 일자(days)
	 * </pre>
	 */
	public static final int diffDays(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffDays(fromDate, toDate);
	}

	public static final int diffDays(int from, int to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffDays(fromDate, toDate);
	}

	public static final int diffDaysWithStart(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		// GregorianCalendar fromCalendar = new GregorianCalendar();
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		fromCalendar.setTime(fromDate);
		fromCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
		fromDate = fromCalendar.getTime();

		return diffDays(fromDate, toDate);
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 달(months) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 달(months)
	 * </pre>
	 */
	public static final int diffMonths(Date from, Date to) throws ParseException {

		// from = DateTimeUtil.getDate("20080929", "yyyyMMdd");
		// to = DateTimeUtil.getDate("20090928", "yyyyMMdd");

		int diffYears = 0;
		int diffMonths = 0;
		try {
			diffYears = diffYears(from, to);

			// GregorianCalendar fromCalendar = new GregorianCalendar();
			GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
			// GregorianCalendar toCalendar = new GregorianCalendar();
			GregorianCalendar toCalendar = DateTimeUtil.getGregorianCalendar();

			fromCalendar.setTime(from);
			toCalendar.setTime(to);

			if (diffYears > 0) {
				fromCalendar.add(GregorianCalendar.YEAR, diffYears);
				diffMonths = diffYears * 12;
			}

			if (fromCalendar.getTimeInMillis() < toCalendar.getTimeInMillis()) {
				while (fromCalendar.getTimeInMillis() < toCalendar.getTimeInMillis()) {
					fromCalendar.setTime(from); // 왜 다시 세팅할까???
					diffMonths++;
					fromCalendar.add(GregorianCalendar.MONTH, diffMonths);
					// //("diffMonths A : " + diffMonths);
				}

				while (fromCalendar.getTimeInMillis() > toCalendar.getTimeInMillis()) { // 이코드는
																						// if에서
																						// 이미
																						// 무의미해??
					fromCalendar.add(GregorianCalendar.MONTH, -1);
					diffMonths--;
					// //("diffMonths B : " + diffMonths);
				}
			}

			// if(fromCalendar.getTimeInMillis() < toCalendar.getTimeInMillis())
			// {
			// while(fromCalendar.getTimeInMillis() <
			// toCalendar.getTimeInMillis()){
			// fromCalendar.add(GregorianCalendar.MONTH, 1);
			// diffMonths++;
			// //("diffMonths A : " + diffMonths);
			// }
			//                
			// while(fromCalendar.getTimeInMillis() >
			// toCalendar.getTimeInMillis()){
			// fromCalendar.add(GregorianCalendar.MONTH, -1);
			// diffMonths--;
			// //("diffMonths B : " + diffMonths);
			// }
			// }

		} catch (Exception e) {
			return 0;
		}
		return diffMonths;
	}

	public static final int diffMonths(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffMonths(fromDate, toDate);
	}

	public static final int diffMonthsWithStart(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		// GregorianCalendar fromCalendar = new GregorianCalendar();
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		fromCalendar.setTime(fromDate);
		fromCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
		fromDate = fromCalendar.getTime();

		return diffMonths(fromDate, toDate);
	}

	/**
	 * <pre>
	 * 두 입력 날짜의 크기비교
	 * 
	 * fromDate가 더 클 경우 -1을 return
	 * fromDate 와 toDate가 같을 경우 0을 return
	 * fromDate가 더 작을 경우 1을 return
	 * 
	 * @param strFrom  입력 Date
	 * @param strTo 입력 Date
	 * @return int 
	 * </pre>
	 */
	public static final int compareDate(String strFrom, String strTo) throws ParseException {
		Date from = DateTimeUtil.getDate(strFrom, "yyyyMMdd");
		Date to = DateTimeUtil.getDate(strTo, "yyyyMMdd");
		return compareDate(from, to);
	}

	private static final int compareDate(Date from, Date to) {
		if (from.getTime() > to.getTime()){
			return -1;// fromDate가 더 클경우,
		}else if (from.getTime() == to.getTime()){
			return 0; // fromDate가 같을 경우,
		}else{
			return 1; // fromDate가 작을 경우
		}
	}

	/**
	 * <pre>
	 * 두 입력 Date에 대해 차이 시간을 int형 년(years) 값으로 반환.
	 * @param from  입력 Date
	 * @param to 입력 Date
	 * @return int 차이 년(years)
	 * </pre>
	 */
	public static final int diffYears(Date from, Date to) {
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		GregorianCalendar toCalendar = DateTimeUtil.getGregorianCalendar();

		// from값이 더 클 경우
		if (from.getTime() > to.getTime()) {
			fromCalendar.setTime(to);
			toCalendar.setTime(from);
		} else {
			fromCalendar.setTime(from);
			toCalendar.setTime(to);
		}

		int years;
		if (fromCalendar.compareTo(toCalendar) == 0) {
			years = 0;
		} else {
			years = toCalendar.get(Calendar.YEAR) - fromCalendar.get(Calendar.YEAR);

			fromCalendar.add(Calendar.YEAR, years); // 차이년 더해보고

			if (fromCalendar.getTimeInMillis() > toCalendar.getTimeInMillis()) {
				--years; // 오히려 크면 -1년
			}

			if (from.getTime() > to.getTime()){
				years *= -1;
			}
		}
		return years;
	}

	public static final int diffYears(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffYears(fromDate, toDate);
	}

	public static final int diffYearsWithStart(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		// GregorianCalendar fromCalendar = new GregorianCalendar();
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		fromCalendar.setTime(fromDate);
		fromCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
		fromDate = fromCalendar.getTime();

		return diffYears(fromDate, toDate);
	}

	/**
	 * <pre>
	 * 기준일자와 현재일자의 경과일수를 주단위로 리턴.
	 * 
	 * 경과일수계산은 양편포함
	 * 남은 일수는 버림
	 * 
	 * [예]
	 * 기준일자 : 20080506
	 * 현재일자 : 20080710
	 * 
	 * DateTimeUtil.diffWeeks(&quot;20080506&quot;); ==&gt; return : 9
	 * 
	 * 2008-05-06부터 현재(2008-07-10)까지 66일(9주 3일)이 지났으므로 9를 리턴
	 * 남은 3일은 버림.  
	 * 
	 * @param from 기준일자FROM(YYYYMMDD)
	 * @return int 경과주차
	 * </pre>
	 */
	public static final int diffWeeks(String from) throws ParseException {
		int diffDays = DateTimeUtil.diffDays(from, DateTimeUtil.getDateString());
		// int days = DateTimeUtil.diffYearMonthDayWithStart(from,
		// DateTimeUtil.getDateString())[2];
		return diffDays / 7;
	}

	/**
	 * <pre>
	 * 기준일자(from)와 기준일자(to)의 경과일수를 주단위로 리턴.
	 * 
	 * 경과일수계산은 양편포함
	 * 남은 일수는 버림
	 * 
	 * [예]
	 * 기준일자from : 20080506
	 * 기준일자to   : 20080710
	 * 
	 * DateTimeUtil.diffWeeks(&quot;20080506&quot;, &quot;20080710&quot;); ==&gt; return : 9
	 * 
	 * 2008-05-06부터 2008-07-10까지 66일(9주 3일)이 지났으므로 9를 리턴
	 * 남은 3일은 버림.  
	 * 
	 * @param from 기준일자FROM(YYYYMMDD)
	 * @param to   기준일자TO(YYYYMMDD)
	 * @return int 경과주차
	 * </pre>
	 */
	public static final int diffWeeks(String from, String to) throws ParseException {
		int diffDays = DateTimeUtil.diffDays(from, to);
		return diffDays / 7;
		// return DateTimeUtil.diffYearMonthDayWithStart(from, to)[2]/7;
	}

	/**
	 * <pre>
	 * 두 일자간 경과기간 (년수, 월수, 일수) - 한편포함
	 * 
	 * 입력파라미터 정합성 검사(NULL CHECK, 기준일자FROM &gt; 기준일자TO)
	 * 기준일자 FROM부터 기준일자 TO까지 경과기간을 년수, 월수, 일수로 리턴한다.
	 * 한편포함의 의미는 기준일자 FROM과 기준일자 TO 중 한쪽만 포함하여 일수로 계산한다는 것이다.
	 * 
	 * [예]
	 * 기준일자FROM : 1994년 2월 28일, 기준일자TO : 1995년 3월 1일
	 * =&gt; 경과기간 : 1년 0개월 1일
	 * 기준일자FROM : 1996년 2월 29일, 기준일자TO : 1997년 3월 1일 (from의 일자가 1일 늘었음에도 결과는 동일함에 주목하라)
	 * =&gt; 경과기간 : 1년 0개월 1일
	 * 기준일자FROM : 1996년 3월 1일, 기준일자TO : 1997년 3월 1일
	 * =&gt; 경과기간 : 1년 0개월 0일
	 * 
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * 
	 * @param from 기준일자FROM(YYYYMMDD)
	 * @param to 기준일자TO(YYYYMMDDD)
	 * @return int[] 경과년수, 경과월수, 경과일수
	 * </pre>
	 */
	public static final int[] diffDates(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffDates(fromDate, toDate);
	}

	public static final int[] diffDates(int from, int to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffDates(fromDate, toDate);
	}

	public static final int[] diffDates(Date from, Date to) {
		int[] retValue = new int[3];
		try {
			int months = diffMonths(from, to);
			int diffMonths = months % 12;
			int diffYears = months / 12;
			int diffDays = 0;

			// GregorianCalendar fromCalendar = new GregorianCalendar();
			GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
			// GregorianCalendar toCalendar = new GregorianCalendar();
			GregorianCalendar toCalendar = DateTimeUtil.getGregorianCalendar();

			fromCalendar.setTime(from);
			toCalendar.setTime(to);

			fromCalendar.add(GregorianCalendar.YEAR, diffYears);
			fromCalendar.add(GregorianCalendar.MONTH, diffMonths);

			while (fromCalendar.getTimeInMillis() < toCalendar.getTimeInMillis()) {
				fromCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
				diffDays++;
			}

			retValue[0] = diffYears;
			retValue[1] = diffMonths;
			retValue[2] = diffDays;

			// 양쪽 모두 말일일 경우 일수차를 0으로...(20090430~20091030)
			if (DateTimeUtil.lastDayOfMonth(from).equals(DateTimeUtil.format(from, "yyyyMMdd"))
					&& DateTimeUtil.lastDayOfMonth(to).equals(DateTimeUtil.format(to, "yyyyMMdd"))) {
				retValue[2] = 0;
			}
			DateTimeUtil.lastDayOfMonth(to);

		} catch (Exception e) {
			return null;
		}
		return retValue;
	}

	/**
	 * <pre>
	 * 두 일자간 경과기간 (년수, 월수, 일수) - 양편포함
	 * 
	 * 입력파라미터 정합성 검사(NULL CHECK, 기준일자FROM &gt; 기준일자TO)
	 * 기준일자 FROM부터 기준일자 TO까지 경과기간을 년수, 월수, 일수로 리턴한다.
	 * 양편포함의 의미는 기준일자 FROM과 기준일자 TO를 모두 포함하여 일수로 계산한다는 것이다.
	 * 
	 * [예]
	 * 기준일자FROM : 1994년 2월 28일, 기준일자TO : 1995년 3월 1일
	 * =&gt; 경과기간 : 1년 0개월 2일
	 * 기준일자FROM : 1996년 2월 29일, 기준일자TO : 1997년 3월 1일 (from의 일자가 1일 늘었음에도 결과는 동일함에 주목하라)
	 * =&gt; 경과기간 : 1년 0개월 2일
	 * 기준일자FROM : 1996년 3월 1일, 기준일자TO : 1997년 3월 1일
	 * =&gt; 경과기간 : 1년 0개월 1일
	 * 
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * 
	 * @param from 기준일자FROM(YYYYMMDD)
	 * @param to 기준일자TO(YYYYMMDDD)
	 * @return int[] 경과년수, 경과월수, 경과일수
	 * </pre>
	 */
	public static final int[] diffDatesWithStart(String from, String to) throws ParseException {
		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");

		// GregorianCalendar fromCalendar = new GregorianCalendar();
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		fromCalendar.setTime(fromDate);
		fromCalendar.add(GregorianCalendar.DAY_OF_MONTH, -1);
		fromDate = fromCalendar.getTime();

		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");
		return diffDates(fromDate, toDate);
	}

	private static final Map<String, Map<String, int[]>> diffYearMonthDaysStrMap = new ConcurrentHashMap<String, Map<String, int[]>>();

	/**
	 * <pre>
	 * 두 일자간 경과기간 (총년수, 총월수, 총일수) - 한편포함
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * 
	 * </pre>
	 */
	public static final int[] diffYearMonthDays(String from, String to) throws ParseException {
		Map<String, int[]> map = diffYearMonthDaysStrMap.get(from);
		if (map != null) {
			int[] diff = map.get(to);
			if (diff != null){
				return diff;
			}
		}

		int[] retValue = new int[3];

		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		retValue[0] = DateTimeUtil.diffYears(fromDate, toDate);

		if (DateTimeUtil.diffDays(fromDate, toDate) >= 0){
			retValue[1] = DateTimeUtil.diffMonths(fromDate, toDate);
		}else{
			retValue[1] = DateTimeUtil.diffMonths(toDate, fromDate) * -1;
		}
		retValue[2] = DateTimeUtil.diffDays(fromDate, toDate);

		if (map == null){
			map = new ConcurrentHashMap<String, int[]>();
		}
		map.put(to, retValue);
		diffYearMonthDaysStrMap.put(from, map);
		return retValue;

	}

	private static final Map<Integer, Map<Integer, int[]>> diffYearMonthDaysIntMap = new ConcurrentHashMap<Integer, Map<Integer, int[]>>();

	public static final int[] diffYearMonthDays(int from, int to) throws ParseException {
		Map<Integer, int[]> map = diffYearMonthDaysIntMap.get(from);
		if (map != null) {
			int[] diff = map.get(to);
			if (diff != null) {
				return diff;
			}
		}

		int[] retValue = new int[3];

		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		retValue[0] = DateTimeUtil.diffYears(fromDate, toDate);

		if (DateTimeUtil.diffDays(fromDate, toDate) >= 0){
			retValue[1] = DateTimeUtil.diffMonths(fromDate, toDate);
		}else{
			retValue[1] = DateTimeUtil.diffMonths(toDate, fromDate) * -1;
		}
		retValue[2] = DateTimeUtil.diffDays(fromDate, toDate);

		if (map == null){
			map = new ConcurrentHashMap<Integer, int[]>();
		}
		map.put(to, retValue);
		diffYearMonthDaysIntMap.put(from, map);
		return retValue;

	}

	/**
	 * <pre>
	 * 두 일자간 경과기간 (총년수, 총월수, 총일수) - 양편포함
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * 
	 * </pre>
	 */
	public static final int[] diffYearMonthDayWithStart(String from, String to) throws ParseException {
		int[] retValue = new int[3];

		Date fromDate = DateTimeUtil.getDate(from, "yyyyMMdd");
		Date toDate = DateTimeUtil.getDate(to, "yyyyMMdd");

		// GregorianCalendar fromCalendar = new GregorianCalendar();
		GregorianCalendar fromCalendar = DateTimeUtil.getGregorianCalendar();
		fromCalendar.setTime(fromDate);
		fromCalendar.add(GregorianCalendar.DAY_OF_MONTH, -1);
		fromDate = fromCalendar.getTime();

		retValue[0] = DateTimeUtil.diffYears(fromDate, toDate);
		retValue[1] = DateTimeUtil.diffMonths(fromDate, toDate);
		retValue[2] = DateTimeUtil.diffDays(fromDate, toDate);
		return retValue;
	}

	/**
	 * <pre>
	 * 달 표현 문자열(yearMonthString)에 대해 해당 달의 마지막 날짜를 반환.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param yearMonthString 달 표현 문자열(yyyyMM)
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 달
	 * @return int 달 표현 형식이 맞고, 존재하는 달 일때 해당 달의 마지막 날짜
	 * </pre>
	 */
	public static final String lastDayOfMonth(String yearMonthString) throws java.text.ParseException {
		if (yearMonthString.length() == 8){
			yearMonthString = yearMonthString.substring(0, 6);
		}
		return lastDayOfMonth(yearMonthString, "yyyyMM");
	}

	/**
	 * <pre>
	 * 달 표현 포맷(format)과 달 표현 문자열(yearMonthString)에 대해 해당 달의 마지막 날짜를 반환.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @param yearMonthString 달 표현 문자열(yyyyMM)
	 * @param format 달 표현 포맷 예를들어, &quot;yyyy/MM&quot;.
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 달
	 * @return int 달 표현 형식이 맞고, 존재하는 달 일때 해당 달의 마지막 날짜
	 * </pre>
	 */
	public static final String lastDayOfMonth(String yearMonthString, String format) throws java.text.ParseException {
		java.util.Date date = getDate(yearMonthString, format);
		return lastDayOfMonth(date);
	}

	/**
	 * <pre>
	 * 입력 Date에 대해 해당 달의 마지막 날짜를 반환.
	 * 
	 * @param date Date
	 * @throws java.text.ParseException 형식이 잘못 되었거나 존재하지 않는 달
	 * @return int 달 표현 형식이 맞고, 존재하는 달 일때 해당 달의 마지막 날짜
	 * </pre>
	 */
	public static final String lastDayOfMonth(Date date) {
		int year = getNumberByPattern(date.getTime(), "yyyy");
		int month = getNumberByPattern(date.getTime(), "MM");
		return lastDay(year, month);
	}

	/**
	 * <pre>
	 * 년(year)와 달(month)의 입력에 대해 해당 달의 마지막 날짜를 반환.
	 * 
	 * @param year 년도
	 * @param month 달
	 * @return int 해당 달의 마지막 날짜
	 * </pre>
	 */
	public static final String lastDay(int year, int month) {
		int day = 0;
		// GregorianCalendar newDate = new GregorianCalendar();
		GregorianCalendar newDate = DateTimeUtil.getGregorianCalendar();
		newDate.set(GregorianCalendar.YEAR, year);
		newDate.set(GregorianCalendar.MONTH, month - 1);
		newDate.set(GregorianCalendar.DAY_OF_MONTH, 1);

		day = newDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		return year + "" + StringUtil.lpad(month, 2) + "" + day;
	}

	/**
	 * <pre>
	 * java.sql.Timestamp를 java.util.Date 형태로 변환하여 반환.
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return java.util.Date
	 * @throws java.text.ParseException
	 * </pre>
	 */
	public static final java.util.Date timestampToDate(java.sql.Timestamp timestamp) throws java.text.ParseException {
		if (timestamp != null) {
			return new java.util.Date(timestamp.getTime());
		} else {
			return null;
		}
	}

	/**
	 * <pre>
	 * java.util.Date를 java.sql.Timestamp형태로 변환하여 반환.
	 * 
	 * @param date java.util.Date
	 * @return java.sql.Timestamp
	 * @throws java.text.ParseException
	 * </pre>
	 */
	public static final java.sql.Timestamp dateToTimestamp(java.util.Date date) throws java.text.ParseException {
		if (date != null) {
			return new java.sql.Timestamp(date.getTime());
		} else {
			return null;
		}
	}

	/**
	 * <pre>
	 * java.util.Date를 java.sql.Timestamp형태로 변환하여 반환.
	 * 
	 * @return java.lang.String 변환된 현재시간
	 * </pre>
	 */
	public static final String getCurrentTimeMillis() {
		return DateTimeUtil.getFormatString("yyyyMMddHHmmss.SSS");
	}

	/**
	 * <pre>
	 * 현재날짜를 &quot;yyyyMMdd&quot; 포멧으로 반환.
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @return java.lang.String 변환된 현재날짜
	 * </pre>
	 */
	public static final String getToDay() {
		return DateTimeUtil.getFormatString("yyyyMMdd");
	}

	/**
	 * <pre>
	 * 현재날짜를 &quot;yyyy-MM-dd&quot; 포멧으로 반환.(from DBIO)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @return java.lang.String 변환된 현재날짜
	 * </pre>
	 */
	public static final String getCurrentDate() {
		// SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
		// return datePattern.format(new Date());
		return DateTimeUtil.getFormatString("yyyy-MM-dd");
	}

	/**
	 * <pre>
	 * 현재시간를 &quot;yyyy-MM-dd HH:mm:ss&quot; 포멧으로 반환.(from DBIO)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @return java.lang.String 변환된 현재시간
	 * </pre>
	 */
	public static final String getCurrentDateTime() {
		// SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// return datePattern.format(new Date());
		return DateTimeUtil.getFormatString("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * <pre>
	 * 현재시간를 &quot;yyyy-MM-dd HH:mm:ss.SSS&quot; 포멧으로 반환.(from DBIO)
	 * (공통프로그램사용가이드(날짜) 참조)
	 * 
	 * @return java.lang.String 변환된 현재시간
	 * </pre>
	 */
	public static final String getCurrentTimestamp() {
		// SimpleDateFormat timestampPattern = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss.SSS");
		// return timestampPattern.format(new Date());
		return DateTimeUtil.getFormatString("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * <pre>
	 * 현재시간만반환.(from DBIO)
	 * 
	 * @return java.lang.String 변환된 현재시간
	 * </pre>
	 * 2015-10-26 추가
	 */
	public static final String getCurrentTime() {
		// SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// return datePattern.format(new Date());
		return DateTimeUtil.getFormatString("HHmmss");
	}

	/**
	 * <pre>
	 * 윤년인지를 판단한다.
	 * 
	 * @param year yyyy 형태의 문자형 년도
	 * @return boolean 윤년유무를 리턴(윤년:true, 평년:false)
	 * @throws java.text.NumberFormatException
	 * </pre>
	 */
	public static final boolean isLeapYear(String year) throws NumberFormatException {
		return isLeapYear(Integer.parseInt(year));
	}

	/**
	 * <pre>
	 * 윤년인지를 판단한다.
	 * 
	 * @param year yyyy 형태의 정수형 년도
	 * @return boolean 윤년유무를 리턴(윤년:true, 평년:false)
	 * @throws java.text.NumberFormatException
	 * </pre>
	 */
	public static final boolean isLeapYear(int year) {
		GregorianCalendar gregorianCal = new GregorianCalendar();
		return gregorianCal.isLeapYear(year);
	}

	/**
	 * 월을 넘겨받으면 분기를 반환
	 * 
	 * @return String 분기
	 */
	public static final String getQtr() {
		return getQtr(getFormatString("yyyyMM"));
	}

	/**
	 * 월을 넘겨받으면 분기를 반환
	 * 
	 * @param yearMonth
	 *            월
	 * @return String 분기
	 */
	public static final String getQtr(String yearMonth) {

		String rtnStr = "";
		if (isValid(yearMonth, "yyyyMM") == false){
			return rtnStr;
		}
		String month = yearMonth.substring(4, 6);

		if (StringUtil.equalsOr(month, "01|02|03")) {
			rtnStr = "1";
		} else if (StringUtil.equalsOr(month, "04|05|06")) {
			rtnStr = "2";
		} else if (StringUtil.equalsOr(month, "07|08|09")) {
			rtnStr = "3";
		} else if (StringUtil.equalsOr(month, "10|11|12")) {
			rtnStr = "4";
		}

		return rtnStr;
	}

	/**
	 * 분기를 넘겨받으면 해당하는 월을 String 배열로 반환
	 * 
	 * @param qtr
	 *            분기
	 * @return String [] 월
	 */
	public static final String[] getQtr2Str(String qtr) {
		return getQtr2Str(qtr, null);
	}

	/**
	 * 분기를 넘겨받으면 해당하는 년월을 String 배열로 반환
	 * 
	 * @param qtr
	 *            분기
	 * @return String [] 년월
	 */
	public static final String[] getQtr2Str(String qtr, String year) {

		// String rltarr[] = (String[])null;
		String[] rltarr = new String[3];
		if (StringUtil.isBlank(qtr) || !StringUtil.equalsOr(qtr, "1|2|3|4")) {
			return null;
		}

		if (StringUtil.isBlank(year)) {
			if ("1".equals(qtr)) {
				rltarr[0] = "01";
				rltarr[1] = "02";
				rltarr[2] = "03";
			} else if ("2".equals(qtr)) {
				rltarr[0] = "04";
				rltarr[1] = "05";
				rltarr[2] = "06";
			} else if ("3".equals(qtr)) {
				rltarr[0] = "07";
				rltarr[1] = "08";
				rltarr[2] = "09";
			} else if ("4".equals(qtr)) {
				rltarr[0] = "10";
				rltarr[1] = "11";
				rltarr[2] = "12";
			}
		} else {
			if (year.length() != 4)
				return null;

			if ("1".equals(qtr)) {
				rltarr[0] = year + "01";
				rltarr[1] = year + "02";
				rltarr[2] = year + "03";
			} else if ("2".equals(qtr)) {
				rltarr[0] = year + "04";
				rltarr[1] = year + "05";
				rltarr[2] = year + "06";
			} else if ("3".equals(qtr)) {
				rltarr[0] = year + "07";
				rltarr[1] = year + "08";
				rltarr[2] = year + "09";
			} else if ("4".equals(qtr)) {
				rltarr[0] = year + "10";
				rltarr[1] = year + "11";
				rltarr[2] = year + "12";
			}
		}

		return rltarr;
	}

	/**
	 * 현재년월에 대한 회계년도를 반환
	 * 
	 * @return String 회계년도
	 */
	public static final String getFYear() {
		return getFYear(getFormatString("yyyyMM"));
	}

	/**
	 * 해당년월에 대한 회계년도를 반환
	 * 
	 * @param yearMonth
	 *            해당년월
	 * @return String 회계년도
	 */
	public static final String getFYear(String yearMonth) {
		String rtnStr = "";
		if (isValid(yearMonth, "yyyyMM") == false){
			return rtnStr;
		}
		String year = yearMonth.substring(0, 4);
		String month = yearMonth.substring(4, 6);

		if (StringUtil.equalsOr(month, "01|02|03")) {
			rtnStr = (Integer.parseInt(year) - 1) + "";
		} else if (StringUtil.equalsOr(month, "04|05|06")) {
			rtnStr = year;
		} else if (StringUtil.equalsOr(month, "07|08|09")) {
			rtnStr = year;
		} else if (StringUtil.equalsOr(month, "10|11|12")) {
			rtnStr = year;
		}

		return rtnStr;
	}

	/**
	 * 생년월일로 만나이를 구하기(현재일자 기준)
	 * 
	 * @param strBirth
	 *            생년월일(yyyyMMdd)
	 * @return String 만나이
	 */
	public static final String getFullAge(String strBirth) {
		return getFullAge(strBirth, getFormatString("yyyyMMdd"));
	}

	/**
	 * 생년월일로 만나이를 구하기(기준일자 기준)
	 * 
	 * @param strBirth
	 *            생년월일(yyyyMMdd)
	 * @param strApplyDate
	 *            기준년월(yyyyMMdd)
	 * @return String 만나이
	 */
	public static final String getFullAge(String strBirth, String strApplyDate) {

		if (DateTimeUtil.isValid(strBirth) == false || DateTimeUtil.isValid(strApplyDate) == false
				|| Integer.parseInt(strApplyDate) < Integer.parseInt(strBirth)) {
			return "";
		}

		String year = strApplyDate.substring(0, 4);// 기준년도
		String month = strApplyDate.substring(4, 6);// 기준월
		String day = strApplyDate.substring(6, 8);// 기준일

		// 현재 연도 - 태어난 연도
		int ageYear = Integer.parseInt(year) - Integer.parseInt(strBirth.substring(0, 4));
		// 태어난 달 - 현재 달
		int ageMonth = Integer.parseInt(strBirth.substring(4, 6)) - Integer.parseInt(month);
		// 태어난 일자 - 현재 일자
		int ageDay = Integer.parseInt(strBirth.substring(6, 8)) - Integer.parseInt(day);

		// 만나이를 구한다.
		int fullAge = 0;
		if (ageMonth < 0) {
			fullAge = ageYear;
		} else if (ageMonth == 0) {
			if (ageDay <= 0){
				fullAge = ageYear;
			}else{
				fullAge = ageYear - 1;
			}
		} else {
			fullAge = ageYear - 1;
		}

		// //("내 만 나이는 : " + strApplyDate.substring(0,4));

		return fullAge + "";
	}

	// final static Calendar DEFAULT_CALENDAR = new GregorianCalendar();
	final static String CALENDAR_YEAR = "yy";
	final static String CALENDAR_MONTH = "MM";
	final static String[] CALENDAR_FORMATS = { "yyyy", "yy", "MM", "dd", "HH", "mm", "ss", "SSS" };
	final static String[] CALENDAR_FORMATTER_FORMATS = { "%1$tY", "%1$ty", "%1$tm", "%1$td", "%1$tH", "%1$tM", "%1$tS",
			"%1$tL" };
	final static int[] CALENDAR_FIELDS = { Calendar.YEAR, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
			Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND };
	final static int LENGTH_CALENDAR_FORMATS = CALENDAR_FORMATS.length;

	private static final Map<String, Date> yyyyMMStrMap = new ConcurrentHashMap<String, Date>();
	private static final Map<String, Date> yyyy_MMStrMap = new ConcurrentHashMap<String, Date>();
	private static final Map<String, Date> yyyyMMddStrMap = new ConcurrentHashMap<String, Date>();
	private static final Map<String, Date> yyyy_MM_ddStrMap = new ConcurrentHashMap<String, Date>();
	private static final Map<String, Date> yyyycMMcddStrMap = new ConcurrentHashMap<String, Date>();

	private static final Map<Integer, Date> yyyyMMIntMap = new ConcurrentHashMap<Integer, Date>();
	private static final Map<Integer, Date> yyyyMMddIntMap = new ConcurrentHashMap<Integer, Date>();

	static {
		GregorianCalendar fromCal = DateTimeUtil.getGregorianCalendar();
		GregorianCalendar toCal = DateTimeUtil.getGregorianCalendar();
		fromCal.roll(GregorianCalendar.YEAR, -100); // 오늘 - 100년 미리 계산

		// "-100년" 오늘 "+100년" 미리 계산
		toCal.set(GregorianCalendar.YEAR, fromCal.get(GregorianCalendar.YEAR) + 200);
		toCal.set(GregorianCalendar.MONTH, 11);
		toCal.set(GregorianCalendar.DAY_OF_MONTH, 31);

		while (fromCal.getTimeInMillis() < toCal.getTimeInMillis()) {
			fromCal.add(GregorianCalendar.DAY_OF_MONTH, 1);
			Date dt = fromCal.getTime();

			int y = fromCal.get(Calendar.YEAR);
			int m = fromCal.get(Calendar.MONTH) + 1;
			int d = fromCal.get(Calendar.DAY_OF_MONTH);
			String yyyyMM = "" + y + ((m >= 10) ? m : "0" + m); // yyyyMM
			String yyyy_MM = "" + y + "-" + ((m >= 10) ? m : "0" + m); // yyyy-MM
			String yyyyMMdd = "" + y + ((m >= 10) ? m : "0" + m) + ((d >= 10) ? d : "0" + d); // yyyyMMdd
			String yyyy_MM_dd = "" + y + "-" + ((m >= 10) ? m : "0" + m) + "-" + ((d >= 10) ? d : "0" + d); // yyyy-MM-dd
			String yyyycMMcdd = "" + y + "." + ((m >= 10) ? m : "0" + m) + "." + ((d >= 10) ? d : "0" + d); // yyyy.MM.dd

			yyyyMMStrMap.put(yyyyMM, dt);
			yyyy_MMStrMap.put(yyyy_MM, dt);
			yyyyMMddStrMap.put(yyyyMMdd, dt);
			yyyy_MM_ddStrMap.put(yyyy_MM_dd, dt);
			yyyycMMcddStrMap.put(yyyycMMcdd, dt);

			yyyyMMIntMap.put(Integer.valueOf(yyyyMM), dt);
			yyyyMMddIntMap.put(Integer.valueOf(yyyyMMdd), dt);
		}

		fromCal = DateTimeUtil.getGregorianCalendar();
		toCal = DateTimeUtil.getGregorianCalendar();
		toCal.set(GregorianCalendar.YEAR, fromCal.get(GregorianCalendar.YEAR) + 100);
		while (fromCal.getTimeInMillis() < toCal.getTimeInMillis()) {
			int y = fromCal.get(Calendar.YEAR);
			int m = fromCal.get(Calendar.MONTH) + 1;
			int yyyyMMdd = y * 10000 + m * 100 + 1;
			String yyyyMMddStr = String.valueOf(yyyyMMdd);

			for (int i = 0; i < 100; i++) {
				try {
					addYearsStr(yyyyMMdd, i);
					addYearsStr(yyyyMMddStr, i);
				} catch (ParseException e) {
					e.getStackTrace();
				}
			}

			fromCal.add(GregorianCalendar.YEAR, 1);
		}

		fromCal = DateTimeUtil.getGregorianCalendar();
		toCal = DateTimeUtil.getGregorianCalendar();
		toCal.set(GregorianCalendar.YEAR, fromCal.get(GregorianCalendar.YEAR) + 100);
		while (fromCal.getTimeInMillis() < toCal.getTimeInMillis()) {
			GregorianCalendar tmpCal = (GregorianCalendar) fromCal.clone();
			tmpCal.add(Calendar.YEAR, 1);

			int y = fromCal.get(Calendar.YEAR);
			int m = fromCal.get(Calendar.MONTH) + 1;
			int yyyyMMdd = y * 10000 + m * 100 + 1;
			String yyyyMMddStr = String.valueOf(yyyyMMdd);

			y = tmpCal.get(Calendar.YEAR);
			m = tmpCal.get(Calendar.MONTH) + 1;
			int to = y * 10000 + m * 100 + 1;
			String toStr = String.valueOf(to);

			try {
				diffYearMonthDays(yyyyMMdd, to);
				diffYearMonthDays(yyyyMMddStr, toStr);
			} catch (ParseException e) {
				e.getStackTrace();
			}

			fromCal.add(GregorianCalendar.MONTH, 1);
		}
	}

	public static final Date parse(int dateInt, String format) throws ParseException {
		if (format == null || format.length() < 1) {
			throw new ParseException("Illegal pattern character. pattern=" + format, 0);
		}

		if ("yyyyMM".equals(format)) {
			int t = (dateInt > 999999) ? dateInt / 100 : dateInt;
			Date d = yyyyMMIntMap.get(t);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, t / 100);
				c.set(Calendar.MONTH, (t % 100) - 1);
				c.set(Calendar.DAY_OF_MONTH, 1);
				d = c.getTime();
				yyyyMMIntMap.put(t, d);
			}
			return (Date) d.clone();
		} else if ("yyyyMMdd".equals(format)) {
			// yyyyMMdd 타입의 int 값이상인지 여부 확인
			if (dateInt < 10000000)
				throw new ParseException("Illegal date int value[yyyyMMdd]. dateInt=" + dateInt, 0);

			int t = dateInt;
			Date d = yyyyMMddIntMap.get(t);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, t / 10000);
				c.set(Calendar.MONTH, (t % 10000) / 100 - 1);
				c.set(Calendar.DAY_OF_MONTH, t % 100);
				d = c.getTime();
				yyyyMMddIntMap.put(t, d);
			}
			return (Date) d.clone();
		} else{
			throw new ParseException("Unsupported pattern character. pattern=" + format, 0);
		}
	}

	public static final Date parse(String dateString, String format) throws ParseException {
		if (format == null || format.length() < 1) {
			throw new ParseException("Illegal pattern character. pattern=" + format, 0);
		}

		if ("yyyyMM".equals(format) && dateString.length() == 6) {
			Date d = yyyyMMStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(dateString.substring(4, 6)) - 1);
				c.set(Calendar.DAY_OF_MONTH, 1);
				d = c.getTime();
				yyyyMMStrMap.put(dateString, d);
			}
			return (Date) d.clone();
		} else if ("yyyy-MM".equals(format) && dateString.length() == 7) {
			Date d = yyyy_MMStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(dateString.substring(5, 7)) - 1);
				c.set(Calendar.DAY_OF_MONTH, 1);
				d = c.getTime();
				yyyy_MMStrMap.put(dateString, d);
			}
			return (Date) d.clone();
		} else if ("yyyyMMdd".equals(format) && dateString.length() == 8) {
			Date d = yyyyMMddStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(dateString.substring(4, 6)) - 1);
				c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(6, 8)));
				d = c.getTime();
				yyyyMMddStrMap.put(dateString, d);
			}
			return (Date) d.clone();
		} else if ("yyyy-MM-dd".equals(format) && dateString.length() == 10) {
			Date d = yyyy_MM_ddStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(dateString.substring(5, 7)) - 1);
				c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(8, 10)));
				d = c.getTime();
				yyyy_MM_ddStrMap.put(dateString, d);
			}
			return (Date) d.clone();
		} else if ("yyyy.MM.dd".equals(format) && dateString.length() == 10) {
			Date d = yyyycMMcddStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
				c.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(dateString.substring(5, 7)) - 1);
				c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(8, 10)));
				d = c.getTime();
				yyyycMMcddStrMap.put(dateString, d);
			}
			return (Date) d.clone();
		}

		GregorianCalendar c = DateTimeUtil.getGregorianCalendar();

		String f = null;
		int index = 0;
		String v = null;
		int iv = 0;
		boolean setting = false;
		for (int i = 0; i < LENGTH_CALENDAR_FORMATS; i++) {
			f = CALENDAR_FORMATS[i];

			// format 이 yyyyMM 일 경우,
			// dd값이 없어서 현재일이 29일 경우, 29일이 없는 년월(예:200902)을 세팅하려 할 때
			// 발생하는 문제를 해결하기 위해 1일로 초기화 함
			if (i == 3 && format.indexOf(f) == -1){
				c.set(CALENDAR_FIELDS[i], 1);
			}
			while (true) {
				if ((index = format.indexOf(f)) > -1) {
					try {
						v = dateString.substring(index, index + f.length());
						iv = Integer.parseInt(v);
					} catch (Exception e) {
						throw new ParseException("Illegal pattern/value character. pattern=" + f + " value=" + v, 0);
					}
					format = format.substring(0, index) + format.substring(index + f.length());
					dateString = dateString.substring(0, index) + dateString.substring(index + f.length());
					if (CALENDAR_MONTH.equals(f)) {
						c.set(CALENDAR_FIELDS[i], iv - 1);
					} else if (CALENDAR_YEAR.equals(f) && iv == 0) {// yy포맷일경우
																	// 00년일때
						c.set(CALENDAR_FIELDS[i], 2000);
					} else {
						c.set(CALENDAR_FIELDS[i], iv);
					}
					if (!setting) {
						setting = true;
					}
				} else {
					break;
				}
			}
		}
		if (!setting) {
			throw new ParseException("Format is invalid", 0);
		}
		return c.getTime();
	}

	private static final int formatInt(Date date, String format) throws ParseException {
		if (format == null || format.length() < 1) {
			throw new ParseException("Format is null", 0);
		}

		if ("yyyy".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			return y;
		} else if ("yyyyMM".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			return y * 100 + m;
		} else if ("yyyyMMdd".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);
			return y * 10000 + m * 100 + d;
		} else
			throw new ParseException("Unsupported pattern character. pattern=" + format, 0);
	}

	private final static char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static final String format(Date date, String format) throws ParseException {
		if (format == null || format.length() < 1) {
			throw new ParseException("Format is null", 0);
		}

		if ("yyyy".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			return String.valueOf(y);
		} else if ("yyyyMM".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			return String.valueOf(y * 100 + m);
		} else if ("yyyy-MM".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;

			char[] ret = new char[7];
			ret[0] = chars[y / 1000];
			ret[1] = chars[(y % 1000) / 100];
			ret[2] = chars[(y % 100) / 10];
			ret[3] = chars[y % 10];
			ret[4] = '-';
			ret[5] = chars[m / 10];
			ret[6] = chars[m % 10];
			return String.valueOf(ret);
		} else if ("yyyyMMdd".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);
			return String.valueOf(y * 10000 + m * 100 + d);
		} else if ("yyyy-MM-dd".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);

			char[] ret = new char[10];
			ret[0] = chars[y / 1000];
			ret[1] = chars[(y % 1000) / 100];
			ret[2] = chars[(y % 100) / 10];
			ret[3] = chars[y % 10];
			ret[4] = '-';
			ret[5] = chars[m / 10];
			ret[6] = chars[m % 10];
			ret[7] = '-';
			ret[8] = chars[d / 10];
			ret[9] = chars[d % 10];
			return String.valueOf(ret);
		} else if ("yyyy.MM.dd".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);

			char[] ret = new char[10];
			ret[0] = chars[y / 1000];
			ret[1] = chars[(y % 1000) / 100];
			ret[2] = chars[(y % 100) / 10];
			ret[3] = chars[y % 10];
			ret[4] = '.';
			ret[5] = chars[m / 10];
			ret[6] = chars[m % 10];
			ret[7] = '.';
			ret[8] = chars[d / 10];
			ret[9] = chars[d % 10];
			return String.valueOf(ret);
		} else if ("yyyy-MM-dd HH:mm:ss.SSS".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);
			int h = c.get(Calendar.HOUR_OF_DAY);
			int mi = c.get(Calendar.MINUTE);
			int s = c.get(Calendar.SECOND);
			int ss = c.get(Calendar.MILLISECOND);

			char[] ret = new char[23];
			ret[0] = chars[y / 1000];
			ret[1] = chars[(y % 1000) / 100];
			ret[2] = chars[(y % 100) / 10];
			ret[3] = chars[y % 10];
			ret[4] = '-';
			ret[5] = chars[m / 10];
			ret[6] = chars[m % 10];
			ret[7] = '-';
			ret[8] = chars[d / 10];
			ret[9] = chars[d % 10];
			ret[10] = ' ';
			ret[11] = chars[h / 10];
			ret[12] = chars[h % 10];
			ret[13] = ':';
			ret[14] = chars[mi / 10];
			ret[15] = chars[mi % 10];
			ret[16] = ':';
			ret[17] = chars[s / 10];
			ret[18] = chars[s % 10];
			ret[19] = '.';
			ret[20] = chars[ss / 100];
			ret[21] = chars[(ss % 100) / 10];
			ret[22] = chars[ss % 10];
			return String.valueOf(ret);
		} else if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);
			int h = c.get(Calendar.HOUR_OF_DAY);
			int mi = c.get(Calendar.MINUTE);
			int s = c.get(Calendar.SECOND);

			char[] ret = new char[23];
			ret[0] = chars[y / 1000];
			ret[1] = chars[(y % 1000) / 100];
			ret[2] = chars[(y % 100) / 10];
			ret[3] = chars[y % 10];
			ret[4] = '-';
			ret[5] = chars[m / 10];
			ret[6] = chars[m % 10];
			ret[7] = '-';
			ret[8] = chars[d / 10];
			ret[9] = chars[d % 10];
			ret[10] = ' ';
			ret[11] = chars[h / 10];
			ret[12] = chars[h % 10];
			ret[13] = ':';
			ret[14] = chars[mi / 10];
			ret[15] = chars[mi % 10];
			ret[16] = ':';
			ret[17] = chars[s / 10];
			ret[18] = chars[s % 10];
			return String.valueOf(ret).trim();
		} else if ("HHmmss".equals(format)) {
			GregorianCalendar c = DateTimeUtil.getGregorianCalendar();
			c.setTime(date);
			int h = c.get(Calendar.HOUR_OF_DAY);
			int mi = c.get(Calendar.MINUTE);
			int s = c.get(Calendar.SECOND);
			// int tmp = h * 10000 + mi * 100 + s;
			return "" + ((h >= 10) ? h : "0" + h) + ((mi >= 10) ? mi : "0" + mi) + ((s >= 10) ? s : "0" + s);

			// if(tmp < 100000){
			// return "0"+String.valueOf(tmp);
			// }else{
			// return String.valueOf(tmp);
			// }

		}

		String f = null;
		int index = 0;
		boolean setting = false;
		for (int i = 0; i < LENGTH_CALENDAR_FORMATS; i++) {
			f = CALENDAR_FORMATS[i];

			while (true) {
				if ((index = format.indexOf(f)) > -1) {
					format = format.substring(0, index) + CALENDAR_FORMATTER_FORMATS[i]
							+ format.substring(index + f.length());
					if (!setting) {
						setting = true;
					}
				} else {
					break;
				}
			}
		}
		if (!setting) {
			throw new ParseException("Format is invalid", 0);
		}
		return String.format(format, date);
	}

	static long lastDate;
	static long lastDateNanos;
	static long nanos;
	static {
		lastDate = System.currentTimeMillis();
		nanos = System.nanoTime();
		lastDateNanos = lastDate * 1000000;
	}

	private static long currentTimeNanos() {
		return lastDateNanos + (System.nanoTime() - nanos);
	}

	private static String elapseNanos(long currTime) {
		String str = String.valueOf(currTime);
		// msec단위자리부터 넣기위해 9자리를 추출하여 그중 6자리를 사용함
		return (str.substring(str.length() - 9, str.length() - 3)); // 필요한 자리수를
																	// 뺌.
	}

	/**
	 * <pre>
	 * 현재시간를 &quot;yyyy-MM-dd HH:mm:ss.FFFFFF&quot; 포멧으로 나노초 까지 반환.
	 * 
	 * @return java.lang.String 변환된 현재시간(나노초)
	 * </pre>
	 */
	public static synchronized String getCurrentTimeNanos() {
		return DateTimeUtil.getFormatString("yyyy-MM-dd HH:mm:ss.") + elapseNanos(currentTimeNanos());
	}
	
	public static String getSysDateTime(int paramInt) {
		Date localDate = Calendar.getInstance().getTime();

		switch (paramInt) {
		case 0:
			return noneDFTF.format(localDate);
		case 1:
			return dashDFTF.format(localDate);
		case 2:
			return korDFTF.format(localDate);
		}
		return "";
	}
		
	/**
	 * 사업년도를 받고 1년동안의 기간이 끝나는날을 리턴
	 * 예 : 입력값 20160101 리턴값 20161231
	 * @param stdDt java.lang.String
	 * @return String
	 */
	public static String oneYear(String stdDt) throws ParseException {
		int year = Integer.parseInt(stdDt.substring(0, 4));
		int month = Integer.parseInt(stdDt.substring(4, 6)) - 1;
		int day = Integer.parseInt(stdDt.substring(6, 8));
		SimpleDateFormat oneDate = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		GregorianCalendar date = DateTimeUtil.getGregorianCalendar();
		date.set(year, month, day);
		date.add(Calendar.YEAR, 1);
		date.add(Calendar.DATE, -1);
		return  oneDate.format(date.getTime());
	}
	
	
}
