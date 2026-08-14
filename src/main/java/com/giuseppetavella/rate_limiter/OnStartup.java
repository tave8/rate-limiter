package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter_algo.RateLimiter;
import com.giuseppetavella.rate_limiter_algo.timeline.RateLimiterSpeed;
import com.giuseppetavella.rate_limiter_algo.timeline.TimelineRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * On server startup, load the json containing mappings 
 * of the services to be rate limited to their endpoint.
 */
@Component
public class OnStartup implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OnStartup.class);
    
    private final ObjectMapper objectMapper;

    
    
    public OnStartup(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        log.info("Loading service to rate limiter mappings...");

        ClassPathResource resource = new ClassPathResource("service_mappings.json");
        List<ServiceMapping> mappings;
        List<ServiceRateLimiter> serviceRateLimiters = new LinkedList<>();
        
        try (InputStream inputStream = resource.getInputStream()) {
            // Deserialize json
            mappings = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<ServiceMapping>>() {}
            );
        }
        
        log.info("Building rate limiters...");
        
        // For each user-defined mapping, create a rate limiter and
        // associate it with an endpoint, so from the endpoint 
        // we can get the associated rate limiter
        mappings.forEach(mapping -> {
            var limiter = new TimelineRateLimiter.Builder(mapping.maxEvents(), mapping.window())
                    .speed(RateLimiterSpeed.NORMAL)
                    .build();
            
            limiter.start();
            
            var serviceHistory = new ServiceRateLimiter(
                    mapping, 
                    limiter 
            );
            
           serviceRateLimiters.add(serviceHistory);
        });
        
        ServiceRateLimiters.setInstance(serviceRateLimiters);

        log.info("Rate limiters are operational.");
        
    }
}
