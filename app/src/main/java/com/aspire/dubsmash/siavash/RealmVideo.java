package com.aspire.dubsmash.siavash;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by sia on 10/11/15.
 */
public class RealmVideo extends RealmObject {
    @Required private String id;
    @Required private String path;

    public RealmVideo(){}

    public RealmVideo(String id, String url) {
        this.id = id;
        path = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
