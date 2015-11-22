package com.aspire.dubsmash

import android.app.Application
import android.content.pm.PackageManager
import io.realm.Realm

/**
 * Created by sia on 11/19/15.
 */
class ApplicationBase : Application() {
    companion object ApplicationName {
        lateinit var appName: String
        lateinit var realm : Realm
    }

    override fun onCreate() {
        super.onCreate()

        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.SIGNATURE_MATCH)
        appName = packageManager.getApplicationLabel(appInfo) as String

        realm = Realm.getInstance(this)

//        Fabric.with(this, Crashlytics());
    }
}
