
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Identifier {

    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("identifierType")
    @Expose
    private String identifierType;
    @SerializedName("identifierExpiresInSeconds")
    @Expose
    private Integer identifierExpiresInSeconds;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public Integer getIdentifierExpiresInSeconds() {
        return identifierExpiresInSeconds;
    }

    public void setIdentifierExpiresInSeconds(Integer identifierExpiresInSeconds) {
        this.identifierExpiresInSeconds = identifierExpiresInSeconds;
    }

}
