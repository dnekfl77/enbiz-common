package com.enbiz.common.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enbiz.common.base.annotation.EmptyToNull;
import com.enbiz.common.base.entity.BaseCommonEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Java Reflection API를 쉽게 사용할 수 있도록 하는 Utility class
 *
 */
@Slf4j
public class ReflectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);
    
    public static Field[] getDeclaredFieldsAll(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> currentClass = type;

        while (currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return fields.toArray(new Field[] {});
    }

	public static Field getDeclaredField(Class<?> type, String fieldName) {
		Class<?> currentType = type;
		Field resultField = null;

		while (!Object.class.equals(currentType)) {
			try {
				resultField = currentType.getDeclaredField(fieldName);
				break;
			} catch (Exception e) {
				LOGGER.trace(e.getMessage(), e);
				currentType = currentType.getSuperclass();
			}
		}

		return resultField;
	}

    /**
     * 해당 필드에 @EmptyToNull 선언
     * Entity Field empty String => Null 변환
     * @param targetObj
     * @param <T>
     */
    public static <T> void convertAnnotationEmptyToNull(T targetObj){
        if(targetObj == null){
            return;
        }

		org.springframework.util.ReflectionUtils.doWithFields(targetObj.getClass(), field -> {
			Annotation annotation = field.getAnnotation(EmptyToNull.class);
			Object value;
			try {
				value = BeanUtils.getProperty(targetObj, field.getName());
			} catch (InvocationTargetException | NoSuchMethodException e) {
				value = null;
			}

			if (value == null) {
				return;
			}

			if (annotation != null && field.getType() == String.class) {
				if (value.toString().trim().equals("")) {
					try {
						BeanUtils.setProperty(targetObj, field.getName(), null);
					} catch (InvocationTargetException e) {
						log.info("", e);
					}
				}
			}

			if (BaseCommonEntity.class.isAssignableFrom(field.getType())) {
				convertAnnotationEmptyToNull(value);
			}

			if (field.getType() == List.class) {
				((List<?>) value).iterator().forEachRemaining(ReflectionUtils::convertAnnotationEmptyToNull);
			}
		});
    }

    public static Map<String, Object> convertToMap(Object obj) throws IllegalAccessException, InstantiationException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    	ObjectMapper objectMapper = new ObjectMapper();

    	if (obj == null) {
            return Collections.emptyMap();
        }

        @SuppressWarnings("unchecked")
		Map<String, Object> convertMap = objectMapper.convertValue(obj, Map.class);

        return convertMap;
    }

    public static <T> T convertToValueObject(Map<String, Object> map, Class<T> type)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	if(type == null) {
    		throw new NullPointerException("can't find Class.");
    	}
    	
		T instance = (T) objectMapper.convertValue(map, type);

    	return instance;
    }

    public static List<Map<String, Object>> convertToMaps(List<?> list)
            throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> convertList = new ArrayList<>();

        for (Object obj : list) {
            convertList.add(ReflectionUtils.convertToMap(obj));
        }
        return convertList;
    }

    public static <T> List<T> convertToValueObjects(List<Map<String, Object>> list, Class<T> type)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> convertList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            convertList.add(ReflectionUtils.convertToValueObject(map, type));
        }
        return convertList;
    }
    
    // convertToValueObject와 동일
    public static <T> T converToValueObject(Class<?> type, Map<String, ?> map) throws IllegalAccessException
    	, InstantiationException, NoSuchMethodException, InvocationTargetException{
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	if(type == null) {
    		throw new NullPointerException("can't find Class.");
    	}
    	
        @SuppressWarnings("unchecked")
		T instance = (T) objectMapper.convertValue(map, type);

    	return instance;
    }

}
