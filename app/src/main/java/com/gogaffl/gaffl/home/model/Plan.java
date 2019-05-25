
package com.gogaffl.gaffl.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Plan {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("date_flexible")
    @Expose
    private Boolean dateFlexible;
    @SerializedName("trip_type")
    @Expose
    private String tripType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("meetup_point")
    @Expose
    private String meetupPoint;
    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("joined_users")
    @Expose
    private ArrayList<JoinedUser> joinedUsers = null;
    @SerializedName("plan_views")
    @Expose
    private PlanViews planViews;
    @SerializedName("nearby_gafflers")
    @Expose
    private ArrayList<NearbyGaffler> nearbyGafflers = null;
    @SerializedName("pending_requests")
    @Expose
    private ArrayList<PendingRequest> pendingRequests = null;
    @SerializedName("eligibility")
    @Expose
    private Eligibility eligibility;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetupPoint() {
        return meetupPoint;
    }

    public void setMeetupPoint(String meetupPoint) {
        this.meetupPoint = meetupPoint;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public ArrayList<JoinedUser> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(ArrayList<JoinedUser> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public PlanViews getPlanViews() {
        return planViews;
    }

    public void setPlanViews(PlanViews planViews) {
        this.planViews = planViews;
    }

    public ArrayList<NearbyGaffler> getNearbyGafflers() {
        return nearbyGafflers;
    }

    public void setNearbyGafflers(ArrayList<NearbyGaffler> nearbyGafflers) {
        this.nearbyGafflers = nearbyGafflers;
    }

    public ArrayList<PendingRequest> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(ArrayList<PendingRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public Eligibility getEligibility() {
        return eligibility;
    }

    public void setEligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
    }


}
