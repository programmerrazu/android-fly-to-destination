package com.gogaffl.gaffl.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review {

    //private final static long serialVersionUID = 352378361165290468L;
    @SerializedName("rater_name")
    @Expose
    private String raterName;
    @SerializedName("rater_picture_url")
    @Expose
    private String raterPictureUrl;
    @SerializedName("rating_time")
    @Expose
    private String ratingTime;
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("rating")
    @Expose
    private float rating;

    public String getRaterName() {
        return raterName;
    }

    public void setRaterName(String raterName) {
        this.raterName = raterName;
    }

    public String getRaterPictureUrl() {
        return raterPictureUrl;
    }

    public void setRaterPictureUrl(String raterPictureUrl) {
        this.raterPictureUrl = raterPictureUrl;
    }

    public String getRatingTime() {
        return ratingTime;
    }

    public void setRatingTime(String ratingTime) {
        this.ratingTime = ratingTime;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}