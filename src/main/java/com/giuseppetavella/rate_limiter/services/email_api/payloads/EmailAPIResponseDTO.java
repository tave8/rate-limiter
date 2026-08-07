package com.giuseppetavella.rate_limiter.services.email_api.payloads;

public record EmailAPIResponseDTO(
       int statusCode,
       String text
) {
}
