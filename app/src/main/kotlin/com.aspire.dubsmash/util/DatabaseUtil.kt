package com.aspire.dubsmash.util

import com.aspire.dubsmash.ApplicationBase
import io.realm.Realm
import io.realm.RealmObject

/**
 * Created by sia on 11/21/15.
 */

fun <T : RealmObject> Realm.write(t: T) {
    val realm = ApplicationBase.realm
    realm.executeTransaction {
        realm.copyToRealm(t)
    }
}