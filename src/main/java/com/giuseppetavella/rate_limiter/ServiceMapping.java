package com.giuseppetavella.rate_limiter;

public record ServiceMapping(
    String serviceName,
    String incomingEndpoint,
    int maxEvents,
    long window,
    String serviceUrl
) {
    
}
