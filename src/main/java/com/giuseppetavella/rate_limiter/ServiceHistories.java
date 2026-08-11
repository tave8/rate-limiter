package com.giuseppetavella.rate_limiter;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceHistories {
    private static List<ServiceRateLimiter> instance;
    
    public static List<ServiceRateLimiter> getInstance() {
        if(instance == null) {
            throw new RuntimeException("instance is null");
        }
        return instance;
    }
    
    public static void setInstance(List<ServiceRateLimiter> list) {
        if(instance != null) {
            throw new RuntimeException("instance was already set.");
        }
        instance = list;
    }

    /**
     * Get the history of the service that was requested at the given endpoint.
     * The history is in-memory and each service has its own.
     * 
     * @return
     */
    public static ServiceRateLimiter getByEndpoint(String endpoint) 
                                                throws EndpointNotMappedToServiceException
    {
        for(var serviceHistory : instance) {
            var match = serviceHistory.mapping().incomingEndpoint().equals(endpoint);
            if(match) {
                return serviceHistory;
            }
        }
        throw new EndpointNotMappedToServiceException(endpoint);
    }
    
}
