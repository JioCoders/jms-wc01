package com.harry.departmentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.harry.departmentservice.client.UserClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFuntion;

    @Bean
    public WebClient userWebClient() {

        return WebClient.builder()
                .baseUrl("http://user-service").filter(filterFuntion).build();
    }

    @Bean
    public UserClient UserClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(userWebClient()))
                .build();

        return factory.createClient(UserClient.class);
    }
}
