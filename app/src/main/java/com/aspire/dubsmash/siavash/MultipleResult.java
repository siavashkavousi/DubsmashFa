package com.aspire.dubsmash.siavash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sia on 10/3/15.
 */
public class MultipleResult<T> {
    @SerializedName("ok") @Expose private String ok;
    @SerializedName("result") @Expose private List<T> mItems = new ArrayList<>();

    /**
     *
     * @return
     * The ok
     */
    public String getOk() {
        return ok;
    }

    /**
     *
     * @param ok
     * The ok
     */
    public void setOk(String ok) {
        this.ok = ok;
    }

    /**
     *
     * @return
     * The result
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     *
     * @param items
     * The result
     */
    public void setItems(List<T> items) {
        this.mItems = items;
    }
}