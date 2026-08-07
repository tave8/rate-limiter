package com.giuseppetavella.rate_limiter;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Controller {
    
    @GetMapping
    public String checkServerOk() {
        return "server running";
    }
    
    @PostMapping("/{serviceEndpointWithoutSlash}")
    public String handleService(
            @PathVariable String serviceEndpointWithoutSlash) 
    {
        
        var serviceHistory = ServiceHistories.getByEndpoint("/"+serviceEndpointWithoutSlash);
        
        serviceHistory.history().add();
        
        // System.out.println(serviceHistory);
        
        return "called service %s".formatted(serviceHistory.mapping().serviceName());
    }
    
}
