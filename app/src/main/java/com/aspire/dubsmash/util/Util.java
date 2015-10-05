package com.aspire.dubsmash.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aspire.dubsmash.siavash.ActivityMain;
import com.aspire.dubsmash.siavash.ApplicationBase;
import com.aspire.dubsmash.siavash.SingleResult;
import com.aspire.dubsmash.siavash.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sia on 10/3/15.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static final String FONTS_PATH = "fonts/";
    private static final String FONTS_EXTENTION = ".ttf";
    private static Map<String, Typeface> fonts = new HashMap<>();
    private static final String MAIN_PREFRENCES = "MAIN_PREFRENCES";
    private static final String USER_NAME = "USER_NAME";
    public static final String USER_ID = "USER_ID";
    private static final String IS_FIRST_RUN = "IS_FIRST_RUN";

    private static FontFamily appDefaultFontFamily = FontFamily.Naskh;

    public static void setUserName(Context context, String userName) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        preferences.edit().putString(USER_NAME, userName).commit();
    }

    public static String getPhoneId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getNextSoundPath(String title) {
        String result = Constants.MY_SOUNDS_PATH + "/" + getValidForm(title);
        File f = new File(result + ".m4a");
        if (f.exists())
            result += "1";
        return result + ".m4a";
    }

    private static String getValidForm(String title) {
        String res = "";
        for (int i = 0; i < title.length(); i++) {
            if (Character.isLetterOrDigit(title.charAt(i)))
                res += title.charAt(i);
            else
                res += " ";
        }
        String[] split = res.split("\\s+");
        res = "";
        for (String str : split) {
            res += str.trim() + " ";
        }
        return res.trim();
    }

    public static String getNextDubPath() {
        return Constants.MY_DUBS_PATH + "/Dub_" + System.currentTimeMillis() + ".mp4";
    }

    public static void registerUser(final Context context) {
        if (Util.isNetworkAvailable(context)) {
            ActivityMain.sNetworkApi.registerUsername(Util.getUsersName(context), new Callback<SingleResult<User>>() {
                @Override
                public void success(SingleResult<User> userSingleResult, Response response) {
                    if (userSingleResult.getOk().equals(Constants.OK))
                        setUserId(context, userSingleResult.getItem().getUserId());
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_FIRST_RUN, true);
    }

    public static void setIsFirstRun(Context context, boolean isFirstRun) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(IS_FIRST_RUN, isFirstRun).commit();
    }

    public static void createDirectories() {
        File f = new File(Constants.BASE_DIR);
        if (!f.isDirectory() || !f.exists())
            f.mkdirs();
        f = new File(Constants.MY_DUBS_PATH);
        if (!f.isDirectory() || !f.exists())
            f.mkdirs();
        f = new File(Constants.MY_SOUNDS_PATH);
        if (!f.isDirectory() || !f.exists())
            f.mkdirs();
        f = new File(Constants.DOWNLOADS_PATH);
        if (!f.isDirectory() || !f.exists())
            f.mkdirs();
        f = new File(Constants.TEMP_PATH);
        if (!f.isDirectory() || !f.exists())
            f.mkdirs();
    }

    public static String getUsersName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        return preferences.getString(USER_NAME, "");
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        preferences.edit().putString(USER_ID, userId).commit();
    }

    public static void registerUserIfNeeded(Context context) {
        if (!userHasRegistered(context))
            registerUser(context);
    }

    public static boolean userHasRegistered(Context context) {
        return context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE).contains(USER_ID);
    }

    public enum FontFamily {
        Naskh("Naskh"),
        Default(Naskh.toString());

        private String text;

        FontFamily(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontWeight {
        Bold("Bold"),
        Regular("Regular");


        private String text;

        FontWeight(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    static public void setFont(Context context, FontFamily fontFamily, FontWeight fontWeight, Object... elements) {
        Typeface typeFace = getTypeFace(context, fontFamily, fontWeight);
        setFont(typeFace, elements);
    }

    private static Typeface getTypeFace(Context context, FontFamily fontFamily, FontWeight fontWeight) {
        if (!fonts.containsKey(fontFamily.toString() + fontWeight.toString()))
            fonts.put(fontFamily.toString() + fontWeight.toString(), Typeface.createFromAsset(context.getAssets(), getTypeFacePath(fontFamily, fontWeight)));
        return fonts.get(fontFamily.toString() + fontWeight.toString());
    }

    private static void setFont(Typeface typeface, Object... elements) {
        for (Object element : elements) {
            if (element instanceof TextView)
                ((TextView) element).setTypeface(typeface);
            else if (element instanceof Button)
                ((Button) element).setTypeface(typeface);
            else if (element instanceof ToggleButton)
                ((ToggleButton) element).setTypeface(typeface);
            else
                Log.e(TAG, "invalid input!");
        }
    }

    private static String getTypeFacePath(FontFamily fontFamily, FontWeight fontWeight) {
        return FONTS_PATH + fontFamily.toString() + "-" + fontWeight.toString() + FONTS_EXTENTION;
    }

    public static void setText(Object elem, String text) {
        if (elem instanceof TextView)
            ((TextView) elem).setText(PersianReshape.reshape(text));
        else if (elem instanceof Button)
            ((Button) elem).setText(PersianReshape.reshape(text));
        else if (elem instanceof ToggleButton)
            ((ToggleButton) elem).setText(PersianReshape.reshape(text));
        else
            Log.e(TAG, "invalid input!");
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1) {
        setText(elem0, text0);
        setText(elem1, text1);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2, Object elem3, String text3) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2, Object elem3, String text3, Object elem4, String text4) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2, Object elem3, String text3, Object elem4, String text4, Object elem5, String text5) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2, Object elem3, String text3, Object elem4, String text4, Object elem5, String text5, Object elem6, String text6) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5, elem6, text6);
    }

    public static void setText(Object elem0, String text0, Object elem1, String text1, Object elem2, String text2, Object elem3, String text3, Object elem4, String text4, Object elem5, String text5, Object elem6, String text6, Object elem7, String text7) {
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5, elem6, text6, elem7, text7);
    }

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static byte[] getFromStream(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1) {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();

        return buf;
    }

    /**
     * private directory of application
     *
     * @param name file name
     */
    public static File createFile(Context context, String name) {
        File file = new File(context.getCacheDir(), name);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                if (Constants.IS_DEBUG) Log.d(TAG, "Required media storage does not exist");
            }
        }
        return file;
    }

    public static void saveToFile(byte[] bytes, String path) throws IOException {
        FileOutputStream fileOuputStream = new FileOutputStream(path);
        fileOuputStream.write(bytes);
    }

    @Nullable
    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MAIN_PREFRENCES, Context.MODE_PRIVATE);
        if (!preferences.contains(USER_ID))
            registerUser(context);
        return preferences.getString(USER_ID, Constants.UNKNOWN);
    }

    public static Bitmap getVideoThumbnailBitmap(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
    }

    public static String getBitmapFilePath(Bitmap bitmap) {
        File file = new File(Constants.TEMP_THUMBNAIL_PATH);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.TEMP_THUMBNAIL_PATH;
    }

    public static String getVideoThumbnailFilePath(String videoPath){
        return getBitmapFilePath(getVideoThumbnailBitmap(videoPath));
    }
}
