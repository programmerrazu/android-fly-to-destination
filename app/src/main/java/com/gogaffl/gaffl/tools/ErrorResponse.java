package com.gogaffl.gaffl.tools;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private Errors error;

    public String getMessage() {
        return message;
    }

    public Errors getError() {
        return error;
    }
}
