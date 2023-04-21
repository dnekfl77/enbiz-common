package com.enbiz.common.base.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.enbiz.common.base.entity.BaseCommonEntity;

public class ClientInfoPopulaterSupport {

	public void populateClientInfo(Object o) {
		if ( Objects.isNull(o) ) {
			return;
		}
		
//		if ( log.isDebugEnabled() ) {
//			log.debug("[CLIENT_INFO_POPULATER]: arg-class={}", o.getClass());
//		}
		if ( o instanceof Collection ) {
			populateClientInfo((Collection<?>)o);
		} else if ( o instanceof Map ) {
			populateClientInfo((Map<?,?>)o);
		} else if ( o.getClass().isArray() ) {
			populateClientInfo(Arrays.asList((Object[])o));
		} else if ( o instanceof BaseCommonEntity ) {
			ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
			
//			if ( log.isDebugEnabled() ) {
//				log.debug("[CLIENT_INFO_POPULATER]: client-info={}", clientInfo);
//			}

			if ( Objects.nonNull(clientInfo) ) {
				BaseCommonEntity entity = (BaseCommonEntity)o;
				entity.setDbLocaleLanguage(clientInfo.getDbLocaleLanguage());
//				entity.setDbTimeZone(clientInfo.getDbTimeZone());
//				entity.setJavaTimeZone(TimeZone.getTimeZone(clientInfo.getJavaTimeZone()));
				if (StringUtils.isNotBlank(clientInfo.getSysMenuId())) {
					entity.setSysRegMenuId(clientInfo.getSysMenuId());
					entity.setSysModMenuId(clientInfo.getSysMenuId());
				}
				if (StringUtils.isNotBlank(clientInfo.getSysIpAddr())) {
					entity.setSysRegIpAddr(clientInfo.getSysIpAddr());
					entity.setSysModIpAddr(clientInfo.getSysIpAddr());
				}
				// FO API SysRegId 공통 처리
				if (StringUtils.isBlank(entity.getSysRegId())
						&& StringUtils.isNotBlank(clientInfo.getMbrNo())) {
					entity.setSysRegId(clientInfo.getMbrNo());
				}
				// FO API SysModId 공통 처리
				if (StringUtils.isBlank(entity.getSysModId())
						&& StringUtils.isNotBlank(clientInfo.getMbrNo())) {
					entity.setSysModId(clientInfo.getMbrNo());
				}
				// BO API SysRegId 공통 처리
				if (StringUtils.isBlank(entity.getSysRegId())
						&& StringUtils.isNotBlank(clientInfo.getLoginId())) {
					entity.setSysRegId(clientInfo.getLoginId());
				}
				// BO API SysModId 공통 처리
				if (StringUtils.isBlank(entity.getSysModId())
						&& StringUtils.isNotBlank(clientInfo.getLoginId())) {
					entity.setSysModId(clientInfo.getLoginId());
				}
			}
		}
	}
	
	private void populateClientInfo(Collection<?> collection) {
		if ( ! CollectionUtils.isEmpty(collection) ) {
			for (Object object : collection) {
				populateClientInfo(object);
			}
		}
	}

	private void populateClientInfo(Map<?,?> map) {
		if ( ! CollectionUtils.isEmpty(map) ) {
			populateClientInfo(map.values());
		}
	}

}
