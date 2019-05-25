
package com.gogaffl.gaffl.notification.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed implements Parcelable {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("feed_id")
    @Expose
    private Integer feedId;
    @SerializedName("feed_time")
    @Expose
    private String feedTime;
    @SerializedName("feed_type")
    @Expose
    private String feedType;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_picture")
    @Expose
    private String userPicture;
    @SerializedName("user_rating")
    @Expose
    private String userRating;
    @SerializedName("user_address")
    @Expose
    private String userAddress;

    @SerializedName("user_gender")
    @Expose
    private String userGender;

    @SerializedName("user_interests")
    @Expose
    private List<String> userInterests = null;

    @SerializedName("is_read")
    @Expose
    private Boolean isRead;

    @SerializedName("user_email_verified")
    @Expose
    private Boolean userEmailVerified;

    @SerializedName("user_facebook_verified")
    @Expose
    private Boolean userFacebookVerified;
    @SerializedName("user_google_verified")
    @Expose
    private Boolean userGoogleVerified;
    @SerializedName("user_linkedin_verified")
    @Expose
    private Boolean userLinkedinVerified;
    @SerializedName("user_phone_verified")
    @Expose
    private Boolean userPhoneVerified;
    @SerializedName("trip_id")
    @Expose
    private Integer tripId;

    @SerializedName("user_age")
    @Expose
    private Integer userAge;

    @SerializedName("trip_title")
    @Expose
    private String tripTitle;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("request_note")
    @Expose
    private String requestNote;
    @SerializedName("invite_id")
    @Expose
    private Integer inviteId;
    @SerializedName("invite_note")
    @Expose
    private String inviteNote;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("cancel_note")
    @Expose
    private String cancelNote;

    public static final int TRIP_JOIN_REQUEST_TYPE = 0;
    public static final int ACCEPT_THE_JOIN_REQUEST_TYPE = 1;
    public static final int CANCEL_THE_JOIN_REQUEST_TYPE = 2;
    public static final int JOIN_THE_TRIP_TYPE = 3;
    public static final int INVITES_TO_JOIN_THE_TRIP_TYPE = 4;
    public static final int TRIP_CREATOR_EDIT_TRIP_TYPE = 5;


    protected Feed(Parcel in) {
        flag = in.readString();
        if (in.readByte() == 0) {
            feedId = null;
        } else {
            feedId = in.readInt();
        }
        feedTime = in.readString();
        feedType = in.readString();
        userName = in.readString();
        userPicture = in.readString();
        userRating = in.readString();
        userAddress = in.readString();
        userGender = in.readString();
        userInterests = in.createStringArrayList();
        byte tmpIsRead = in.readByte();
        isRead = tmpIsRead == 0 ? null : tmpIsRead == 1;
        byte tmpUserEmailVerified = in.readByte();
        userEmailVerified = tmpUserEmailVerified == 0 ? null : tmpUserEmailVerified == 1;
        byte tmpUserFacebookVerified = in.readByte();
        userFacebookVerified = tmpUserFacebookVerified == 0 ? null : tmpUserFacebookVerified == 1;
        byte tmpUserGoogleVerified = in.readByte();
        userGoogleVerified = tmpUserGoogleVerified == 0 ? null : tmpUserGoogleVerified == 1;
        byte tmpUserLinkedinVerified = in.readByte();
        userLinkedinVerified = tmpUserLinkedinVerified == 0 ? null : tmpUserLinkedinVerified == 1;
        byte tmpUserPhoneVerified = in.readByte();
        userPhoneVerified = tmpUserPhoneVerified == 0 ? null : tmpUserPhoneVerified == 1;
        if (in.readByte() == 0) {
            tripId = null;
        } else {
            tripId = in.readInt();
        }
        if (in.readByte() == 0) {
            userAge = null;
        } else {
            userAge = in.readInt();
        }

        tripTitle = in.readString();
        if (in.readByte() == 0) {
            requestId = null;
        } else {
            requestId = in.readInt();
        }
        requestNote = in.readString();
        if (in.readByte() == 0) {
            inviteId = null;
        } else {
            inviteId = in.readInt();
        }
        inviteNote = in.readString();
        cancelReason = in.readString();
        cancelNote = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getFeedId() {
        return feedId;
    }

    public void setFeedId(Integer feedId) {
        this.feedId = feedId;
    }

    public String getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(String feedTime) {
        this.feedTime = feedTime;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserAddress() {
        return userAddress;
    }
    public String getUserGender() {
        return userGender;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public List<String> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(List<String> userInterests) {
        this.userInterests = userInterests;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public Boolean getUserEmailVerified() {
        return userEmailVerified;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public void setUserEmailVerified(Boolean userEmailVerified) {
        this.userEmailVerified = userEmailVerified;
    }

    public Boolean getUserFacebookVerified() {
        return userFacebookVerified;
    }

    public void setUserFacebookVerified(Boolean userFacebookVerified) {
        this.userFacebookVerified = userFacebookVerified;
    }

    public Boolean getUserGoogleVerified() {
        return userGoogleVerified;
    }

    public void setUserGoogleVerified(Boolean userGoogleVerified) {
        this.userGoogleVerified = userGoogleVerified;
    }

    public Boolean getUserLinkedinVerified() {
        return userLinkedinVerified;
    }

    public void setUserLinkedinVerified(Boolean userLinkedinVerified) {
        this.userLinkedinVerified = userLinkedinVerified;
    }

    public Boolean getUserPhoneVerified() {
        return userPhoneVerified;
    }

    public void setUserPhoneVerified(Boolean userPhoneVerified) {
        this.userPhoneVerified = userPhoneVerified;
    }

    public Integer getTripId() {
        return tripId;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    public String getInviteNote() {
        return inviteNote;
    }

    public void setInviteNote(String inviteNote) {
        this.inviteNote = inviteNote;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelNote() {
        return cancelNote;
    }

    public void setCancelNote(String cancelNote) {
        this.cancelNote = cancelNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flag);
        if (feedId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(feedId);
        }
        dest.writeString(feedTime);
        dest.writeString(feedType);
        dest.writeString(userName);
        dest.writeString(userPicture);
        dest.writeString(userRating);
        dest.writeString(userAddress);
        dest.writeString(userGender);
        dest.writeStringList(userInterests);
        dest.writeByte((byte) (isRead == null ? 0 : isRead ? 1 : 2));
        dest.writeByte((byte) (userEmailVerified == null ? 0 : userEmailVerified ? 1 : 2));
        dest.writeByte((byte) (userFacebookVerified == null ? 0 : userFacebookVerified ? 1 : 2));
        dest.writeByte((byte) (userGoogleVerified == null ? 0 : userGoogleVerified ? 1 : 2));
        dest.writeByte((byte) (userLinkedinVerified == null ? 0 : userLinkedinVerified ? 1 : 2));
        dest.writeByte((byte) (userPhoneVerified == null ? 0 : userPhoneVerified ? 1 : 2));
        if (tripId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tripId);
        }
        if (userAge == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userAge);
        }
        dest.writeString(tripTitle);
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestId);
        }
        dest.writeString(requestNote);
        if (inviteId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(inviteId);
        }
        dest.writeString(inviteNote);
        dest.writeString(cancelReason);
        dest.writeString(cancelNote);
    }
}
