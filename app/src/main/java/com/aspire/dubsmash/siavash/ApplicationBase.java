package com.aspire.dubsmash.siavash;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;

/**
 * Created by sia on 9/3/15.
 */
public class ApplicationBase extends Application {
    private static String sAppName;
    private static Realm sRealm;
    private RefWatcher refWatcher;

    public static String getAppName() {
        return ApplicationBase.sAppName;
    }

    public static Realm getRealm() {
        return sRealm;
    }

    public static RefWatcher getRefWatcher(Context context) {
        ApplicationBase appBase = (ApplicationBase) context.getApplicationContext();
        return appBase.refWatcher;
    }

    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.SIGNATURE_MATCH);
            sAppName = appInfo != null ? (String) getPackageManager().getApplicationLabel(appInfo) : "unknown";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sRealm = Realm.getInstance(this);
    }
}
