package com.afterhoursdev.scavengerhunt.hunt.exception;

public class ErrorMessage {
    private final int httpStatusCode;
    private final String error;
    private final String message;

    // Private constructor to enforce the use of Builder
    private ErrorMessage(Builder builder) {
        this.httpStatusCode = builder.httpStatusCode;
        this.error = builder.error;
        this.message = builder.message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    // Builder Class
    public static class Builder {
        private int httpStatusCode;
        private String error;
        private String message;

        public Builder httpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorMessage build() {
            return new ErrorMessage(this);
        }
    }

}
