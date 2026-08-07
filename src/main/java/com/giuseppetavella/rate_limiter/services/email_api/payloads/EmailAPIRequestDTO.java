package com.giuseppetavella.rate_limiter.services.email_api.payloads;

public record EmailAPIRequestDTO(
        String recipient,
        String subject,
        String body
) {
}
