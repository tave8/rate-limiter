package com.giuseppetavella.rate_limiter;

/**
 * The standard response DTO to each request.
 * Service-specific DTOs can be added to and are found
 * at the <code>responseDTO.details</code> field.
 *
 */
public class ResponseDTO {
    private final int statusCode;
    private final Object serviceDetails;
    
    public ResponseDTO(int statusCode,
                       Object serviceDetails) 
    {
        this.statusCode = statusCode;
        this.serviceDetails = serviceDetails;
    }
    
    public ResponseDTO(int statusCode)
    {
        this(statusCode, null);
    }

    public Object getServiceDetails() {
        return serviceDetails;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
}
