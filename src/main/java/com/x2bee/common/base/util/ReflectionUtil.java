package com.x2bee.common.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.x2bee.common.base.annotation.EmptyToNull;
import com.x2bee.common.base.entity.BaseCommonEntity;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Java Reflection API를 쉽게 사용할 수 있도록 하는 Utility class
 *
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
    
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

    public static Field getIterableField(Class<?> type, String propName) {
        Class<?> currentType = type;
        Field resultField = null;

        while (!Object.class.equals(currentType)) {
            try {
                Field[] fields = currentType.getDeclaredFields();
                Field field = getTargetField(fields, propName);
                if (field != null) {
                    resultField = field;
                    resultField.setAccessible(true);
                    break;
                } else {
                    currentType = currentType.getSuperclass();
                }
            } catch (Exception e) {
                LOGGER.trace(e.getMessage(), e);
                resultField = null;
            }
        }
        return resultField;
    }

    private static Field getTargetField(Field[] fields, String fieldName) {
        for (Field field : fields) {
            Class<?> type = field.getType();
            String currentFieldName = field.getName();

            if (!currentFieldName.startsWith(fieldName) && !currentFieldName.endsWith(fieldName)) {
                continue;
            }

            if (!List.class.isAssignableFrom(type) &&
                    !Object[].class.isAssignableFrom(type)) {
                continue;
            }

            return field;
        }

        return null;
    }

    /**
     * Entity Copy
     * 
     * @param src
     * @param desc
     */
    public static void entityCopy(Object src, Object desc) {
        copyFields(src, desc, src.getClass());
    }

    /**
     * Field By Field Copy Value
     * 
     * @param src
     * @param desc
     */
    // public static void copyFieldByField(Object src, Object desc) {
    // copyFields(src, desc, src.getClass());
    // }

    /**
     * Field By Field Copy Value
     * 
     * @param src
     * @param desc
     * @param clazz
     */
    private static void copyFields(Object src, Object desc, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);
            copyFieldValue(src, desc, f);
        }

        clazz = clazz.getSuperclass();
        if (clazz != null) {
            copyFields(src, desc, clazz);
        }
    }

    /**
     * Field Copy Value
     * 
     * @param src
     * @param desc
     * @param f
     */
    private static void copyFieldValue(Object src, Object desc, Field f) {
        try {
            Object value = f.get(src);
            // f.set(desc, value);
            PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(f.getName(), desc.getClass());
            objPropertyDescriptor.getWriteMethod().invoke(desc, value);
        } catch (Exception e) {
            LOGGER.trace(e.getMessage(), e);
        }
    }

    public static Object getFieldValue(Object obj, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (Exception e) {
            LOGGER.trace(e.getMessage(), e);
        }
        return result;
    }

    public static void setFieldValue(Object obj, Field field, Object value) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            field.set(obj, value);
        } catch (Exception e) {
            LOGGER.trace(e.getMessage(), e);
        }
    }

    /**
     * Entity Field empty String => Null 변환
     * @param targetObj
     * @param <T>
     */
    public static <T> void convertEmptyToNull(T targetObj){
        if(targetObj == null){
            return;
        }

        ReflectionUtils.doWithFields(targetObj.getClass(),
                field -> {
                    field.setAccessible(true);
                    Object value = field.get(targetObj);

                    if(value == null){
                        return;
                    }

                    if(field.getType() == String.class){
                        if(value.toString().trim().equals("")){
                            field.set(targetObj,null);
                        }
                    }

                    if(BaseCommonEntity.class.isAssignableFrom(field.getType())){
                        convertAnnotationEmptyToNull(value);
                    }

                    if(field.getType() == List.class){
                        ((List<?>) value).iterator().forEachRemaining(ReflectionUtil::convertAnnotationEmptyToNull);
                    }
                });
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

        ReflectionUtils.doWithFields(targetObj.getClass(),
                field -> {
                    field.setAccessible(true);
                    Annotation annotation = field.getAnnotation(EmptyToNull.class);
                    Object value = field.get(targetObj);

                    if(value == null){
                        return;
                    }

                    if(annotation != null && field.getType() == String.class){
                        if(value.toString().trim().equals("")){
                            field.set(targetObj,null);
                        }
                    }

                    if(BaseCommonEntity.class.isAssignableFrom(field.getType())){
                        convertAnnotationEmptyToNull(value);
                    }


                    if(field.getType() == List.class){
                        ((List<?>) value).iterator().forEachRemaining(ReflectionUtil::convertAnnotationEmptyToNull);
                    }
                });
    }

    public static Map<String, Object> convertToMap(Object obj) throws IllegalAccessException, InstantiationException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (obj == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> convertMap = new LinkedHashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            convertMap.put(field.getName(), field.get(obj));
        }

        return convertMap;
    }

    public static <T> T convertToValueObject(Map<String, Object> map, Class<T> type)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (type == null) {
            throw new NullPointerException("Class cannot be null");
        }

        T instance = type.getConstructor().newInstance();

        if (map == null || map.isEmpty()) {
            return instance;
        }

        for (Map.Entry<String, Object> entrySet : map.entrySet()) {
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                String fieldName = field.getName();

                boolean isSameType = entrySet.getValue().getClass().equals(field.getType());
                boolean isSameName = entrySet.getKey().equals(fieldName);

                if (isSameType && isSameName) {
                    field.set(instance, map.get(fieldName));
                }
            }
        }
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
            convertList.add(ReflectionUtil.convertToMap(obj));
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
            convertList.add(ReflectionUtil.convertToValueObject(map, type));
        }
        return convertList;
    }
}
