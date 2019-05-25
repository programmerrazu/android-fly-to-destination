package com.gogaffl.gaffl.home.model;

import java.util.ArrayList;

public class SearchPlaces {

    /**
     * found : true
     * results : ["Saint Helena","Seychelles","Cheltenham","Saint Barthelemy","Saint Helier","Schellenberg","Plaines Wilhelm","Ngerchelong","Cheljabinsk","Saint Helena","Helsinki"]
     */

    private boolean found;
    private ArrayList<String> results;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }
}
