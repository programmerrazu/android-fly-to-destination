
package com.gogaffl.gaffl.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private final static long serialVersionUID = 8743548185062574843L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("joined_in")
    @Expose
    private String joinedIn;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("response_time")
    @Expose
    private Integer responseTime;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("interests")
    @Expose
    private ArrayList<Interest> interests = null;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("reviews")
    @Expose
    private ArrayList<Review> reviews = null;
    @SerializedName("created_trips")
    @Expose
    private ArrayList<CreatedTrip> createdTrips = null;
    @SerializedName("joined_trips")
    @Expose
    private ArrayList<JoinedTrip> joinedTrips = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoinedIn() {
        return joinedIn;
    }

    public void setJoinedIn(String joinedIn) {
        this.joinedIn = joinedIn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<Interest> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interest> interests) {
        this.interests = interests;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<CreatedTrip> getCreatedTrips() {
        return createdTrips;
    }

    public void setCreatedTrips(ArrayList<CreatedTrip> createdTrips) {
        this.createdTrips = createdTrips;
    }
    public ArrayList<JoinedTrip> getJoinedTrips() {
        return joinedTrips;
    }

    public void setJoinedTrips(ArrayList<JoinedTrip> joinedTrips) {
        this.joinedTrips = joinedTrips;
    }
}