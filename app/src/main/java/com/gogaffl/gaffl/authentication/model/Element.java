package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element {
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("handle~")
    @Expose
    private HandleEmail handleEmail;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public HandleEmail getHandleEmail() {
        return handleEmail;
    }

    public void setHandleEmail(HandleEmail handleEmail) {
        this.handleEmail = handleEmail;
    }
}
