package com.giuseppetavella.rate_limiter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class Controller {
    
    private final RestTemplate restTemplate;
    
    public Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @GetMapping
    public String checkServerOk() {
        return "rate limiter - server running";
    }

    @PostMapping("/{serviceEndpointWithoutSlash}")
    public ResponseEntity<byte[]> handleService(
            @PathVariable String serviceEndpointWithoutSlash,
            @RequestBody(required = false) byte[] rawBytes,
            HttpServletRequest request)
    {
        var serviceHistory = ServiceHistories.getByEndpoint("/" + serviceEndpointWithoutSlash);
        serviceHistory.history().add(); // Rate limiter

        String serviceUrl = serviceHistory.mapping().serviceUrl();

        // 1. Extract and forward incoming HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                headers.addAll(headerName, Collections.list(request.getHeaders(headerName)))
        );
        // Optional: remove host header so restTemplate sets the target server host correctly
        headers.remove(HttpHeaders.HOST);

        // 2. Wrap body + headers into an HttpEntity
        HttpEntity<byte[]> entity = new HttpEntity<>(rawBytes, headers);

        // 3. Execute request and mirror the exact status, body, and headers back to client
        return restTemplate.exchange(
                serviceUrl,
                HttpMethod.POST,
                entity,
                byte[].class
        );
    }
    
}
