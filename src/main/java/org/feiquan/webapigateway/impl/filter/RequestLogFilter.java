package org.feiquan.webapigateway.impl.filter;

import org.feiquan.webapigateway.common.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 完成每次请求的日志记录
 * @author junwei.jjw
 * @date 2021/6/14
 */
@Slf4j
@Order(Integer.MAX_VALUE)
@Component
public class RequestLogFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log(exchange, null);
        }));
    }

    public static void log(ServerWebExchange exchange) {
        log(exchange, null);
    }

    public static void log(ServerWebExchange exchange, Throwable error) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = ((LinkedHashSet<URI>)exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR)).iterator().next();
        String requestQuery = requestUri.getQuery();
        String requestUrl = requestUri.getPath() + (StringUtils.isEmpty(requestQuery) ? "" : "?" + requestQuery);
        String requestMethod = request.getMethodValue().toUpperCase();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);

        long time = System.currentTimeMillis() - SetStartTimeFilter.getStartMs(exchange);
        String result;
        if (error == null) {
            HttpStatus responseStatus = exchange.getResponse().getStatusCode();
            result = Integer.toString(responseStatus.value());
        } else {
            result = "ERROR: " + ErrorUtil.getMessage(error);
        }
        log.info("{}\t{}\t->\t{}\t{}\t=\t{}ms\t{}", requestMethod, requestUrl, route.getId(), routeUri, time, result);
    }
}
