
package com.gogaffl.gaffl.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileResponse {

    @SerializedName("user")
    @Expose
    private User user;
    //private final static long serialVersionUID = 5644936472171922519L;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}