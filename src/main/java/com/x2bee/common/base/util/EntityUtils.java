package com.x2bee.common.base.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x2bee.common.base.entity.AbstractEntity;
import com.x2bee.common.base.exception.FrameworkException;

@Component
public class EntityUtils {
	public static final String INSERT_LIST = "insertList";
	public static final String UPDATE_LIST = "updateList";
	public static final String DELETE_LLIST = "deleteList";
	
	@Autowired
	private CryptoUtil cryptoUtil;

	public EntityUtils() {
		super();
	}

//	public JSONObject toJSONObject(Object object) {
//		return JSONObject.fromObject(object);
//	}
//
//	public JSONArray toJSONArray(List<?> searchedList) {
//		JsonConfig jsonConfig = new JsonConfig();
//		jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
//
//		return JSONArray.fromObject(searchedList, jsonConfig);
//	}

	public static <T> T fromJSONObject(String jsonString, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			throw new FrameworkException(e);
		}
	}

	@SuppressWarnings("unchecked")
    public static <T> T fromBase64(String base64) {
		byte[] bytes = Base64.decodeBase64(base64);

		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais)) {
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new FrameworkException(e);
		}
	}

	public String encrypt(Object obj) {
		String base64 = ((AbstractEntity) obj).toBase64();

		return cryptoUtil.encrypt(base64);
	}

    public <T> T decrypt(String encrypted) {
		String decrypted = cryptoUtil.decrypt(encrypted);

        return fromBase64(decrypted);
	}

    public static boolean nullOrEmpty(List<?> list){
        return list == null || list.isEmpty();
    }

    public static boolean notNullAndNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
