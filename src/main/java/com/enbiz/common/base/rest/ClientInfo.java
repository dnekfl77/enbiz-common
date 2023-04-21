package com.enbiz.common.base.rest;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClientInfo {
	public final static String CLIENT_INFO_HEADER_NAME = "X-ClientInfo";
	
	private String dbLocaleLanguage;
	private String dbTimeZone;
	private String javaTimeZone;
	private Boolean userAgentIsApp = false;
	private String mbrNo;
	private String loginId;
	private String sysMenuId;
	private String sysIpAddr;
	private String siteNo;
	private String entrNo;

	public static ClientInfo defaultValue() {
		ClientInfo info = new ClientInfo();
		info.setDbLocaleLanguage(Locale.getDefault().getLanguage());
		info.setDbTimeZone("UTC");
		info.setJavaTimeZone("UTC");
		return info;
	}
}
