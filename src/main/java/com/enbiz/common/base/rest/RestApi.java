package com.enbiz.common.base.rest;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.enbiz.common.base.context.ApplicationContextWrapper;
import com.enbiz.common.base.token.MemberTokenService;
import com.enbiz.common.base.token.ServiceTokenService;
import com.enbiz.common.base.token.TokenRequest;
import com.enbiz.common.base.token.UserDetail;
import com.enbiz.common.base.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RestApi {
	private static ServiceTokenService SERVICE_TOKEN_SERVICE = null;
	private static MemberTokenService MEMBER_TOKEN_SERVICE = null;

	private HttpHeaders requestHeaders;
	private UriComponentsBuilder uriComponentsBuilder;
	private Map<String, Object> uriVariables;

	private long latencyTimes = -1;
	
	private boolean enableTokenAuth = false;

	public static RestApi client(String url) {
		return client(url, true);
	}
	
	public static RestApi client(String url, boolean enableTokenAuth) {
		return client(url, enableTokenAuth, MediaType.APPLICATION_JSON_VALUE);
	}

	public static RestApi client(String url, boolean enableTokenAuth, String acceptTypes) {
		RestApi restApi = new RestApi() {
		};
		if (RestApi.SERVICE_TOKEN_SERVICE == null) {
			RestApi.SERVICE_TOKEN_SERVICE = (ServiceTokenService)ApplicationContextWrapper.getBean("serviceTokenService");
			RestApi.MEMBER_TOKEN_SERVICE = (MemberTokenService)ApplicationContextWrapper.getBean("memberTokenService");
		}
		restApi.uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
		restApi.enableTokenAuth = enableTokenAuth;
		restApi.requestHeaders = new HttpHeaders();
		restApi.requestHeaders.setAccept(MediaType.parseMediaTypes(acceptTypes));
		restApi.requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		restApi.requestHeaders.setAcceptCharset(List.of(Charset.forName("utf-8")));

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
		setObjectAsQueryParam(request);
		return execute(null, HttpMethod.GET, type);
	}

	public <T> RestResponse<T> get(Object request, ParameterizedTypeReference<T> responseReference) {
		setObjectAsQueryParam(request);
		return execute(null, HttpMethod.GET, responseReference);
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
		setObjectAsQueryParam(request);
		return execute(null, HttpMethod.DELETE, type);
	}

	public <T> RestResponse<T> delete(Object request, ParameterizedTypeReference<T> responseReference) {
		setObjectAsQueryParam(request);
		return execute(null, HttpMethod.DELETE, responseReference);
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
			UserDetail userDetail = getUserDetail();
			if (userDetail != null) {
				httpHeaders.setBearerAuth(MEMBER_TOKEN_SERVICE.create(userDetail).getAccessToken());
			}
			else {
				final long validMillis = 60 * 1000L;
				httpHeaders.setBearerAuth(SERVICE_TOKEN_SERVICE.createToken(
						new TokenRequest().setUsername("service").setValidMillis(validMillis)).getToken());
			}
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
	
	@SuppressWarnings("rawtypes")
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
		response.setCode(MapUtils.getString(data, "code"));
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

	/**
	 * GET 메소드 api를 호출한다. 호출 시 파라미터 객체를 query string으로 설정한다.
	 * @param <T>
	 * @param request
	 * @param responseReference
	 * @return
	 */
	public <T> RestResponse<T> getWithQueryParam(Object request, ParameterizedTypeReference<T> responseReference) {
		setObjectAsQueryParam(request);
		return execute(request, HttpMethod.GET, responseReference);
	}

	/**
	 * 객체를 필드 중에서 WrapperType+String 필드를 queryString param으로 세팅한다.
	 * @param request
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void setObjectAsQueryParam(Object request) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (request != null) {
            Map<String, Object> map = objectMapper.convertValue(request, Map.class);
            map.entrySet().stream().forEach(entry ->
            	{
            		if (entry.getValue() != null 
            				&& isWrapperType(entry.getValue())) {
            	    	if (entry.getValue() instanceof List<?> && ((List)entry.getValue()).size() > 0) {
            				this.uriComponentsBuilder.queryParam(entry.getKey(), ((List)entry.getValue()).toArray());
            	    	}
            	    	else {
            				this.uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
            	    	}
            		}
            	});
        }
    }

    /**
     * WrapperType 목록
     */
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    /**
     * WrapperType + String 여부 체크
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
	private static boolean isWrapperType(Object obj) {
    	Class<?> clazz = obj.getClass();
    	if (clazz.isArray()) {
    		return WRAPPER_TYPES.contains(clazz.getComponentType());
    	}
    	else if (obj instanceof List<?> && ((List)obj).size() > 0) {
    		return WRAPPER_TYPES.contains(((List)obj).get(0).getClass());
    	}
    	else {
    		return WRAPPER_TYPES.contains(clazz);
    	}
    }

    /**
     * WrapperType 목록 저장
     * @return
     */
    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        return ret;
    }

    private UserDetail getUserDetail() {
    	if (SecurityContextHolder.getContext().getAuthentication() != null
    			&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
    			&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetail
    			&& StringUtils.isNotBlank(((UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())) {
    		return (UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	}
    	else {
    		return null;
    	}
    }
}
