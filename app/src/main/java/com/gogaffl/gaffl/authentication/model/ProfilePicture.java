
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePicture {

    @SerializedName("displayImage")
    @Expose
    private String displayImage;
    @SerializedName("displayImage~")
    @Expose
    private DisplayImageSee displayImageSee;

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public DisplayImageSee getDisplayImageSee() {
        return displayImageSee;
    }

    public void setDisplayImageSee(DisplayImageSee displayImageSee) {
        this.displayImageSee = displayImageSee;
    }

}
