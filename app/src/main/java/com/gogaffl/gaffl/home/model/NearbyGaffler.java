package com.gogaffl.gaffl.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearbyGaffler {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;
    @SerializedName("travel_status")
    @Expose
    private String travelStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }
}
