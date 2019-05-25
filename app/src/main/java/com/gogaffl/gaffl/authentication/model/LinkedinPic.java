
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinPic {

    @SerializedName("profilePicture")
    @Expose
    private ProfilePicture profilePicture;
    @SerializedName("id")
    @Expose
    private String id;

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
