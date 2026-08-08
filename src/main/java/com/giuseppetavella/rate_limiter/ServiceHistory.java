package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter.libs.HistoryQueue;

public record ServiceHistory(
        ServiceMapping mapping,
        HistoryQueue history
) {
}
