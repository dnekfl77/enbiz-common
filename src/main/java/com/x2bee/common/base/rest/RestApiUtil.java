package com.x2bee.common.base.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.x2bee.common.base.exception.AppException;
import com.x2bee.common.base.exception.MessageResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestApiUtil {
	/**
	 * RestApi 호출하여 응답반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> Response<T> get(String url, Object params, ParameterizedTypeReference<Response<T>> responseReference) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).get(params, responseReference);
		return processResult(response);
	}

	/**
	 * RestApi 호출하여 응답반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @param responseReference
	 * @return
	 * @throws Exception
	 */
	public <T> Response<T> post(String url, Object params, ParameterizedTypeReference<Response<T>> responseReference) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).post(params, responseReference);
		return processResult(response);
	}
	
	/**
	 * RestApi 호출하여 응답반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @param responseReference
	 * @return
	 * @throws Exception
	 */
	public <T> Response<T> put(String url, Object params, ParameterizedTypeReference<Response<T>> responseReference) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).put(params, responseReference);
		return processResult(response);
	}
	
	/**
	 * RestApi 호출하여 응답반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @param responseReference
	 * @return
	 * @throws Exception
	 */
	public <T> Response<T> patch(String url, Object params, ParameterizedTypeReference<Response<T>> responseReference) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).patch(params, responseReference);
		return processResult(response);
	}
	
	/**
	 * RestApi 호출하여 응답반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @param responseReference
	 * @return
	 * @throws Exception
	 */
	public <T> Response<T> delete(String url, Object params, ParameterizedTypeReference<Response<T>> responseReference) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).delete(params, responseReference);
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
	private <T> Response<T> processResult(RestResponse<Response<T>> response) throws Exception {
		if (response.hasError()) {
			log.error("", response.getException());
			//400일경우 API 받은 오류 메세지를 세팅하여 AppException 발생시킴
			if (response.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
				if (response.getBody() != null && response.getBody().getMessage() != null) {
					throw new AppException(response.getBody().getCode(), response.getBody().getMessage());
				} else {
					throw new AppException(response.getBody().getCode(), MessageResolver.getMessage("adminCommon.restapi.internalServerError")); 
				}
			} else {
				throw new AppException(response.getBody().getCode(), MessageResolver.getMessage("adminCommon.restapi.internalServerError"));
			}
			
		}
		return response.getBody();
	}

	/**
	 * RestApi 호출하여 응답+HttpStatus 반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseEntity<Response<T>> getAndGetResponseEntity(String url, Object params) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).get(params, new ParameterizedTypeReference<Response<T>>() {});
		if (response.hasError()) {
			log.error("", response.getException());
			if (response.getBody() != null && response.getBody().getMessage() != null) {
				throw new AppException(response.getBody().getCode(), response.getBody().getMessage());
			}
			else {
				throw response.getException(); 
			}
		}
		return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCode()));
	}

	/**
	 * RestApi 호출하여 응답+HttpStatus 반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseEntity<Response<T>> postAndGetResponseEntity(String url, Object params) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).post(params, new ParameterizedTypeReference<Response<T>>() {});
		if (response.hasError()) {
			log.error("", response.getException());
			throw response.getException();
		}
		return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCode()));
	}

	/**
	 * RestApi 호출하여 응답+HttpStatus 반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseEntity<Response<T>> putAndGetResponseEntity(String url, Object params) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).put(params, new ParameterizedTypeReference<Response<T>>() {});
		if (response.hasError()) {
			log.error("", response.getException());
			throw response.getException();
		}
		return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCode()));
	}

	/**
	 * RestApi 호출하여 응답+HttpStatus 반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseEntity<Response<T>> patchAndGetResponseEntity(String url, Object params) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).patch(params, new ParameterizedTypeReference<Response<T>>() {});
		if (response.hasError()) {
			log.error("", response.getException());
			throw response.getException();
		}
		return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCode()));
	}

	/**
	 * RestApi 호출하여 응답+HttpStatus 반환.
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseEntity<Response<T>> deleteAndGetResponseEntity(String url, Object params) throws Exception {
		RestResponse<Response<T>> response = RestApi.client(url).delete(params, new ParameterizedTypeReference<Response<T>>() {});
		if (response.hasError()) {
			log.error("", response.getException());
			throw response.getException();
		}
		return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCode()));
	}

}
