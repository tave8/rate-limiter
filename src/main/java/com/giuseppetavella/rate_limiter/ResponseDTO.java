package com.giuseppetavella.rate_limiter;

/**
 * The standard response DTO to each request.
 * Service-specific DTOs can be added to and are found
 * at the <code>responseDTO.details</code> field.
 * 
 * @param <T> an optional, service-specific response DTO 
 *           found at the <code>responseDTO.details</code> field
 */
public class ResponseDTO<T extends ServiceDetailsDTO> {
    private final int statusCode;
    private final String text;
    private final T serviceDetails;
    
    public ResponseDTO(int statusCode, 
                       String text,
                       T serviceDetails) 
    {
        this.statusCode = statusCode;
        this.text = text;
        this.serviceDetails = serviceDetails;
    }
    
    public ResponseDTO(int statusCode,
                       String text)
    {
        this(statusCode, text, null);
    }

    public T getServiceDetails() {
        return serviceDetails;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getText() {
        return text;
    }
}
