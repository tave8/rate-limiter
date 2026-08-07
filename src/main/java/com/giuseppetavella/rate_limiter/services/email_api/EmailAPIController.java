package com.giuseppetavella.rate_limiter.services.email_api;

import com.giuseppetavella.rate_limiter.ResponseDTO;
import com.giuseppetavella.rate_limiter.services.email_api.payloads.EmailAPIRequestDTO;
import com.giuseppetavella.rate_limiter.services.email_api.payloads.EmailAPIResponseDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-api")
public class EmailAPIController {
    
    @PostMapping("/")
    public ResponseDTO<EmailAPIResponseDTO> handleEmailAPI(
            @RequestBody @Validated EmailAPIRequestDTO body,
            BindingResult validation) 
    {
        return new ResponseDTO<>(
                200, "hi"
        );
    }
    
}
