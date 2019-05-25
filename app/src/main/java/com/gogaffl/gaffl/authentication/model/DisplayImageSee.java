
package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DisplayImageSee {

    @SerializedName("elements")
    @Expose
    private List<Elements> elements = null;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
