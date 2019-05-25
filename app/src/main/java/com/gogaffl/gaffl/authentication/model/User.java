package com.gogaffl.gaffl.authentication.model;


import android.net.Uri;

public class User {

    private static String firstName;
    private static String lastName;
    private static String email;
    private static String password;
    private static String gender;
    private static String age;
    private static String uid;
    private static Uri fileUri;
    private static String provider;
    private static boolean socialStatus;

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        User.lastName = lastName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        User.gender = gender;
    }

    public static String getAge() {
        return age;
    }

    public static void setAge(String age) {
        User.age = age;
    }

    public static Uri getFileUri() {
        return fileUri;
    }

    public static void setFileUri(Uri fileUri) {
        User.fileUri = fileUri;
    }

    public static boolean isSocialStatus() {
        return socialStatus;
    }

    public static void setSocialStatus(boolean socialStatus) {
        User.socialStatus = socialStatus;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        User.uid = uid;
    }

    public static String getProvider() {
        return provider;
    }

    public static void setProvider(String provider) {
        User.provider = provider;
    }
}