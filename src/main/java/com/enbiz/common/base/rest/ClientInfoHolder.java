package com.enbiz.common.base.rest;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import com.enbiz.common.base.util.JsonUtils;
import com.enbiz.common.base.util.RequestUtils;

public abstract class ClientInfoHolder {
	public static ClientInfo getClientInfo() {
		ClientInfo clientInfo = resolveClientInfo();
		return clientInfo;
	}
	
	private static ClientInfo resolveClientInfo() {
		ClientInfo clientInfo = RequestUtils.getAttribute(ClientInfo.CLIENT_INFO_HEADER_NAME);
		if (clientInfo == null) {
			HttpHeaders headers = RequestUtils.requestHeaders();
			if ( Objects.nonNull(headers) ) {
				String value = headers.getFirst(ClientInfo.CLIENT_INFO_HEADER_NAME);
				if ( StringUtils.hasText(value) ) {
					clientInfo =  JsonUtils.object(value, ClientInfo.class);
					RequestUtils.setAttribute(ClientInfo.CLIENT_INFO_HEADER_NAME, clientInfo);
					return clientInfo;
				}
			}
			
			return ClientInfo.defaultValue();
		}
		else {
			return clientInfo;
		}
	}
}
