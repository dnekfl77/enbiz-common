package com.x2bee.common.base.rest.infapi;

import java.util.LinkedHashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.x2bee.common.base.exception.AppException;
import com.x2bee.common.base.exception.MessageResolver;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.RestApi;
import com.x2bee.common.base.rest.RestResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InfApiUtil {
	@Value("${app.apiUrl.common}")
	private String commonApiUrl;

	private static final String INF_REQUEST_ONE_URI = "/api/common/inf-requests/one";
	private static final String INF_REQUEST_URI = "/api/common/inf-requests";
	private static final String INF_REQUEST_TEXT_URI = "/api/common/inf-requests/text";
	
	/**
	 * RestApi get 호출하여 응답반환.
	 * @param <T>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T getOne(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return get(commonApiUrl+INF_REQUEST_ONE_URI, request, responseType);
	}
	public <T> T get(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return get(commonApiUrl+INF_REQUEST_URI, request, responseType);
	}
	public <T> T getText(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return get(commonApiUrl+INF_REQUEST_TEXT_URI, request, responseType);
	}
	private <T> T get(String ifUrl, InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		request.setHttpMethod(HttpMethod.GET.name());
		RestResponse<T> response = RestApi.client(ifUrl).post(request, responseType);
		return processResult(response);
	}

	/**
	 * RestApi post 호출하여 응답반환.
	 * @param <T>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T postOne(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return post(commonApiUrl+INF_REQUEST_ONE_URI, request, responseType);
	}
	public <T> T post(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return post(commonApiUrl+INF_REQUEST_URI, request, responseType);
	}
	private <T> T post(String ifUrl, InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		request.setHttpMethod(HttpMethod.POST.name());
		RestResponse<T> response = RestApi.client(ifUrl).post(request, responseType);
		return processResult(response);
	}

	/**
	 * RestApi put 호출하여 응답반환.
	 * @param <T>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T putOne(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return put(commonApiUrl+INF_REQUEST_ONE_URI, request, responseType);
	}
	public <T> T put(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return put(commonApiUrl+INF_REQUEST_URI, request, responseType);
	}
	private <T> T put(String ifUrl, InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		request.setHttpMethod(HttpMethod.PUT.name());
		RestResponse<T> response = RestApi.client(ifUrl).post(request, responseType);
		return processResult(response);
	}

	/**
	 * RestApi patch 호출하여 응답반환.
	 * @param <T>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T patchOne(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return patch(commonApiUrl+INF_REQUEST_ONE_URI, request, responseType);
	}
	public <T> T patch(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return patch(commonApiUrl+INF_REQUEST_URI, request, responseType);
	}
	private <T> T patch(String ifUrl, InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		request.setHttpMethod(HttpMethod.PATCH.name());
		RestResponse<T> response = RestApi.client(ifUrl).post(request, responseType);
		return processResult(response);
	}

	/**
	 * RestApi delete 호출하여 응답반환.
	 * @param <T>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T deleteOne(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return delete(commonApiUrl+INF_REQUEST_ONE_URI, request, responseType);
	}
	public <T> T delete(InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		return delete(commonApiUrl+INF_REQUEST_URI, request, responseType);
	}
	private <T> T delete(String ifUrl, InfRequest request, ParameterizedTypeReference<T> responseType) throws Exception {
		request.setHttpMethod(HttpMethod.DELETE.name());
		RestResponse<T> response = RestApi.client(ifUrl).post(request, responseType);
		return processResult(response);
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
	private <T> T processResult(RestResponse<T> response) throws Exception {
		if (response.hasError()) {
			log.error("", response.getException());
			//400일경우 API 받은 오류 메세지를 세팅하여 AppException 발생시킴
			if (response.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
					throw new AppException("0400", MessageResolver.getMessage("adminCommon.restapi.internalServerError")); 
			} else {
				throw new AppException("0500", MessageResolver.getMessage("adminCommon.restapi.internalServerError"));
			}
			
		}
		return response.getBody();
	}

	
	
}
