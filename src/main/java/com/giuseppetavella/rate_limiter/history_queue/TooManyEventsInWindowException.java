package com.giuseppetavella.rate_limiter.history_queue;

public class TooManyEventsInWindowException extends RuntimeException {
    public TooManyEventsInWindowException(int maxItemsInPeriod) {
        super("Too many items in period, must wait. Max items was: " + maxItemsInPeriod);
    }
    
  public TooManyEventsInWindowException() {
    super("Too many items in period, must wait.");
  }
    
}
