package com.gogaffl.gaffl.home.model;

import java.util.ArrayList;

public class Trips {


    private String title;
    private String description;
    private String place_name;
    private String start_date;
    private String end_date;
    private boolean date_flexible;
    private int id;
    private String creator_name;
    private String creator_rating;
    private String creator_picture_url;
    private String creator_profile_url;
    private ArrayList<JoinedUsers> joined_users;

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

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public boolean isDate_flexible() {
        return date_flexible;
    }

    public void setDate_flexible(boolean date_flexible) {
        this.date_flexible = date_flexible;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreator_rating() {
        return creator_rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreator_rating(String creator_rating) {
        this.creator_rating = creator_rating;
    }

    public String getCreator_picture_url() {
        return creator_picture_url;
    }

    public void setCreator_picture_url(String creator_picture_url) {
        this.creator_picture_url = creator_picture_url;
    }

    public String getCreator_profile_url() {
        return creator_profile_url;
    }

    public void setCreator_profile_url(String creator_profile_url) {
        this.creator_profile_url = creator_profile_url;
    }

    public ArrayList<JoinedUsers> getJoined_users() {
        return joined_users;
    }

    public void setJoined_users(ArrayList<JoinedUsers> joined_users) {
        this.joined_users = joined_users;
    }

}
