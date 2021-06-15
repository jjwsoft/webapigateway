package org.feiquan.webapigateway.common;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author junwei.jjw
 * @date 2021/6/15
 */
public class JsonUtilTest {
    @Test
    public void testSerialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 2);

        String json = JsonUtil.serialize(map);
        System.out.println(json);

        DynamicObject map2 = JsonUtil.deserialize(json);
        assertEquals(map, map2);
    }
}