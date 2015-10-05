package com.aspire.dubsmash.siavash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by sia on 10/3/15.
 */
public class SingleResult<T> {
    @SerializedName("ok") @Expose private String mOk;
    @SerializedName("result") @Expose private T mItem;

    /**
     * @return The ok
     */
    public String getOk() {
        return mOk;
    }

    /**
     * @param ok The ok
     */
    public void setOk(String ok) {
        this.mOk = ok;
    }

    /**
     * @return The result
     */
    public T getItem() {
        return mItem;
    }


    public void setSounds(T item) {
        this.mItem = item;
    }
}