package com.aspire.dubsmash.siavash;

/**
 * Created by sia on 10/4/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Host {

    @SerializedName("host") @Expose private String host;

    /**
     * @return The host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host The host
     */
    public void setHost(String host) {
        this.host = host;
    }

}