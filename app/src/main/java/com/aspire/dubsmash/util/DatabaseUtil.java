package com.aspire.dubsmash.util;

import com.aspire.dubsmash.siavash.ApplicationBase;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by sia on 10/13/15.
 */
public class DatabaseUtil {
    public static <T extends RealmObject> void writeObjectToDatabase(T object) {
        Realm realm = ApplicationBase.getRealm();
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    public static <T> T queryObjectFromDatabase() {
        return null;
    }
}
