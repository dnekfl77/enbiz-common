package com.enbiz.common.base.constant;

import java.text.SimpleDateFormat;

public class X2Constants {
	public static final String X2_CORE_LOGGER_CATEGORY = "x2.spring.core";
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String Y = "Y";
    public static final String N = "N";
    public static final String COMMA = ",";
    public static final String PERIOD = ".";
    public static final String EMPTY = "";
    public static final String ASTERISK = "*";
    public static final String SLASH = "/";
    public static final String DASH = "-";
    public static final String DLOUBLE_BACK_SLASH = "\\";
    public static final String HTTPS = "https";
    public static final String HTTP = "http";

    //for redirect.jsp
    public static final String JSP_FOR_REDIRECT = "common/redirect";
    public static final String REDIRECT_URL = "redirectUrl";
    public static final String SHOW_MESSAGE = "showMessage";

    //for DateFormat
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDD_WITH_DELIM = "yyyy/MM/dd";
    public static final String YYYYMMDD_WITH_DASH_DELIM = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMISS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMISS_WITH_DELIM = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYYMMDDHHMISS_WITH_DASH_DELIM = "yyyy-MM-dd HH:mm:ss";

    // Paging 상수
    public static final int DEFAULT_BLOCK_COUNT = 10;
    public static final int BLOCK_COUNT_5 = 5;
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int ROWS_PER_PAGE_20 = 20;


    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
            return new SimpleDateFormat(YYYYMMDD_WITH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DASH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
            return new SimpleDateFormat(YYYYMMDD_WITH_DASH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DELIM = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
            return new SimpleDateFormat(YYYYMMDDHHMISS_WITH_DELIM);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDD = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
            return new SimpleDateFormat(YYYYMMDD);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat initialValue(){
            return new SimpleDateFormat(YYYYMMDDHHMISS);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DASH_DELIM = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat(YYYYMMDDHHMISS_WITH_DASH_DELIM);
        }
    };

    private X2Constants() {
   	}
}
