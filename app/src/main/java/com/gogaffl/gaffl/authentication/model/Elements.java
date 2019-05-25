
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Elements {

    @SerializedName("artifact")
    @Expose
    private String artifact;
    @SerializedName("authorizationMethod")
    @Expose
    private String authorizationMethod;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("identifiers")
    @Expose
    private List<Identifier> identifiers = null;

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public String getAuthorizationMethod() {
        return authorizationMethod;
    }

    public void setAuthorizationMethod(String authorizationMethod) {
        this.authorizationMethod = authorizationMethod;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

}
