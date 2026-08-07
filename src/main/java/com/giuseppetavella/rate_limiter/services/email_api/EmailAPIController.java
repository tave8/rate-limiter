package com.giuseppetavella.rate_limiter.services.email_api;

import com.giuseppetavella.rate_limiter.ResponseDTO;
import com.giuseppetavella.rate_limiter.services.email_api.payloads.EmailAPIRequestDTO;
import com.giuseppetavella.rate_limiter.services.email_api.payloads.EmailAPIResponseDTO;
import com.giuseppetavella.rate_limiter.services.email_api.payloads.EmailAPIServiceDetailsDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-api")
public class EmailAPIController {
    
    @PostMapping
    public ResponseDTO<EmailAPIServiceDetailsDTO> handleEmailAPI(
            @RequestBody @Validated EmailAPIRequestDTO body,
            BindingResult validation) 
    {
        
        // Request Email API service
        // var requestDTO = new Request
        
        // var emailAPI_DTO = new EmailAPIResponseDTO() 
                
        return new ResponseDTO<>(
                200, 
                "hi",
                new EmailAPIServiceDetailsDTO("email sent")
        );
    }
    
}
