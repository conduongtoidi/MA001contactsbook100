package com.whatsoft.contactbook.base;

import com.whatsoft.contactbook.utils.Log;
import com.whatsoft.contactbook.utils.ParserUtil;

import java.io.Serializable;

/**
 * Created by mb on 3/4/16
 */
public class BaseObject implements Serializable {
    public String toJsonString() {
        try {
            return ParserUtil.toJson(this);
        } catch (Exception e) {
            Log.e(e);
        }
        return "";
    }
}
