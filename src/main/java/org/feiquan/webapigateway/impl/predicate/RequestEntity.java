package org.feiquan.webapigateway.impl.predicate;

import org.feiquan.webapigateway.common.JsonUtil;
import lombok.Getter;
import org.springframework.web.server.ServerWebExchange;

/**
 * 请求实体包装，用于作为条件表达式的输入，可不断的追加变量
 * @author junwei.jjw
 * @date 2021/6/14
 */
@Getter
public class RequestEntity {
    private String remoteAddress;
    private Object body;

    public static RequestEntity build(ServerWebExchange exchange, Object body) {
        RequestEntity result = new RequestEntity();
        result.remoteAddress = exchange.getRequest().getRemoteAddress().toString();
        result.body = buildBody(body);
        return result;
    }

    private static Object buildBody(Object body) {
        if (body != null) {
            if (body instanceof String) {
                return JsonUtil.deserialize((String)body);
            }
        }
        return body;
    }
}
