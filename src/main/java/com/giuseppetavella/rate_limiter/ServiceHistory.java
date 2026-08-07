package com.giuseppetavella.rate_limiter;


import com.giuseppetavella.rate_limiter_algo.HistoryQueue;

public record ServiceHistory(
        ServiceMapping mapping,
        HistoryQueue history
) {
}
