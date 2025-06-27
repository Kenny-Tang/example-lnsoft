package com.redjujubetree.response;

public class ExceptionResponse extends BaseResponse{
    private String path;
    private String timestamp;
    public ExceptionResponse(int code, String message) {
        super(code, message);
    }
    public ExceptionResponse(int code, String message, String path, String timestamp) {
        super(code, message);
        this.path = path;
        this.timestamp = timestamp;
    }

    public static ExceptionResponse error(int code, String message, String path) {
        return new ExceptionResponse(code, message, path, now());
    }

    private static String now() {
        return java.time.LocalDateTime.now().toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
