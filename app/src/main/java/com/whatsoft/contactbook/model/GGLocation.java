package com.whatsoft.contactbook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mb on 3/7/16
 */
public class GGLocation {
    @Expose
    @SerializedName("formatted_address")
    private String formattedAddress;

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
