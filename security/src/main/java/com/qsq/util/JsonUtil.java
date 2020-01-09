package com.qsq.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author QSQ
 * @create 2019/4/11 11:37
 * No, again
 * 〈〉
 */
@Component
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    static {
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public JsonUtil() {
    }

    /**
     * 将 POJO 对象转为 JSON 字符串
     *
     * @param pojo
     * @param <T>
     * @return
     */
    public static <T> String pojoToJson(T pojo) {
        String json;
        try {
            json = objectMapper.writeValueAsString(pojo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * 将 JSON 字符串转为 POJO 对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T jsonToPojo(String json, Class<T> type) {
        T pojo;
        try {
            pojo = objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pojo;
    }


    public static <T> List<T> fromListJson(String json, Class<T> type) {
        List<T> pojo = Lists.newArrayList();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
            pojo = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pojo;
    }

    public static <K, T> Map<K, T> fromMapJson(String json, Class<K> keyType, Class<T> valueType) {
        Map<K, T> pojo = Maps.newHashMap();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            pojo = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pojo;
    }

}