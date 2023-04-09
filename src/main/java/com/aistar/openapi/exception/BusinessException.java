package com.aistar.openapi.exception;



@SuppressWarnings("all")
public class BusinessException extends RuntimeException {


    private String message;


    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    public BusinessException(final String message) {
        super(message);
        this.message = message;

    }
}
