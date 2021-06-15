package org.feiquan.webapigateway.impl.predicate;

import org.feiquan.webapigateway.common.ErrorUtil;
import org.feiquan.webapigateway.impl.expression.GroovyScriptRunner;
import groovy.lang.Binding;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 动态条件断言器，用来决策某路由条件是否成立
 * @author junwei.jjw
 * @date 2021/6/14
 */
@Slf4j
public class ConditionPredicateFactory extends AbstractRoutePredicateFactory<ConditionPredicateFactory.Config> {
    private static final String CACHE_BODY = "conditionCachedBody";
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    public ConditionPredicateFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "Condition";
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsyncPredicate<ServerWebExchange> applyAsync(Config config) {
        return exchange -> {
            Object body = exchange.getAttribute(CACHE_BODY);
            if (body != null) {
                return Mono.just(config.test(exchange, body));
            } else {
                return ServerWebExchangeUtils.cacheRequestBodyAndRequest(
                    exchange,
                    (request) -> ServerRequest
                        .create(exchange.mutate().request(request).build(), messageReaders)
                        .bodyToMono(String.class)
                        .doOnNext(value -> exchange.getAttributes().put(CACHE_BODY, value))
                        .map(value -> config.test(exchange, value))
                );
            }
        };
    }

    @Getter
    @ToString
    @Validated
    public static class Config {
        @NotEmpty
        private List<String> conditions = new ArrayList<>();

        private Boolean test(ServerWebExchange exchange, Object body) {
            try {
                RequestEntity request = RequestEntity.build(exchange, body);
                Binding binding = new Binding();
                binding.setProperty("request", request);
                for (String condition : conditions) {
                    if (isMatch(condition, binding)) {
                        return true;
                    }
                }
            } catch (Throwable e) {
                ErrorUtil.log(log, e, "test body failed. exchange={} body={}", exchange.getRequest().getURI(), body);
            }
            return false;
        }

        private boolean isMatch(String condition, Binding binding) {
            try {
                Object value = GroovyScriptRunner.getInstance().run(condition, binding);
                if (value instanceof Boolean) {
                    return (Boolean)value;
                }
            } catch (NullPointerException e) {
                // 空指针基本是body结构与条件表达式的预期结构不符合
            } catch (Throwable e) {
                ErrorUtil.log(log, e, "test condition failed. condition={} binding={}", condition, binding);
            }
            return false;
        }
    }
}
