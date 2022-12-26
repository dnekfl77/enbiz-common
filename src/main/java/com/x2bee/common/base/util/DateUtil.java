package com.x2bee.common.base.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x2bee.common.base.constant.X2Constants;

/**
 * Description : 클래스 설명<p>
 * CreatedDate : 2011. 11. 16. <p>
 * UpdatedDate : 2011. 11. 16. <p>
 * UpdatedBy : freeman <p>
 * Update Desc : <p>
 *
 * @author : freeman <p>
 * @since : 1.0 <p>
 */
public class DateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	private DateUtil() {
	}

	/**
	 * <pre>
	 * 1. 개요 : 오늘 날짜 받아오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : getToday
	 * @return
	 */
	public static String getToday(){
	    return getToday(X2Constants.YYYYMMDD_WITH_DASH_DELIM);
	}

	/**
	 * 넘어온 포멧 형태로 현재 날짜 출력
	 * @param format - 출력받을 날짜 포멧
	 * @return String - 현재 날짜
	 */
	public static String getToday(String format) {
		if(format == null){
			throw new IllegalArgumentException();
		}

        return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * <pre>
	 * 1. 개요 : 오늘부터 넘어온 날짜료 계산된 날짜 리턴
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : getBetweenToday
	 * @param term
	 * @return
	 */
	public static String getBetweenToday(int term){
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DAY_OF_YEAR, term);

	    return format(cal.getTime(), X2Constants.YYYYMMDD_WITH_DASH_DELIM);
	}

	/**
	 * 현재날짜의 월 가져오기
	 * @return
	 */
	public static int getThisMonth(){
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 현재날짜의 일 가져오기
	 * @return
	 */
	public static int getThisDay(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 원하는 날짜형식으로 오늘 날짜를 리턴한다.
	 *
	 * @see java.text.SimpleDateFormat
	 * @param string
	 *            패턴
	 * @return string 패턴화 된 날짜 문자열
	 */
	public static String today(String pattern) {
		if(pattern == null){
			throw new IllegalArgumentException();
		}

		return format(new Date(), pattern);
	}

	/**
     * 현재 날짜를 기준으로 원하는 시점의 날짜를 구함  Default Date Format "yyyyMMdd"
     * @param field      Calendar Field - 일: Calendar.DATE, 주: Calendar.WEEK_OF_MONTH, 월: Calendar.MONTH,  년: Calendar.YEAR
     * @param amount   원하는 날짜 시점 (10일 후를 원하면 10, 10일 전을 원하면 -10)
     * @return 날짜 String
     */
	public static String addDateFromNow(int field, int amount) {
        return addDateFromNow(field, amount, "yyyyMMdd");
    }

	/**
     * 현재 날짜를 기준으로 원하는 시점의 날짜를 구함
     * @param field      Calendar Field - 일: Calendar.DATE, 주: Calendar.WEEK_OF_MONTH, 월: Calendar.MONTH,  년: Calendar.YEAR
     * @param amount     원하는 날짜 시점 (10일 후를 원하면 10, 10일 전을 원하면 -10)
     * @param formatstr  날짜 format
     * @return 날짜 String
     */
	public static String addDateFromNow(int field, int amount, String formatstr) {
        Calendar cal = Calendar.getInstance();

        cal.add(field, amount);
        Date date = cal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat(formatstr);
        return formatter.format(date);

    }

	/**
	 * 주어진 Date를 pattern화 된 문자열로 반환한다.
	 *
	 * @param date
	 *            패턴화할 날짜
	 * @param pattern
	 *            string 패턴
	 * @return string 패턴화된 날짜 문자열
	 */
	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date.getTime());
	}

	/**
	 * 현재날짜의 년도 가져오기
	 * @return
	 */
	public static int getThisYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @param datestr
	 * @return
	 */
	public static java.sql.Timestamp stamp(String datestr, String pattern){
		return new java.sql.Timestamp(parse(datestr, pattern).getTime());
	}

	/**
	 * pattern형식으로 포맷된 날짜 문자열을 java.util.Date 형태로 반환한다.
	 *
	 * @param s
	 *            date string you want to check.
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return date java.util.Date
	 */
	public static Date parse(String str, String pattern) {
		if (str == null) {
			throw new IllegalArgumentException("date string to check is null");
		}

		if (pattern == null) {
			throw new IllegalArgumentException("format string to check date is null");
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
				java.util.Locale.KOREA);
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException(" wrong date:\"" + str
					+ "\" with format \"" + pattern + "\"");
		}

	}

	/**
	 * <pre>
	 * 1. 개요 : 현재월의 첫날
	 * 2. 처리내용 : 현재월의 첫날을 되돌려 준다.
	 * </pre>
	 * @Method Name : getStartOfTheMonth
	 * @return
	 */
	public static String getStartOfTheMonth(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    return sf.format(cal.getTime());
	}

    /**
     * 현재 시간이 이 시간에 해당하는지의 여부를 반환한다.
     * ex) 201006261800-201006271830
     * @param nonAvailableTimeStr
     * @return 현재 시간이 주어진 시간대에 포함되면 true, 주어진 시간대에 포함되지 않으면 false.
     */
	public static boolean isAvailableTime( String nonAvailableTimeStr ) {
       boolean result = false;
       SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmm" );
       try {
	       Date nonAvailableTime1 = sdf.parse( nonAvailableTimeStr.substring( 0, nonAvailableTimeStr.indexOf( "-" ) ) );
	       Date nonAvailableTime2 = sdf.parse( nonAvailableTimeStr.substring( nonAvailableTimeStr.indexOf( "-" ) + 1, nonAvailableTimeStr.length() ) );
	       Date now = new Date();
	       if ( now.after( nonAvailableTime1 ) && now.before( nonAvailableTime2 ) )	{
	           result = true;
	       }
       } catch (Exception e) {
    	   LOGGER.trace(e.getMessage(), e);
       }
       return result;
   }

	public static Timestamp parseValue(String value){
        Timestamp timestamp = parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DASH_DELIM.get());
        timestamp = timestamp == null ? parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DASH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, X2Constants.SIMPLE_DATE_FORMAT_YYYYMMDD.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithISODateFormat(value) : timestamp;

		return timestamp;
	}

	private static Timestamp parseValueWithTimestamp(String value, SimpleDateFormat simpleDateFormat){
		Timestamp timestamp = null;
		try{
			Date parsedDate = simpleDateFormat.parse(value);
			timestamp = new Timestamp(parsedDate.getTime());
		}catch(Exception e){
			LOGGER.trace(e.getMessage(),e);
		}

		return timestamp;
	}

	private static Timestamp parseValueWithISODateFormat(String value) {
		Timestamp timestamp = null;
		try {
			timestamp = new Timestamp(DatatypeConverter.parseDateTime(value).getTimeInMillis());
		} catch (Exception e) {
			LOGGER.trace(e.getMessage(), e);
		}

		return timestamp;
	}

}