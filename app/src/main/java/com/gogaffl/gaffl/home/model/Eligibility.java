
package com.gogaffl.gaffl.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Eligibility {

    @SerializedName("created")
    @Expose
    private Boolean created;
    @SerializedName("joined")
    @Expose
    private Boolean joined;
    @SerializedName("request_pending")
    @Expose
    private Boolean requestPending;
    @SerializedName("invitation_pending")
    @Expose
    private Boolean invitationPending;

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(Boolean joined) {
        this.joined = joined;
    }

    public Boolean getRequestPending() {
        return requestPending;
    }

    public void setRequestPending(Boolean requestPending) {
        this.requestPending = requestPending;
    }

    public Boolean getInvitationPending() {
        return invitationPending;
    }

    public void setInvitationPending(Boolean invitationPending) {
        this.invitationPending = invitationPending;
    }

}
