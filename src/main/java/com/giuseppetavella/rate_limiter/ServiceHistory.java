package com.giuseppetavella.rate_limiter;

import com.giuseppetavella.rate_limiter.history_queue.HistoryQueue;

public record ServiceHistory(
        ServiceMapping mapping,
        HistoryQueue history
) {
}
