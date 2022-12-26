package com.x2bee.common.base.rest.infapi;

import org.springframework.util.LinkedMultiValueMap;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class InfRequest extends BaseCommonEntity {
	private static final long serialVersionUID = 8104153799634606070L;

	private String ifType;
	private String ifId;
	private String httpMethod;
	private String url;
	private LinkedMultiValueMap<String,String> headers; 
	private LinkedMultiValueMap<String, String> formData; 
	private LinkedMultiValueMap<String,String> queryParams; 
	private Object requestObject;
	private int connectionTimeoutSeconds = -1;
	private int responseTimeoutSeconds = -1;
}
