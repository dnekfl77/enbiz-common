package com.x2bee.common.base.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;

import com.x2bee.common.base.rest.erpapi.ErpRequest;

/**
 * Interface 관련 Utility Class
 * @author 홍혜리
 *
 */
public class InterfaceUtil {

    public InterfaceUtil() {
    }

    public static ErpRequest getErpRequest (String ifType, String ifId, String url, Object requestData) throws Exception {

        if (StringUtils.isEmpty(ifType)) {
            throw new IllegalArgumentException("ifType can't be null or empty");
        }

        if (StringUtils.isEmpty(ifId)) {
            throw new IllegalArgumentException("ifId can't be null or empty");
        }

        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can't be null or empty");
        }

        if (requestData == null) {
            throw new IllegalArgumentException("requestData can't be null");
        }

        ErpRequest erpRequest = new ErpRequest();
        erpRequest.setSysRegId(getFieldValue(requestData,"sysRegId"));
        erpRequest.setSysRegMenuId(getFieldValue(requestData,"sysRegMenuId"));
        erpRequest.setSysRegIpAddr(getFieldValue(requestData,"sysRegIpAddr"));

        return erpRequest.setIfType(ifType)
                .setIfId(ifId)
                .setUrl(url)
                .setFormData(getErpFormData(requestData));
    }
    //Object가 리스트인 경우
    public static ErpRequest getErpRequestList (String ifType, String ifId, String url, Object requestData) throws Exception {
    	Object info=null;
    	boolean isList=false;
    	if(requestData instanceof List) {
    		isList = true;
    		if(!((List)requestData).isEmpty() && ((List)requestData).size() > 0) {
    			info = ((List)requestData).get(0);
    		}
    	}
    	
        if (StringUtils.isEmpty(ifType)) {
            throw new IllegalArgumentException("ifType can't be null or empty");
        }

        if (StringUtils.isEmpty(ifId)) {
            throw new IllegalArgumentException("ifId can't be null or empty");
        }

        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can't be null or empty");
        }

        if (requestData == null) {
            throw new IllegalArgumentException("requestData can't be null");
        }

        ErpRequest erpRequest = new ErpRequest();
        if(isList) {
        	erpRequest.setSysRegId(getFieldValue(info,"sysRegId"));
            erpRequest.setSysRegMenuId(getFieldValue(info,"sysRegMenuId"));
            erpRequest.setSysRegIpAddr(getFieldValue(info,"sysRegIpAddr"));
        }else {
        	erpRequest.setSysRegId(getFieldValue(requestData,"sysRegId"));
            erpRequest.setSysRegMenuId(getFieldValue(requestData,"sysRegMenuId"));
            erpRequest.setSysRegIpAddr(getFieldValue(requestData,"sysRegIpAddr"));
        }

        return erpRequest.setIfType(ifType)
                .setIfId(ifId)
                .setUrl(url)
                .setFormData(getErpFormData(requestData));
    }

    public static String getFieldValue(Object obj, String fieldName) throws Exception {
        return (String)ReflectionUtil.getFieldValue(obj, ReflectionUtil.getDeclaredField(obj.getClass(),fieldName));
    }

    public static LinkedMultiValueMap<String,String> getErpFormData (Object requestData) throws Exception {

        if (requestData == null) {
            throw new IllegalArgumentException("requestData can't be null");
        }

        String inData = "";

        if (requestData instanceof String) {
            inData = (String) requestData;
        } else {
            inData = JsonUtils.string(requestData);
        }

        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("INDATA", inData);

        return formData;
    }
}
