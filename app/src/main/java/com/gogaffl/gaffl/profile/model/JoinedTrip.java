package com.gogaffl.gaffl.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JoinedTrip {

    //private final static long serialVersionUID = -8994236400914265388L;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("date_flexible")
    @Expose
    private Boolean dateFlexible;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("creator_name")
    @Expose
    private String creatorName;
    @SerializedName("creator_rating")
    @Expose
    private String creatorRating;
    @SerializedName("creator_picture_url")
    @Expose
    private String creatorPictureUrl;
    @SerializedName("creator_profile_url")
    @Expose
    private String creatorProfileUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getDateFlexible() {
        return dateFlexible;
    }

    public void setDateFlexible(Boolean dateFlexible) {
        this.dateFlexible = dateFlexible;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorRating() {
        return creatorRating;
    }

    public void setCreatorRating(String creatorRating) {
        this.creatorRating = creatorRating;
    }

    public String getCreatorPictureUrl() {
        return creatorPictureUrl;
    }

    public void setCreatorPictureUrl(String creatorPictureUrl) {
        this.creatorPictureUrl = creatorPictureUrl;
    }

    public String getCreatorProfileUrl() {
        return creatorProfileUrl;
    }

    public void setCreatorProfileUrl(String creatorProfileUrl) {
        this.creatorProfileUrl = creatorProfileUrl;
    }
}