package com.giuseppetavella.rate_limiter;

public class EndpointNotMappedToServiceException extends RuntimeException {
    public EndpointNotMappedToServiceException(String endpoint) {
        super("The endpoint %s does not seem to be mapped to a service.".formatted(endpoint));
    }
}
