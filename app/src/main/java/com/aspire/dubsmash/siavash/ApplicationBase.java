package com.aspire.dubsmash.siavash;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by sia on 9/3/15.
 */
public class ApplicationBase extends Application {
    private static String sAppName;

    public static String getAppName() {
        return ApplicationBase.sAppName;
    }

    @Override public void onCreate() {
        super.onCreate();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.SIGNATURE_MATCH);
            sAppName = appInfo != null ? (String) getPackageManager().getApplicationLabel(appInfo) : "unknown";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
