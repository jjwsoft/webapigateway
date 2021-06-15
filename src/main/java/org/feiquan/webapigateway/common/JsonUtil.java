package org.feiquan.webapigateway.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json处理工具
 * @author junwei.jjw
 * @date 2021/6/15
 */
public class JsonUtil {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    /**
     * 使用项目默认的Json来进行输出
     *
     * @param object
     * @return
     */
    public static String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析一个文本到指定Java对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Class<T> clz) {
        try {
            return mapper.readValue(json, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static DynamicObject deserialize(String json) {
        return deserialize(json, DynamicObject.class);
    }
}
