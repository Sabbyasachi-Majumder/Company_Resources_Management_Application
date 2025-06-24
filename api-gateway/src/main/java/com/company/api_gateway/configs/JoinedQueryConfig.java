package com.company.api_gateway.configs;

import com.company.api_gateway.beans.JoinedQueryRequestBean;
import com.company.api_gateway.beans.JoinedQueryResponseBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = {"beans", "Controller"})
public class JoinedQueryConfig {
    @Bean
    public JoinedQueryRequestBean joinedQueryRequestBean(){
        return new JoinedQueryRequestBean();
    }

    @Bean
    public JoinedQueryResponseBean joinedQueryResponseBean(){
        return new JoinedQueryResponseBean();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient initilizeWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://localhost:8081/").build();
    }

}
