package com.x2bee.common.base.rest;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x2bee.common.base.context.ApplicationContextWrapper;
import com.x2bee.common.base.token.TokenRequest;
import com.x2bee.common.base.token.ServiceTokenService;
import com.x2bee.common.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RestApi {
	private static ServiceTokenService TOKEN_SERVICE = null;

	private HttpHeaders requestHeaders;
	private UriComponentsBuilder uriComponentsBuilder;
	private Map<String, Object> uriVariables;

	private long latencyTimes = -1;
	
	private boolean enableTokenAuth = false;

	public static RestApi client(String url) {
		return client(url, true);
	}
	
	public static RestApi client(String url, boolean enableTokenAuth) {
		RestApi restApi = new RestApi() {
		};
		if (RestApi.TOKEN_SERVICE == null) {
			RestApi.TOKEN_SERVICE = (ServiceTokenService)ApplicationContextWrapper.getBean("serviceTokenService");
		}
		restApi.uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
		restApi.enableTokenAuth = enableTokenAuth;
		restApi.requestHeaders = new HttpHeaders();
		restApi.requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restApi.requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		return restApi;
	}

	public RestApi addHeader(String key, String value) {
		if (Objects.isNull(requestHeaders)) {
			requestHeaders = new HttpHeaders();
		}
		requestHeaders.add(key, value);
		return this;
	}

	public RestApi setHeader(String key, String value) {
		if (Objects.isNull(requestHeaders)) {
			requestHeaders = new HttpHeaders();
		}
		requestHeaders.set(key, value);
		return this;
	}

	public RestApi queryParam(String name, Object... values) {
		this.uriComponentsBuilder.queryParam(name, values);
		return this;
	}

	public RestApi uriVariable(String name, Object value) {
		if (Objects.isNull(uriVariables)) {
			this.uriVariables = new HashMap<>();
		}
		this.uriVariables.put(name, value);
		return this;
	}

	public MultiValueMap<String, String> getHeaders() {
		return requestHeaders;
	}

	public long getLatencyTimes() {
		return latencyTimes;
	}

	public <T> RestResponse<T> get(Object request, Class<T> type) {
		return execute(request, HttpMethod.GET, type);
	}

	public <T> RestResponse<T> get(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.GET, responseReference);
	}
	
	public <T> RestResponse<T> post(Object request, Class<T> type) {
		return execute(request, HttpMethod.POST, type);
	}
	
	public <T> RestResponse<T> post(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.POST, responseReference);
	}
	
	public <T> RestResponse<T> put(Object request, Class<T> type) {
		return execute(request, HttpMethod.PUT, type);
	}
	
	public <T> RestResponse<T> put(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.PUT, responseReference);
	}
	
	public <T> RestResponse<T> patch(Object request, Class<T> type) {
		return execute(request, HttpMethod.PATCH, type);
	}
	
	public <T> RestResponse<T> patch(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.PATCH, responseReference);
	}
	
	public <T> RestResponse<T> delete(Object request, Class<T> type) {
		return execute(request, HttpMethod.DELETE, type);
	}

	public <T> RestResponse<T> delete(Object request, ParameterizedTypeReference<T> responseReference) {
		return execute(request, HttpMethod.DELETE, responseReference);
	}
	
	private WebClient webClient() {
		return WebClientInstance.get();
	}
	
	private void configRequestHeader(HttpHeaders httpHeaders) {
		configAuthorization(httpHeaders);
		WebClientInstance.configRequestHeader(httpHeaders);
	}
	
	private void configAuthorization(HttpHeaders httpHeaders) {
		if ( this.enableTokenAuth ) {
			final long validMillis = 60 * 1000L;
			httpHeaders.setBearerAuth(TOKEN_SERVICE.createToken(
					new TokenRequest().setUserName("service").setValidMillis(validMillis)).getToken());
		}
	}

	@SuppressWarnings("unchecked")
	private <T> RestResponse<T> execute(Object requestObject, HttpMethod method, Object type) {
		if (Objects.nonNull(uriVariables)) {
			this.uriComponentsBuilder.uriVariables(uriVariables);
		}

		URI orgUrl = this.uriComponentsBuilder.build().toUri(); // 한글 파라미터 로깅을 위하여.
		this.uriComponentsBuilder.encode();
		URI url = this.uriComponentsBuilder.build().toUri();

		configRequestHeader(requestHeaders);
		
		ResponseEntity<T> responseEntity = null;
		Exception exception = null;
		long start = System.currentTimeMillis();

		try {
			ResponseSpec respenseSpec;
			// request body 없는 경우
			if (requestObject == null) { // method == HttpMethod.GET || method == HttpMethod.DELETE
				respenseSpec = webClient()
						.method(method)
						.uri(url)
						.headers(newRequestHeader -> {newRequestHeader.addAll(requestHeaders);})
						.retrieve();
			}
			// request body 있는 경우
			else {
				respenseSpec = webClient()
					.method(method)
					.uri(url)
					.body(BodyInserters.fromValue(requestObject))
					.headers(newRequestHeader -> {newRequestHeader.addAll(requestHeaders);})
					.retrieve();
			}

			// 결과 type이 Class<T>로 지정된 경우
			if (type instanceof Class<?>) {
				responseEntity = respenseSpec.toEntity((Class<T>)type).block();
			}
			// 결과 type이 ParameterizedTypeReference<T>로 지정된 경우
			else {
				responseEntity = respenseSpec.toEntity((ParameterizedTypeReference<T>)type).block();
			}
			
			return new RestResponse<>(responseEntity);
		} catch (RestClientResponseException e) {
			exception = e;
			return new RestResponse<>(e);
		} catch (WebClientResponseException e) {
			exception = e;
			return (RestResponse<T>) this.createErrorResponse(e);
		} catch (Exception e) {
			exception = e;
			return new RestResponse<>(e);
		} finally {
			this.latencyTimes = System.currentTimeMillis() - start;
			logging(orgUrl, method, requestHeaders, requestObject, responseEntity, exception);
		}
	}
	
	private <T> RestResponse<Response<T>> createErrorResponse(WebClientResponseException e) {
		String message = null;
		if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
			message = "{\"code\":\"404\", \"message\":\"url not found\"}";
		} else {
			message = e.getResponseBodyAsString(Charset.defaultCharset());
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map data = null;
		try {
			data = objectMapper.readValue(message, Map.class);
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		Response<T> response = new Response<T>();
		response.setCode((String)data.get("code"));
		response.setMessage((String)data.get("message"));
		
		ResponseEntity<Response<T>> responseEntity = new ResponseEntity<>(response, e.getStatusCode());
		RestResponse<Response<T>> restResponse =  new RestResponse<>(responseEntity, e);
		return restResponse;
	}
	
	private <T> void logging(URI url, HttpMethod method, HttpHeaders reqHeaders, Object reqBody, ResponseEntity<T> responseEntity, Exception e) {
		if (Objects.isNull(e)) {
			Object resBody = responseEntity.getBody();
			logging(url, method, reqHeaders, reqBody==null?null:JsonUtils.string(reqBody), responseEntity.getStatusCodeValue(),
					responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getHeaders(),
					resBody==null?null:JsonUtils.string(resBody), null);
		}
		else if (e instanceof RestClientResponseException) {
			RestClientResponseException re = (RestClientResponseException) e;
			logging(url, method, reqHeaders, reqBody==null?null:JsonUtils.string(reqBody), re.getRawStatusCode(), re.getStatusText(),
					re.getResponseHeaders(), re.getResponseBodyAsString(), e);
		} else {
			logging(url, method, reqHeaders, reqBody==null?null:JsonUtils.string(reqBody), -1, null, null, null, e);
		}
	}

	private void logging(URI url, HttpMethod method, HttpHeaders reqHeaders, String reqBody,
			int statusCode,String statusText, HttpHeaders resHeaders, String resBody, Exception e) {
		StringBuilder sb = new StringBuilder().append('\n');
		sb.append("##################################################################").append('\n');
		sb.append("# [REST_API] latency-time: ").append(this.latencyTimes).append(" ms\n");
		sb.append("##[Request]#######################################################").append('\n');
		sb.append("# URL    : ").append(url).append('\n');
		sb.append("# Method : ").append(method).append('\n');
		sb.append("# Headers: ").append(reqHeaders).append('\n');
		sb.append("# Body   : ").append(reqBody).append('\n');
		sb.append("##[Response]######################################################").append('\n');
		sb.append("# Code   : ").append(statusCode).append(' ').append(statusText).append('\n');
		sb.append("# Headers: ").append(resHeaders).append('\n');
		sb.append("# Body   : ").append(resBody).append('\n');
		sb.append("##################################################################");

		if (Objects.isNull(e)) {
			if (log.isDebugEnabled()) {
				log.debug(sb.toString());
			}
		} else {
			sb.append('\n').append("# Exception : ");
			log.error(sb.toString(), e);
		}
	}

	@Component
	static class WebClientInstance {
		private static WebClient webClient;
		static void set(WebClient webClient) {
			WebClientInstance.webClient = webClient;
		}
		static WebClient get() {
			return WebClientInstance.webClient;
		}

		private static ClientInfoResolver clientInfoResolver;
		static void configRequestHeader(HttpHeaders httpHeaders) {
			if ( Objects.nonNull(WebClientInstance.clientInfoResolver) ) {
				ClientInfo clientInfo = WebClientInstance.clientInfoResolver.resolve();
				if ( Objects.nonNull(clientInfo) ) {
					httpHeaders.set(ClientInfo.CLIENT_INFO_HEADER_NAME, JsonUtils.string(clientInfo));
				}
			}
		}
		
		@Autowired
		public void init(WebClient webClient) {
			WebClientInstance.webClient = webClient;
		}
		
		@Autowired(required = false)
		public void init(ClientInfoResolver clientInfoResolver) {
			WebClientInstance.clientInfoResolver = clientInfoResolver;
		}

	}

}
