/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whatsoft.contactbook.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.whatsoft.contactbook.base.BaseObject;
import com.whatsoft.contactbook.constant.Gender;

public class Contact extends BaseObject {
    @Expose
    @SerializedName("id")
    private String id = "";
    @Expose
    @SerializedName("full_name")
    private String name = "";
    @Expose
    @SerializedName("relationship")
    private String relationship = "";
    @Expose
    @SerializedName("gender")
    private String gender = "";
    @Expose
    @SerializedName("birthday")
    private String dayOfBirth = "";
    @Expose
    @SerializedName("folk")
    private String nation = "";
    @Expose
    @SerializedName("address")
    private String address = "";
    @Expose
    @SerializedName("number_family")
    private String homeNumber = "";
    @Expose
    @SerializedName("number_street")
    private String addressNo = "";
    @Expose
    @SerializedName("create_time")
    private long createTime;
    @Expose
    @SerializedName("update_time")
    private long updateTime;
    @Expose
    @SerializedName("long")
    private String longitude = "";
    @Expose
    @SerializedName("lat")
    private String latitude = "";

    private boolean isFavorite;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public Gender getGender() {
        if (!TextUtils.isEmpty(gender) && gender.equals(Gender.FEMALE.getValue())) {
            return Gender.FEMALE;
        }
        return Gender.MALE;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", relationship='" + relationship + '\'' +
                ", gender='" + gender + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", nation='" + nation + '\'' +
                ", homeNumber='" + homeNumber + '\'' +
                ", addressNo='" + addressNo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    public Contact copy() {
        Contact contact = new Contact();
        contact.setGender(getGender().getValue());
        contact.setLongitude(getLongitude());
        contact.setLatitude(getLatitude());
        contact.setRelationship(getRelationship());
        contact.setAddressNo(getAddressNo());
        contact.setAddress(getAddress());
        contact.setCreateTime(getCreateTime());
        contact.setDayOfBirth(getDayOfBirth());
        contact.setFavorite(isFavorite());
        contact.setHomeNumber(getHomeNumber());
        contact.setId(getId());
        contact.setUpdateTime(getUpdateTime());
        contact.setName(getName());
        contact.setNation(getNation());
        return contact;
    }
}
