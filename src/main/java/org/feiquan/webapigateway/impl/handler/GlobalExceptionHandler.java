package org.feiquan.webapigateway.impl.handler;

import org.feiquan.webapigateway.impl.filter.RequestLogFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author junwei.jjw
 * @date 2021/6/15
 */
@Slf4j
@Order(0)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        RequestLogFilter.log(exchange, ex);
        return Mono.empty();
    }
}
