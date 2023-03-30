package com.enbiz.common.base.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Joiner;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNoUtils {

	public PhoneNo valueOf(String phoneNo) {
		if (StringUtils.isBlank(phoneNo)) {
			return new PhoneNo();
		}

		var pattern = Pattern.compile("^(02|\\d{3})(\\d{3,4})(\\d{4})$");
		var matcher = pattern.matcher(phoneNo);
		if (matcher.matches()) {
			return new PhoneNo().setSctNo(matcher.group(1)).setTxnoNo(matcher.group(2)).setEndNo(matcher.group(3));
		}

		pattern = Pattern.compile("^(\\d{2,3})-(\\d{3,4})-(\\d{4})$");
		matcher = pattern.matcher(phoneNo);
		if (matcher.matches()) {
			return new PhoneNo().setSctNo(matcher.group(1)).setTxnoNo(matcher.group(2)).setEndNo(matcher.group(3));
		}

		return null;
	}

	@Getter
	@Setter
	@Accessors(chain = true)
	@JsonInclude(Include.NON_EMPTY)
	public static class PhoneNo {
		private String sctNo;
		private String txnoNo;
		private String endNo;

		public String toJoinString(String separator) {
			return Joiner.on(separator).skipNulls().join(sctNo, txnoNo, endNo);
		}
	}
}
