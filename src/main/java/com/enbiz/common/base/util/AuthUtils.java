package com.enbiz.common.base.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class AuthUtils {
	
	public static Optional<String> resolveToken(HttpServletRequest request) {
		final String prefix = "Bearer ";
		String authorization = request.getHeader("Authorization");
		if (StringUtils.isNotBlank(authorization) && StringUtils.startsWithIgnoreCase(authorization, prefix)) {
			return Optional.ofNullable(authorization.substring(prefix.length()));
		}
		return Optional.empty();
	}
}
