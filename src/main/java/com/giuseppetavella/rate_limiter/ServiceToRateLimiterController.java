package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter_algo.EventRejectedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/")
public class ServiceToRateLimiterController {
    
    private final RestTemplate restTemplate;
    
    public ServiceToRateLimiterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @GetMapping
    public String checkServerOk() {
    
        return "rater limiter: middleman. up and running.";
    }

    /**
     * Maps service to rate limiter. 
     * 
     * @param serviceEndpointWithoutSlash
     * @param rawBytes
     * @param request
     * @return
     */
    @PostMapping("/{serviceEndpointWithoutSlash}")
    public ResponseEntity<byte[]> handleService(
            @PathVariable String serviceEndpointWithoutSlash,
            @RequestBody(required = false) byte[] rawBytes,
            HttpServletRequest request)
    {
        // Get the pair rate limiter and the service info based on endpoint
        var serviceRateLimiter = ServiceRateLimiters.getByEndpoint("/" + serviceEndpointWithoutSlash);
        // The service associated to this endpoint
        var service = serviceRateLimiter.mapping();
        // The rate limiter associated to the service associated to this endpoint
        var limiter = serviceRateLimiter.limiter();
        
        // Try adding new event
        if( !limiter.add() ) {
            // Rate limiter rejected new event, so end request
            throw new EventRejectedException("Too many requests (Rate Limiter middleman). Reason: %s".formatted(limiter.getRejectionReason()));
        }

        // Rate limiter added event, so request can be forwarded
        // to destination service 
        String serviceUrl = service.serviceUrl();

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
