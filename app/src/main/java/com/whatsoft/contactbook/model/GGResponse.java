package com.whatsoft.contactbook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mb on 3/7/16
 */
public class GGResponse {
    @Expose
    @SerializedName("results")
    private List<GGLocation> locations;

    @Expose
    @SerializedName("status")
    private String status;

    public List<GGLocation> getLocations() {
        return locations;
    }

    public String getStatus() {
        return status;
    }
}
