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
     * Find the ServiceHistory object matching this endpoint.
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
