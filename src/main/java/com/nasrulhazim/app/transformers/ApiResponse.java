package com.nasrulhazim.app.transformers;

public class ApiResponse {
    private String message;
    private String status;
    public ApiResponse(String message, String status)
    {
        this.message = message;
        this.status = status;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getStatus()
    {
        return this.status;
    }
}
