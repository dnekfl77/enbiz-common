package com.enbiz.common.base.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.enbiz.common.base.constant.BaseConstants;

public class TimestampPropertyEditorSupport extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String value){
		Timestamp timestamp = parseValue(value);
		setValue(timestamp);
	}
	
	private Timestamp parseValue(String value){
        Timestamp timestamp = parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DASH_DELIM.get());
        timestamp = timestamp == null ? parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DASH_DELIM.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS.get()) : timestamp;
		timestamp = timestamp == null ? parseValueWithTimestamp(value, BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD.get()) : timestamp;

		return timestamp;
	}
	
	private Timestamp parseValueWithTimestamp(String value, SimpleDateFormat simpleDateFormat){
		Timestamp timestamp = null;
		try{
			Date parsedDate = simpleDateFormat.parse(value);
			timestamp = new Timestamp(parsedDate.getTime());
		}catch(Exception e){
			//cannot parse
		}
		
		return timestamp;
	}
}
