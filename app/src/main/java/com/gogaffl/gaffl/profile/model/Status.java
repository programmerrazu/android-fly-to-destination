
package com.gogaffl.gaffl.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status {

    @SerializedName("id_verified")
    @Expose
    private Boolean idVerified;
    @SerializedName("id_verification_pending")
    @Expose
    private Boolean idVerificationPending;
    @SerializedName("facebook")
    @Expose
    private Boolean facebook;
    @SerializedName("google")
    @Expose
    private Boolean google;
    @SerializedName("linkedin")
    @Expose
    private Boolean linkedin;
    @SerializedName("phone_verified")
    @Expose
    private Boolean phoneVerified;
    @SerializedName("basic_info")
    @Expose
    private Boolean basicInfo;
    @SerializedName("profile_info")
    @Expose
    private Boolean profileInfo;
    @SerializedName("interest_and_about")
    @Expose
    private Boolean interestAndAbout;
    //private final static long serialVersionUID = -2128996710243455175L;

    public Boolean getIdVerified() {
        return idVerified;
    }

    public void setIdVerified(Boolean idVerified) {
        this.idVerified = idVerified;
    }

    public Boolean getIdVerificationPending() {
        return idVerificationPending;
    }

    public void setIdVerificationPending(Boolean idVerificationPending) {
        this.idVerificationPending = idVerificationPending;
    }

    public Boolean getFacebook() {
        return facebook;
    }

    public void setFacebook(Boolean facebook) {
        this.facebook = facebook;
    }

    public Boolean getGoogle() {
        return google;
    }

    public void setGoogle(Boolean google) {
        this.google = google;
    }

    public Boolean getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(Boolean linkedin) {
        this.linkedin = linkedin;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(Boolean basicInfo) {
        this.basicInfo = basicInfo;
    }

    public Boolean getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(Boolean profileInfo) {
        this.profileInfo = profileInfo;
    }

    public Boolean getInterestAndAbout() {
        return interestAndAbout;
    }

    public void setInterestAndAbout(Boolean interestAndAbout) {
        this.interestAndAbout = interestAndAbout;
    }

}