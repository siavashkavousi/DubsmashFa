package com.aspire.dubsmash.siavash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sia on 10/3/15.
 */
public class User {

    @SerializedName("user_id") @Expose String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
