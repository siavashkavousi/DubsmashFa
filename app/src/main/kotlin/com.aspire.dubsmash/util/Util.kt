package com.aspire.dubsmash.util

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Environment
import com.aspire.dubsmash.ApplicationBase
import com.aspire.dubsmash.R
import com.aspire.dubsmash.ResettableCountDownLatch
import com.aspire.dubsmash.fragments.FragmentRecordSound
import com.aspire.dubsmash.siavash.util.NetworkService
import org.jetbrains.anko.connectivityManager
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit
import java.io.File
import java.util.concurrent.Executors

/**
 * Created by sia on 11/18/15.
 */
// network service
var url = "http://api.farsi-dub.mokhi.ir"
val baseUrl = BaseUrl(url)
val networkApi = NetworkService.createService(NetworkApi::class.java, baseUrl)

// shared preferences constants
private val mainPreferences = "mainPreferences"
private val isFirstRun = "isFirstRun"
private val userId = "userId"
private val userName = "userName"

// app directory constants
val baseDirectory: String = Environment.getExternalStorageDirectory().absolutePath + File.separator + ApplicationBase.appName
val tempPath = baseDirectory + "/.temp"
val mySoundsPath = baseDirectory + "/mySounds"
val myDubsPath = baseDirectory + "/myDubs"
val downloadsPath = baseDirectory + "/downloads"
val tempSoundPath = tempPath + "/tempSound.m4a"
val tempVideoPath = tempPath + "/tempVideo.mp4"
val tempThumbnailPath = tempPath + "/tempThumbnail.png"

val SOUND_PATH = "SOUND_PATH"
val videoPath = "DUB_PATH"
val notAssigned = "NOT_ASSIGNED"
val unknown = "unknown"

// sounds & videos latest or trending constants
val latest = "latest"
val trending = "trending"

// thread pool and thread related vars
private val coreCount = Runtime.getRuntime().availableProcessors()
val executor = Executors.newFixedThreadPool(coreCount + 1)
val signalToPlayVideo = ResettableCountDownLatch(1)

val fileMaxLength = 10 * 1000
val fileMinLength = 1 * 1000
val recyclerViewSpace = 2

fun getAppMainFont(context: Context): Typeface {
    return getFont(context, Font.AFSANEH)
}

fun getDefaultPoemFont(context: Context): Typeface {
    return getFont(context, Font.MASHIN_TAHRIR)
}

fun getFont(context: Context, fontName: Font): Typeface {
    var resource: String = "fonts/" + fontName.resourceId
    return Typeface.createFromAsset(context.assets, resource)
}

fun isFirstRun(context: Context): Boolean {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    return preferences.getBoolean(isFirstRun, true)
}

fun setIsFirstRun(context: Context, firstRun: Boolean) {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    preferences.edit().putBoolean(isFirstRun, firstRun).apply()
}

fun registerUserIfNeeded(context: Context) {
    if (!userHasRegistered(context))
        registerUser(context)
}

fun userHasRegistered(context: Context): Boolean {
    return context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE).contains(userId)
}

fun setUserName(context: Context, username: String) {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    preferences.edit().putString(userName, username).apply()
}

fun getUsersName(context: Context): String {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    return preferences.getString(userName, "")
}

fun setUserId(context: Context, userId: String) {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    preferences.edit().putString(userId, userId).apply()
}

fun getUserId(context: Context): String {
    val preferences = context.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE)
    if (!preferences.contains(userId))
        registerUser(context)
    return preferences.getString(userId, unknown)
}

fun registerUser(context: Context) {
    if (isNetworkAvailable(context)) {
        val result = networkApi.registerUsername(getUsersName(context))

        result.enqueue(object : Callback<SingleResult<User>> {
            override fun onResponse(response: Response<SingleResult<User>>, retrofit: Retrofit) {
                if (response.isSuccess && response.body().ok == "ok")
                    setUserId(context, response.body().item.userId)
            }

            override fun onFailure(t: Throwable?) {
                throw UnsupportedOperationException()
            }

        })
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val networkInfo = context.connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun FragmentManager.addFragment(fragment: Fragment, container: Int = R.id.container, tag: String? = null, animEnter: Int = 0, animExit: Int = 0, animPopEnter: Int = 0, animPopExit: Int = 0) {
    this.beginTransaction()
            .setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit)
            .add(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
}

fun FragmentManager.replaceFragment(fragment: Fragment, container: Int = R.id.container, tag: String? = null, animEnter: Int = 0, animExit: Int = 0, animPopEnter: Int = 0, animPopExit: Int = 0) {
    this.beginTransaction()
            .setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit)
            .replace(container, fragment, tag)
            .commit()
}

fun createDirectories() {
    var f = File(baseDirectory)
    if (!f.isDirectory || !f.exists())
        f.mkdirs()
    f = File(myDubsPath)
    if (!f.isDirectory || !f.exists())
        f.mkdirs()
    f = File(mySoundsPath)
    if (!f.isDirectory || !f.exists())
        f.mkdirs()
    f = File(downloadsPath)
    if (!f.isDirectory || !f.exists())
        f.mkdirs()
    f = File(tempPath)
    if (!f.isDirectory || !f.exists())
        f.mkdirs()
}

fun MediaPlayer.stopAndRelease() {
    stopAndReset()
    release()
}

fun MediaPlayer.stopAndReset() {
    if (isPlaying) stop()
    reset()
}

fun stopNReset(mediaPlayer: MediaPlayer){
    if(mediaPlayer.isPlaying) mediaPlayer.stop()
    mediaPlayer.reset()
}

fun changeEndpointIfNeeded(baseUrl: BaseUrl) {
    val result = networkApi.getHost("sia")
    result.enqueue(object : Callback<SingleResult<Host>> {
        override fun onResponse(response: Response<SingleResult<Host>>, retrofit: Retrofit?) {
            if (response.isSuccess) {
                val host = response.body().item.host
                baseUrl.baseUrl = host
            }
        }

        override fun onFailure(t: Throwable?) {
        }
    })
}

fun doAsync(function: () -> Unit) {
    executor.execute {
        function()
    }
}

fun doAsyncAndWait(ctx: Context, message: String, function: FragmentRecordSound.() -> Unit) {
    val progressDialog = ProgressDialog(ctx)
    progressDialog.setMessage("در حال پردازش داب شما!")
    progressDialog.setCancelable(false)
    progressDialog.show()
    executor.execute { FragmentRecordSound().function() }
    progressDialog.dismiss()
}

fun doAsyncAndWaitThenShowResult(ctx: Context, message: String, function: () -> Unit, result: () -> Unit) {
    val progressDialog = ProgressDialog(ctx)
    progressDialog.setMessage(message)
    progressDialog.setCancelable(false)
    progressDialog.show()
    executor.execute { function() }
    progressDialog.dismiss()
    result()
}

fun getDisplaySize(activity: Activity): Point {
    val p = Point()
    activity.windowManager.defaultDisplay.getSize(p)
    return p
}

fun generateSoundFile(title: String): File {
    val file = File(mySoundsPath + File.separator + title + ".m4a")
    if (file.exists()) duplicateName()
    return file
}

fun duplicateName() {

}

/**
 * Returns the index of the smallest element or `null` if there are no elements.
 */
fun FloatArray.minElementIndex(): Int? {
    if (isEmpty()) return null
    var minIndex = 0
    var min = this[0]
    for (i in 1..lastIndex) {
        val e = this[i]
        if (min > e) {
            minIndex = i
            min = e
        }
    }
    return minIndex
}