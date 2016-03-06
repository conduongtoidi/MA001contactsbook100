package com.whatsoft.contactbook.constant;

import com.whatsoft.contactbook.R;

/**
 * Created by mb on 3/4/16
 */
public enum Gender {
    MALE(R.mipmap.male, 0, "Nam"),
    FEMALE(R.mipmap.female, 1, "Nữ");
    private String value;
    private int key;
    private int icon;

    Gender(int icon, int key, String value) {
        this.icon = icon;
        this.key = key;
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
