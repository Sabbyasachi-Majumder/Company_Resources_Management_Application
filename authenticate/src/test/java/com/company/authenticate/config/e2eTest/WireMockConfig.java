package com.company.authenticate.config.e2eTest;

import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import javax.annotation.PostConstruct;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@AutoConfigureWireMock(port = 9999)
public class WireMockConfig {
    @PostConstruct
    public void setupStubs() {
        stubFor(get(urlEqualTo("/external/service"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\": \"success\"}")));
    }
}