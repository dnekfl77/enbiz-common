package com.enbiz.common.base.util;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enbiz.common.base.constant.BaseConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("deprecation")
public class ObjectMapperUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperUtils.class);
    
    private static final SimpleDateFormat[] DATE_FORMATS = {
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DASH_DELIM.get(),
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS_WITH_DELIM.get(),
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DELIM.get(),
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD_WITH_DASH_DELIM.get(),
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDDHHMISS.get(),
            BaseConstants.SIMPLE_DATE_FORMAT_YYYYMMDD.get()

    };

    public static <T> T treeToValue(ObjectMapper objectMapper, JsonNode node, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (Exception e) {
            LOGGER.trace(e.getMessage() ,e);
        }

        T t = null;
        for (SimpleDateFormat dateFormat : DATE_FORMATS) {
            try {
                objectMapper.setDateFormat(dateFormat);
                t = objectMapper.treeToValue(node, clazz);

                break;
            } catch (Exception e) {
                LOGGER.trace(e.getMessage() ,e);
                continue;
            }
        }

        objectMapper.setDateFormat(new ISO8601DateFormat());
        return t;
    }
}
