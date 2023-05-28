package com.example.idp.domain.configuration;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.messaging.ScopeAwareProvider;
import org.axonframework.tracing.SpanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public DeadlineManager deadlineManager(
            org.axonframework.config.Configuration configuration,
            TransactionManager transactionManager,
            SpanFactory spanFactory
    ){
        ScopeAwareProvider scopeAwareProvider = new ConfigurationScopeAwareProvider(configuration);
        return SimpleDeadlineManager.builder()
                .scopeAwareProvider(scopeAwareProvider)
                .transactionManager(transactionManager)
                .spanFactory(spanFactory).build();
    }

}