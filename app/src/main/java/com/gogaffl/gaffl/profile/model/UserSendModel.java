package com.gogaffl.gaffl.profile.model;

import java.util.ArrayList;

public class UserSendModel {
    private String name;
    private String picture;
    private int countryID;
    private int stateID;
    private String gender;
    private String date;
    private String about;
    private static String email;
    private static int uid;
    private static boolean phoneEdit;
    private static boolean interestEdit;
    private static String token;
    private String title;
    private static int screenValue;
    private static boolean updateCache;
    private static boolean dataLoadFailed;
    private ArrayList<Interest> interests = null;


    public UserSendModel(String about) {
        this.about = about;
    }

    public UserSendModel(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public UserSendModel(int countryID, int stateID, String gender, String date) {
        this.countryID = countryID;
        this.stateID = stateID;
        this.gender = gender;
        this.date = date;
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

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserSendModel.email = email;
    }

    public static int getUid() {
        return uid;
    }

    public static void setUid(int uid) {
        UserSendModel.uid = uid;
    }

    public ArrayList<Interest> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interest> interests) {
        this.interests = interests;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserSendModel.token = token;
    }

    public static boolean isPhoneEdit() {
        return phoneEdit;
    }

    public static void setPhoneEdit(boolean phoneEdit) {
        UserSendModel.phoneEdit = phoneEdit;
    }

    public static boolean isInterestEdit() {
        return interestEdit;
    }

    public static void setInterestEdit(boolean interestEdit) {
        UserSendModel.interestEdit = interestEdit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static boolean isUpdateCache() {
        return updateCache;
    }

    public static void setUpdateCache(boolean updateCache) {
        UserSendModel.updateCache = updateCache;
    }

    public static boolean isDataLoadFailed() {
        return dataLoadFailed;
    }

    public static void setDataLoadFailed(boolean dataLoadFailed) {
        UserSendModel.dataLoadFailed = dataLoadFailed;
    }

    public static int getScreenValue() {
        return screenValue;
    }

    public static void setScreenValue(int screenValue) {
        UserSendModel.screenValue = screenValue;
    }
}
