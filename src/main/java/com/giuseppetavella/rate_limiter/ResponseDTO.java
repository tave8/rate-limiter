package com.giuseppetavella.rate_limiter;

public class ResponseDTO<T> {
    private final int statusCode;
    private final String text;
    private final T more;
    
    public ResponseDTO(int statusCode, 
                       String text,
                       T more) 
    {
        this.statusCode = statusCode;
        this.text = text;
        this.more = more;
    }
    
    public ResponseDTO(int statusCode,
                       String text)
    {
        this(statusCode, text, null);
    }

    public T getMore() {
        return more;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getText() {
        return text;
    }
}
