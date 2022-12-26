package com.x2bee.common.base.util;

/**
 * @author whlee
 * @version 1.0
 * @since 2021. 11. 22.
 */
public class DataBaseUtil {

	public static String generateDbSequence(String prefix, String middlefix, int sequence, int digit) {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.nvl(prefix, ""));
		sb.append(StringUtil.nvl(middlefix, ""));
		sb.append(String.format("%0"+digit+"d", sequence));
		return sb.toString();
	}

}
