package com.aspire.dubsmash.siavash;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import io.realm.Realm;

/**
 * Created by sia on 9/3/15.
 */
public class ApplicationBase extends Application {
    private static String sAppName;
    private static Realm sRealm;

    public static String getAppName() {
        return ApplicationBase.sAppName;
    }

    public static Realm getRealm() {
        return sRealm;
    }

    @Override public void onCreate() {
        super.onCreate();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.SIGNATURE_MATCH);
            sAppName = appInfo != null ? (String) getPackageManager().getApplicationLabel(appInfo) : "unknown";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sRealm = Realm.getInstance(this);
    }
}
