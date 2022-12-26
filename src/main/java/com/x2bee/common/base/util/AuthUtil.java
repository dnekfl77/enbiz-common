package com.x2bee.common.base.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class AuthUtil {
    public static Optional<String> resolveToken(HttpServletRequest request) {
		final String prefix = "Bearer ";
		String authorization = request.getHeader("Authorization");
		if ( StringUtils.hasText(authorization) && StringUtils.startsWithIgnoreCase(authorization, prefix) ) {
			return Optional.ofNullable(authorization.substring(prefix.length()));
		}
		return Optional.empty();
    }

}
