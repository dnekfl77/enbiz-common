package com.enbiz.common.base.util;

import org.slf4j.helpers.MessageFormatter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringFormatter {

	public String format(String format, Object... args) {
		return MessageFormatter.arrayFormat(format, args).getMessage();
	}

}