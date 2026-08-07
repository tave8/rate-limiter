package com.giuseppetavella.rate_limiter;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Controller {
    
    @PostMapping("/{serviceEndpointWithoutSlash}")
    public String handleService(
            @PathVariable String serviceEndpointWithoutSlash) 
    {
        var serviceHistory = ServiceHistories.getByEndpoint("/"+serviceEndpointWithoutSlash);
        
        System.out.println(serviceHistory);
        
        return "server running";
    }
    
}
