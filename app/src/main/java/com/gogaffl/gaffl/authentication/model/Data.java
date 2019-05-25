
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("com.linkedin.digitalmedia.mediaartifact.StillImage")
    @Expose
    private ComLinkedinDigitalmediaMediaartifactStillImage comLinkedinDigitalmediaMediaartifactStillImage;

    public ComLinkedinDigitalmediaMediaartifactStillImage getComLinkedinDigitalmediaMediaartifactStillImage() {
        return comLinkedinDigitalmediaMediaartifactStillImage;
    }

    public void setComLinkedinDigitalmediaMediaartifactStillImage(ComLinkedinDigitalmediaMediaartifactStillImage comLinkedinDigitalmediaMediaartifactStillImage) {
        this.comLinkedinDigitalmediaMediaartifactStillImage = comLinkedinDigitalmediaMediaartifactStillImage;
    }

}
