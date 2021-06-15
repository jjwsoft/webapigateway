package org.feiquan.webapigateway.impl.predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义断言器注册
 * @author junwei.jjw
 * @date 2021/6/14
 */
@Configuration
public class CustomPredicatesConfig {
    @Bean
    public ConditionPredicateFactory conditionPredicate() {
        return new ConditionPredicateFactory();
    }
}
