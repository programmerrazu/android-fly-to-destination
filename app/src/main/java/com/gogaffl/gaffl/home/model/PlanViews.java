package com.gogaffl.gaffl.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanViews {

    @SerializedName("uniq_views")
    @Expose
    private Integer uniqViews;
    @SerializedName("total_views")
    @Expose
    private Integer totalViews;

    public Integer getUniqViews() {
        return uniqViews;
    }

    public void setUniqViews(Integer uniqViews) {
        this.uniqViews = uniqViews;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

}
