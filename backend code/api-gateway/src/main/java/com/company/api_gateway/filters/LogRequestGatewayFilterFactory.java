package com.company.api_gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class LogRequestGatewayFilterFactory extends AbstractGatewayFilterFactory<LogRequestGatewayFilterFactory.Config> {
    private static final Logger logger = LoggerFactory.getLogger(LogRequestGatewayFilterFactory.class);

    public LogRequestGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("Request received: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}