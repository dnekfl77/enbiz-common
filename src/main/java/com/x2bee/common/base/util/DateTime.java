package com.x2bee.common.base.util;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 날짜관련 Util
 *
 * @author User
 *
 */
public class DateTime {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTime.class);

    //yyyyMMdd 포맷
    private static final String DATE_FORMAT_YYYYMM = "yyyyMM";
    //yyyyMMdd 포맷
    private static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    //yyyyMMdd 포맷
    private static final String DATE_FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    //yyyyMMdd 포맷
    private static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    //yyyyMMdd 포맷
    private static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private DateTime() {
    }

    /**
     * 일자를 사용자가 정의한 형식에 의해 체크한다
     *
     * @param s
     * @param format
     * @return boolean
     */
    public static boolean isValidDate(String s, String format) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
        java.util.Date date = null;
        try {
            date = formatter.parse(s);
        } catch (java.text.ParseException e) {
            return false;
        }

        if (!formatter.format(date).equals(s)) {
            return false;
        }
        return true;
    }

    /**
     * 기능:offset만큼의 날짜를 추가한다.
     *
     * @param yymmdd
     * @param offset
     * @return String
     */
    public static String getAddDateString(String yymmdd, int offset) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
                Integer.parseInt(yymmdd.substring(6, 8)));
        rightNow.add(Calendar.DATE, offset);
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);
        return formatter.format(rightNow.getTime());
    }

    /**
     * 기능:offset만큼의 주를 추가한다.
     *
     * @param yymmdd
     * @param offset
     * @return String
     */
    public static String getAddWeekString(String yymmdd, int offset) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
                Integer.parseInt(yymmdd.substring(6, 8)));
        rightNow.add(Calendar.WEDNESDAY, offset);
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);
        return formatter.format(rightNow.getTime());
    }

    /**
     * 기능:offset만큼의 달를 추가한다.
     *
     * @param yymmdd
     * @param offset
     * @return
     */
    public static String getAddMonthString(String yymmdd, int offset) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
                Integer.parseInt(yymmdd.substring(6)));

        rightNow.add(Calendar.MONTH, offset);

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);

        return formatter.format(rightNow.getTime());
    }

    /**
     * 기능:offset만큼의 년 추가한다.
     *
     * @param yymmdd
     * @param offset
     * @return
     */
    public static String getAddYearString(String yymmdd, int offset) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
                Integer.parseInt(yymmdd.substring(6)));

        rightNow.add(Calendar.YEAR, offset);

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);

        return formatter.format(rightNow.getTime());
    }

    /**
     * 날짜포맷 문자열 리턴
     * @param pattern
     * @return String
     * @throws ParseException
     */
    public static String getFormatString(String strDate, String pattern) throws ParseException {
        String returnValue;
        String tempFormat;

        String date = getNumber(strDate);

        int dateLength = date.length();

        switch (dateLength) {
        case 6:
            tempFormat = DateTime.DATE_FORMAT_YYYYMM;
            break;
        case 8:
            tempFormat = DateTime.DATE_FORMAT_YYYYMMDD;
            break;
        case 10:
            tempFormat = DateTime.DATE_FORMAT_YYYYMMDDHH;
            break;
        case 12:
            tempFormat = DateTime.DATE_FORMAT_YYYYMMDDHHMM;
            break;
        case 14:
            tempFormat = DateTime.DATE_FORMAT_YYYYMMDDHHMMSS;
            break;
        default:
            tempFormat = "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(tempFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pattern);

        returnValue = sdf2.format(sdf.parse(date));

        return returnValue;
    }

    /**
     * 숫자만 추출한다. Regular Expression 적용.
     *
     * @param extractDate
     *            날짜
     * @return String 숫자만으로 된 문자열
     */
    public static String getNumber(String extractDate) {
        StringBuilder returnValue = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(extractDate);

        for (int i = 0; matcher.find(i); i = matcher.end()){
            returnValue.append(extractDate.substring(matcher.start(), matcher.end()));
        }

        return returnValue.toString();
    }

    /**
     * 기능:현재 년도를 "yyyy" 형식으로 return 한다.
     *
     * @return String
     */
    public static String getYear() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능 : 현재 요일을 리턴한다.
     *
     * @return String
     */
    public static String getDay() {
        DateFormatSymbols symbol = new DateFormatSymbols(Locale.KOREA);

        String[] dayofweek = symbol.getWeekdays();

        int day = Calendar.getInstance(TimeZone.getTimeZone("JST")).get(Calendar.DAY_OF_WEEK);

        return dayofweek[day];
    }

    /**
     * 기능 : 현재 요일을 리턴한다.
     *
     * @return String
     */
    public static String getShortDay() {
        DateFormatSymbols symbol = new DateFormatSymbols(Locale.KOREA);

        String[] dayofweek = symbol.getShortWeekdays();

        int day = Calendar.getInstance(TimeZone.getTimeZone("JST")).get(Calendar.DAY_OF_WEEK);

        return dayofweek[day];
    }

    /**
     * 기능:현재일자를 format 형식으로 return 한다.
     *
     * @return
     */
    public static String getDateString(String format) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능 : 현재일자를 "yyyyMMdd" 형식으로 return 한다.
     *
     * @return String
     */
    public static String getDateString() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재일자를 "yyyy-MM-dd" 형식으로 return 한다.
     *
     * @return
     */
    public static String getDateString2() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD, java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재일자를 "yyyy/MM/dd" 형식으로 return 한다.
     *
     * @return
     */
    public static String getDateString3() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재일자를 "yyyy.MM.dd" 형식으로 return 한다.
     *
     * @return
     */
    public static String getDateString4() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재시간을 "HHmm" 형식으로 return 한다.
     *
     * @return String
     */
    public static String getTimeString() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HHmm", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 시간을 "HH:mm:ss" 형식으로 return 한다.
     *
     * @return String
     */
    public static String getTimeString2() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재시간을 "HHmmss" 형식으로 return 한다.
     *
     * @return String
     */
    public static String getTimeString3() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HHmmss", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }



    /**
     *
     * @param sz
     * @return
     */
    public static Timestamp getTimestamp(String sz) {
        return (Timestamp) DateUtil.parseValue(sz);
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyy-MM-dd-HH:mm:ss" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStampString() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS",
                java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyy-MM-dd-HH:mm:ss" 형식으로 return한다.
     *
     * @param dt input date
     * @return
     */
    public static String getTimeStampString(Date dt) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS",
                java.util.Locale.KOREA);
        return formatter.format(dt);
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyy-MM-dd HH:mm:SSS" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStampString2() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS",
                java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyy-MM-dd HH:mm:SSS" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStampString2(Date dt) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS",
                java.util.Locale.KOREA);
        return formatter.format(dt);
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmmssSSS" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStamp2String() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS",
                java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmmssSSS" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStamp2String(Date dt) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS",
                java.util.Locale.KOREA);
        return formatter.format(dt);
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmmss" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStamp3String() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDDHHMMSS, java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmmss" 형식으로 return한다.
     *
     * @param dt input data
     * @return
     */
    public static String getTimeStamp3String(Date dt) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDDHHMMSS, java.util.Locale.KOREA);
        return formatter.format(dt);
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmm" 형식으로 return한다.
     *
     * @return
     */
    public static String getTimeStamp4String() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDDHHMM, java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }

    /**
     * 기능:현재 날짜와 시간을 "yyyyMMddHHmm" 형식으로 return한다.
     *
     * @param dt input date
     * @return
     */
    public static String getTimeStamp4String(Date dt) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDDHHMM, java.util.Locale.KOREA);
        return formatter.format(dt);
    }

    /**
     * 기능 : 시간을 비교한다.
     *
     * @param time
     * @param fromTime
     * @param toTime
     * @return String
     */
    public static boolean isTrueTime(int time, int fromTime, int toTime) {
        if (time >= fromTime && time < toTime) {
            return true;
        }
        return false;
    }

    /**
     * 기능:oldType을 newType으로 변환하여 return 한다.
     *
     * @param oldType oldType
     * @param newType newType
     * @param date date
     * @return
     */
    public static String getDateFormat(String oldType, String newType, String date) {
        String newDate;
        try {
            SimpleDateFormat oldDate = new SimpleDateFormat(oldType, Locale.KOREA);
            SimpleDateFormat newdate = new SimpleDateFormat(newType, Locale.KOREA);
            newDate = newdate.format(oldDate.parse(date));
        } catch (Exception e) {
            LOGGER.trace(e.getMessage(), e);
            newDate = date;
        }

        return newDate;
    }

    /**
     * 지정한 날짜에서 지정한 날수를 더하거나 뺀 날짜를 구한다
     *
     * @param date String
     *            "지정일 : yyyyMMdd"
     * @param offset int
     *            "입력일 부터 상대 날짜, -값은 과거일, + 값은 미래일)
     * @return 작업 결과(지정한 날짜에서 지정한 날수를 계산한 날짜)
     */

    public static String getDefaultDate(String date, int offset) {

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
                Integer.parseInt(date.substring(6, 8)));

        cal.add(Calendar.DATE, offset);

        int iYear = cal.get(Calendar.YEAR);
        int iMonth = cal.get(Calendar.MONTH) + 1;
        int iDate = cal.get(Calendar.DATE);

        String sNewDate = String.valueOf(iYear);
        if (iMonth < 10){
            sNewDate += "0" + iMonth;
        }else{
            sNewDate += String.valueOf(iMonth);
        }

        if (iDate < 10){
            sNewDate += "0" + iDate;
        }else{
            sNewDate += String.valueOf(iDate);
        }

        return sNewDate;
    }

    /**
     * 지정한 날짜에서 지정한 날수를 더하거나 뺀 날짜를 구한다
     *
     * @param String
     *            "지정일 : yyyy-MM-dd"
     * @param int
     *            "입력일 부터 상대 날짜, -값은 과거일, + 값은 미래일)
     * @return 작업 결과(지정한 날짜에서 지정한 날수를 계산한 날짜)
     */

    public static String getDefaultDate2(String date, int offset) {

        Calendar cal = Calendar.getInstance();

        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1,
                Integer.parseInt(date.substring(8, 10)));

        cal.add(Calendar.DATE, offset);

        int iYear = cal.get(Calendar.YEAR);
        int iMonth = cal.get(Calendar.MONTH) + 1;
        int iDate = cal.get(Calendar.DATE);

        String sNewDate = iYear + "-";

        if (iMonth < 10){
            sNewDate += "0" + iMonth;
        }else{
            sNewDate += String.valueOf(iMonth);
        }
        sNewDate += "-";

        if (iDate < 10){
            sNewDate += "0" + iDate;
        }else{
            sNewDate += String.valueOf(iDate);
        }

        return sNewDate;
    }

    /**
     * 두 날짜의 차이를 일자로 구하여 반환한다. yyyyMMdd 형식만 가능
     *
     * @param startDate
     *            시작일자
     * @param endDate
     *            종료일자
     * @return 두 날짜의 차이
     * @throws ParseException
     * @throws Exception
     */
    public static String getDistanceDates(String startDate, String endDate) throws ParseException {
        String returnValue;

        SimpleDateFormat sdf = new SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD);

        Date sDate = sdf.parse(startDate);
        Date eDate = sdf.parse(endDate);

        long sTime = sDate.getTime();
        long eTime = eDate.getTime();
        long dTime = eTime - sTime;

        returnValue = String.valueOf(dTime / 1000 / 60 / 60 / 24);

        return returnValue;
    }

    /**
     * 두 날짜의 차이를 일자로 구하여 반환한다. yyyyMMdd 형식만 가능
     *
     * @param startDate
     *            시작일자
     * @param endDate
     *            종료일자
     * @return 두 날짜의 차이
     * @throws ParseException
     * @throws Exception
     */
    public static long getDistanceDates1(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTime.DATE_FORMAT_YYYYMMDD);

        Date sDate = sdf.parse(startDate);
        Date eDate = sdf.parse(endDate);

        long sTime = sDate.getTime();
        long eTime = eDate.getTime();
        long dTime = eTime - sTime;

        return dTime / 1000 / 60 / 60 / 24;
    }

    /**
     * 두 날짜의 차이를 일자로 구하여 반환한다. format 형식만 가능
     *
     * @param startDate
     *            시작일자
     * @param endDate
     *            종료일자
     * @return 두 날짜의 차이
     * @throws ParseException
     * @throws Exception
     */
    public static String getDistanceDates(String startDate, String endDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date sDate = sdf.parse(startDate);
        Date eDate = sdf.parse(endDate);

        long sTime = sDate.getTime();
        long eTime = eDate.getTime();
        long dTime = eTime - sTime;

        return String.valueOf(dTime / 1000 / 60 / 60 / 24);
    }

    /**
     * Date를 원하는 포맷의 문자열로 리턴한다.
     *
     * @param srcDate
     * @param format
     * @return
     */
    public static String getDateFormatStr(Date srcDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        if (srcDate != null) {
            return sdf.format(srcDate);
        } else {
            return "";
        }
    }

    /**
     * 넘어온 포멧 형태로 현재 날짜 기준 전일, 후일 날짜 반환
     *
     * @param format
     *            - 출력받을 날짜 포멧
     * @param day
     *            - 전,후일 날짜 ex) -1 : 하루전 , -5 : 5일전, 5 : 5일후
     * @return String - 날짜
     */
    public static String getDate(String format, int day) {

        Date today = new Date();
        Date seldate = new Date();
        SimpleDateFormat simple = new SimpleDateFormat(format);
        seldate.setTime(today.getTime() + (1000 * 60 * 60 * 24) * day);

        return simple.format(seldate);
    }

    /**
     * 현재 날짜의 파라미터만큼의 전월을 구한다.
     *
     * @param minVal
     * @return
     */
    public static String getBeforeMonth(int minVal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, minVal);

        String beforeYear = dateFormat.format(cal.getTime()).substring(0, 4);
        String beforeMonth = dateFormat.format(cal.getTime()).substring(4, 6);

        return beforeYear + beforeMonth;
    }


}
