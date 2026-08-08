package com.giuseppetavella.rate_limiter.libs;

public class ChronologyInvariantViolatedException extends RuntimeException {
    public ChronologyInvariantViolatedException() {
        super("Chronology invariant violated.");
    }
}
