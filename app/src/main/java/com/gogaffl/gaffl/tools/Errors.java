package com.gogaffl.gaffl.tools;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Errors {
    @SerializedName("mobile_no")
    @Expose
    private ArrayList<String> mobileNo;
    @SerializedName("email")
    @Expose
    private ArrayList<String> email;

    public ArrayList<String> getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(ArrayList<String> mobileNo) {
        this.mobileNo = mobileNo;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }
}
