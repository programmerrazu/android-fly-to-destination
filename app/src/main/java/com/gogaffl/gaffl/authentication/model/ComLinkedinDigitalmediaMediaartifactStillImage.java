
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComLinkedinDigitalmediaMediaartifactStillImage {

    @SerializedName("storageSize")
    @Expose
    private StorageSize storageSize;
    @SerializedName("storageAspectRatio")
    @Expose
    private StorageAspectRatio storageAspectRatio;
    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("rawCodecSpec")
    @Expose
    private RawCodecSpec rawCodecSpec;
    @SerializedName("displaySize")
    @Expose
    private DisplaySize displaySize;
    @SerializedName("displayAspectRatio")
    @Expose
    private DisplayAspectRatio displayAspectRatio;

    public StorageSize getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(StorageSize storageSize) {
        this.storageSize = storageSize;
    }

    public StorageAspectRatio getStorageAspectRatio() {
        return storageAspectRatio;
    }

    public void setStorageAspectRatio(StorageAspectRatio storageAspectRatio) {
        this.storageAspectRatio = storageAspectRatio;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public RawCodecSpec getRawCodecSpec() {
        return rawCodecSpec;
    }

    public void setRawCodecSpec(RawCodecSpec rawCodecSpec) {
        this.rawCodecSpec = rawCodecSpec;
    }

    public DisplaySize getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(DisplaySize displaySize) {
        this.displaySize = displaySize;
    }

    public DisplayAspectRatio getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    public void setDisplayAspectRatio(DisplayAspectRatio displayAspectRatio) {
        this.displayAspectRatio = displayAspectRatio;
    }

}
