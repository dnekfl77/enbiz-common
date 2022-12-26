package com.x2bee.common.base.rest.erpapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.x2bee.common.base.exception.AppException;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.RestApi;
import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ErpApiUtil {
	@Value("${app.apiUrl.common}")
	private String commonApiUrl;

	private static final String ERP_REQUEST_URI = "/api/common/erp-requests";

	public <T> Response<T> post(ErpRequest request, TypeReference<T> responseType) throws Exception {
		RestResponse<Response<String>> response = RestApi.client(commonApiUrl+ERP_REQUEST_URI).post(request, new ParameterizedTypeReference<Response<String>>() {});
		return processResult(response, responseType);
	}

	/**
	 * RestApi 호출결과 처리. 정상인경우 응답반환. 오류인지 체크하여 오류 response 생성. 
	 * @param <T>
	 * @param url
	 * @param params
	 * @param responseReference
	 * @return
	 * @throws Exception
	 */
	private <T> Response<T> processResult(RestResponse<Response<String>> response, TypeReference<T> responseType) throws Exception {
		if (response.hasError()) {
			log.error("", response.getException());
			//400일경우 API 받은 오류 메세지를 세팅하여 AppException 발생시킴
			if (response.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
					throw new AppException("0400", "[400]시스템 내부 오류 입니다. 오류 지속 시 시스템 관리자에게 문의하세요."); 
			} else {
				throw new AppException("0500", "[500]시스템 내부 오류 입니다. 오류 지속 시 시스템 관리자에게 문의하세요.");
			}
			
		}
		if (response == null || response.getBody() == null || response.getBody().getPayload() == null) {
			return null;
		}
		else {
			return new Response<T>().setPayload(JsonUtils.object(response.getBody().getPayload(), responseType));
		}
	}

}
