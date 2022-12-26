package com.x2bee.common.base.rest.erpapi;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ErpRequest extends BaseCommonEntity {
	private static final long serialVersionUID = -5919140823589423335L;
	private String ifType;
	private String ifId;
	private String url;
	private LinkedMultiValueMap<String, String> headers; 
	private LinkedMultiValueMap<String, String> formData; 
	private int connectionTimeoutSeconds = -1;
	private int responseTimeoutSeconds = -1;
}
