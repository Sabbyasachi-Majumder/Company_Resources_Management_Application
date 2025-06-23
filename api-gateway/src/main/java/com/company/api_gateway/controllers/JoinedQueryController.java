package com.company.api_gateway.controllers;

import com.company.api_gateway.beans.JoinedQueryRequestBean;
import com.company.api_gateway.beans.JoinedQueryResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

// Controller for handling joined queries across microservices
// Purpose: Combines JSON responses from employee, department, project, and job-details services
@RestController
public class JoinedQueryController {

    @Autowired
    private final WebClient webClient;

    public JoinedQueryController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:").build();
    }

    // URLs of the four standalone applications (replace with actual URLs)
    private static final String SERVICE_1_URL = "http://localhost:8081/";
    private static final String SERVICE_2_URL = "http://localhost:8082/";
    private static final String SERVICE_3_URL = "http://localhost:8083/";
    private static final String SERVICE_4_URL = "http://localhost:8084/";

    @GetMapping(value = "/testPostmanToApplicationConnection")
    public ResponseEntity<JoinedQueryResponseBean> testPostmanToApplicationConnection() {
        JoinedQueryResponseBean jrBean = new JoinedQueryResponseBean();
        jrBean.setResponseMessage("Success");
        jrBean.setResponseStatusCode(HttpStatus.OK);

        return ResponseEntity
                .status(jrBean.getResponseStatusCode()) // Set status code
                .header("URL", "/testPostmanToApplicationConnection")
                .body(jrBean);
    }

    @GetMapping(value = "/testEmployeeTestPostmanToApplicationConnection")
    public Mono<ResponseEntity<JoinedQueryResponseBean>> callEmployeeTestPostmanToApplicationConnection() {
        return webClient.get()
                .uri("/testConnection")
                .retrieve()
                .toEntity(JoinedQueryResponseBean.class)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JoinedQueryResponseBean("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null))));
    }

    @GetMapping(value = "/testEmployeeDataBaseConnection")
    public Mono<ResponseEntity<JoinedQueryResponseBean>> callEmployeeTestDataBaseConnection() {
        return webClient.get()
                .uri("8081"+"/testDataBaseConnection")
                .retrieve()
                .toEntity(JoinedQueryResponseBean.class)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JoinedQueryResponseBean("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null))));
    }

    @PostMapping(value = "/searchEmployeeDataDetails" ,consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<JoinedQueryResponseBean>> searchEmployeeDataDetails(@RequestBody JoinedQueryRequestBean jqBean) {
        Mono<ResponseEntity<JoinedQueryResponseBean>> employee= callService(
                SERVICE_1_URL+"searchEmployeeData", jqBean);

        Mono<ResponseEntity<JoinedQueryResponseBean>> project= callService(
                SERVICE_2_URL+"searchProjectData", jqBean);

        // Zip all responses
        return Mono.zip(employee, project)
                .map(tuple -> {
                    // Extract the body from each ResponseEntity
                    JoinedQueryResponseBean employeeResponse = tuple.getT1().getBody();
                    JoinedQueryResponseBean projectResponse = tuple.getT2().getBody();

                    // Combine the responses into a single JoinedQueryResponseBean
                    // Assuming JoinedQueryResponseBean has a way to merge data (e.g., a Map or generic data field)
                    JoinedQueryResponseBean combinedResponse = new JoinedQueryResponseBean(
                            "Combined response",
                            HttpStatus.OK,
                            null, // Adjust based on your JoinedQueryResponseBean structure
                            null  // Adjust based on your JoinedQueryResponseBean structure
                    );

                    // Check if any response has an
                    if (employeeResponse.getResponseStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR ||
                            projectResponse.getResponseStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new JoinedQueryResponseBean(
                                        "One or more requests failed",
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        null,
                                        null));
                    }

                    return ResponseEntity.ok(combinedResponse);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JoinedQueryResponseBean(
                                "Error combining responses: " + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                null,
                                null))));
    }

    private Mono<ResponseEntity<JoinedQueryResponseBean>> callService(String uri,JoinedQueryRequestBean jqBean) {
        return webClient.post()
                .uri(uri)
                .bodyValue(jqBean)
                .retrieve()
                .toEntity(JoinedQueryResponseBean.class)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JoinedQueryResponseBean("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null))));
    }
}