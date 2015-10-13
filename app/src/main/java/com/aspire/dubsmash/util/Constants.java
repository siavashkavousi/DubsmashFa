package com.aspire.dubsmash.util;

import android.os.Environment;

import com.aspire.dubsmash.siavash.ApplicationBase;

import java.io.File;

/**
 * Created by sia on 10/3/15.
 */
public class Constants {
//    hojjat Constants
    public static final String APP_NAME = "دابسمش فارسی";
    public final static int FILES_MAX_LENGTH = 10 * 1000;
    public final static int FILES_MIN_LENGTH = 1 * 1000;
    public static final String SOUND_PATH = "SOUND_PATH";
    public static final String DUB_PATH = "DUB_PATH";
    public static final String NOT_ASSIGNED = "NOT_ASSIGNED";
    public static final String UNKNOWN = "unknown";
    public static final String BASE_DIR = Environment.getExternalStorageDirectory() + File.separator + ApplicationBase.getAppName();
    public static final String TEMP_PATH = BASE_DIR + "/.temp";
    public static final String MY_SOUNDS_PATH = BASE_DIR + "/mySounds";
    public static final String MY_DUBS_PATH = BASE_DIR + "/myDubs";
    public static final String DOWNLOADS_PATH = BASE_DIR + "/downloads";
    public static final String TEMP_SOUND_PATH = TEMP_PATH + "/tempSound.m4a";
    public static final String TEMP_VIDEO_PATH = TEMP_PATH + "/tempVideo.mp4";
    public static final String TEMP_THUMBNAIL_PATH = TEMP_PATH + "/tempThumbnail.png";

//    sia Constants
    public static final String OK = "true";
    public static final String LATEST  = "latest";
    public static final String TRENDING = "trending";
    public static final boolean IS_DEBUG = true;
    public static final String PRIVATE_SOUND = ".privateSound";
    public static final int RECYCLE_VIEW_SPACE = 2;

    public static final String WHICH_FRAGMENT = "whichFragment";
    public static final String NO_FRAGMENT = "noFragment";
    public static final String FRAGMENT_VIEW_PAGER = "fragmentViewPager";
    public static final String FRAGMENT_SOUNDS = "fragmentSounds";
    public static final String FRAGMENT_VIDEOS = "fragmentVideos";
    public static final String FRAGMENT_MY_SOUNDS = "fragmentMySounds";
    public static final String FRAGMENT_MY_DUBS = "fragmentMyDubs";
    public static final String FRAGMENT_DOWNLOADS = "fragmentDownloads";
}