package org.feiquan.webapigateway.impl.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 完成每次请求的日志记录
 * @author junwei.jjw
 * @date 2021/6/14
 */
@Slf4j
@Order(-1)
@Component
public class SetStartTimeFilter implements GlobalFilter {
    private static final String KEY_START = "requestStartMs";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        exchange.getAttributes().put(KEY_START, start);
        return chain.filter(exchange);
    }

    public static long getStartMs(ServerWebExchange exchange) {
        Object value = exchange.getAttribute(KEY_START);
        if (value instanceof Long) {
            return (long)value;
        }
        return System.currentTimeMillis();
    }
}
