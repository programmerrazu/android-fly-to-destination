
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisplayAspectRatio {

    @SerializedName("widthAspect")
    @Expose
    private Integer widthAspect;
    @SerializedName("heightAspect")
    @Expose
    private Integer heightAspect;
    @SerializedName("formatted")
    @Expose
    private String formatted;

    public Integer getWidthAspect() {
        return widthAspect;
    }

    public void setWidthAspect(Integer widthAspect) {
        this.widthAspect = widthAspect;
    }

    public Integer getHeightAspect() {
        return heightAspect;
    }

    public void setHeightAspect(Integer heightAspect) {
        this.heightAspect = heightAspect;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

}
