package com.giuseppetavella.rate_limiter;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manages all service : rate limiter pairs. This allows
 * dynamic mapping endpoint -> service -> rate limiter. 
 * So that when a http request hits the endpoint, the associated 
 * rate limiter is retrieved. Then the new event is added to the rate limiter,
 * and if ok, request is forwarded to destination service. 
 */
@Component
public class ServiceRateLimiters {
    private static List<ServiceRateLimiter> instance;
    
    public static List<ServiceRateLimiter> getInstance() {
        if(instance == null) {
            throw new RuntimeException("instance is null");
        }
        return instance;
    }

    /**
     * Should be used only once, for example at server startup. 
     * The pairs service : rate limiter are built and then set once.
     * This makes the pairs endpoint : service : rate limiter globally available.
     * 
     * @param list
     */
    public static void setInstance(List<ServiceRateLimiter> list) {
        if(instance != null) {
            throw new RuntimeException("instance was already set.");
        }
        instance = list;
    }

    /**
     * Get the pair service : rate limiter from an endpoint. 
     * Use this when a new http request comes through and you 
     * need to retrive the service info as well as the rate limiter
     * associated to {@code endpoint}.
     * 
     * @param endpoint the endpoint that was just hit (including slash), 
     *                 something like {@code /email-api}

     * @return the pair service : rate limiter associated to input endpoint
     */
    public static ServiceRateLimiter getByEndpoint(String endpoint) 
                                                throws EndpointNotMappedToServiceException
    {
        for(ServiceRateLimiter pair : instance) {
            var match = pair.mapping().incomingEndpoint().equals(endpoint);
            if(match) {
                return pair;
            }
        }
        throw new EndpointNotMappedToServiceException(endpoint);
    }
    
}
