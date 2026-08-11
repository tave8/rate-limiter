package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter_algo.RateLimiter;

public record ServiceRateLimiter(
        ServiceMapping mapping,
        RateLimiter limiter
) {
}
