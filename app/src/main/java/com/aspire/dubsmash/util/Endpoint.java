package com.aspire.dubsmash.util;

/**
 * Created by sia on 10/6/15.
 */
public class Endpoint implements retrofit.Endpoint {
    private String mUrl;

    public Endpoint(String baseUrl) {
        mUrl = baseUrl;
    }

    @Override public String getUrl() {
        if (mUrl == null) throw new IllegalStateException("url not set!");
        return mUrl;
    }

    public void setUrl(String baseUrl) {
        mUrl = baseUrl;
    }

    @Override public String getName() {
        return "default";
    }
}
