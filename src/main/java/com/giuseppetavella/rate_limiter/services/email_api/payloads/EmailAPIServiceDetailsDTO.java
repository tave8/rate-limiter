package com.giuseppetavella.rate_limiter.services.email_api.payloads;

import com.giuseppetavella.rate_limiter.ServiceDetailsDTO;

public record EmailAPIServiceDetailsDTO(
        String text
) implements ServiceDetailsDTO {
}
