package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter.libs.HistoryQueue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
  
@Component
public class OnStartup implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    
    public OnStartup(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void run(String... args) throws Exception {

        // Load JSON of service mappings from src/main/resources/service_mappings.json
        ClassPathResource resource = new ClassPathResource("service_mappings.json");
        List<ServiceMapping> mappings;
        List<ServiceHistory> serviceHistories = new LinkedList<>();
        
        try (InputStream inputStream = resource.getInputStream()) {
            mappings = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<ServiceMapping>>() {}
            );
            
            // Verify or pass mappings to your rate limiter registry
            // mappings.forEach(mapping -> System.out.println("Loaded: " + mapping));
        }
        
        mappings.forEach(mapping -> {
            var serviceHistory = new ServiceHistory(
                    mapping, 
                    new HistoryQueue(mapping.maxEvents(), mapping.window()) 
            );
           serviceHistories.add(serviceHistory);
        });
        
        ServiceHistories.setInstance(serviceHistories);
        
        
    }
}
