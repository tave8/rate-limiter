package com.giuseppetavella.rate_limiter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class ServiceHistories {
    private static List<ServiceHistory> instance;
    
    public static List<ServiceHistory> getInstance() {
        if(instance == null) {
            throw new RuntimeException("instance is null");
        }
        return instance;
    }
    
    public static void setInstance(List<ServiceHistory> list) {
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
    public static ServiceHistory getByEndpoint(String endpoint) 
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
