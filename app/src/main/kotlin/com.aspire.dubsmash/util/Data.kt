package com.aspire.dubsmash.util

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Required

/**
 * Created by sia on 11/18/15.
 */
data class Host(@SerializedName("host") @Expose var host: String)

data class SingleResult<T>(@SerializedName("ok") @Expose var ok: String, @SerializedName("result") @Expose var item: T)

data class MultipleResult<T>(@SerializedName("ok") @Expose var ok: String, @SerializedName("result") @Expose var items: List<T>)

data class Sound(var isLiked: Boolean
                 , @SerializedName("sound_sendername") @Expose var soundSenderName: String
                 , @SerializedName("sound_senderid") @Expose var soundSenderId: String
                 , @SerializedName("sound_id") @Expose var soundId: String
                 , @SerializedName("sound_title") @Expose var soundTitle: String
                 , @SerializedName("sound_url") @Expose var soundUrl: String
                 , @SerializedName("sound_likes") @Expose var soundLikes: String
                 , @SerializedName("sound_tags") @Expose var soundTags: List<String>)

data class Video(@SerializedName("video_recordername") @Expose var videoRecorderName: String
                 , @SerializedName("video_recorderid") @Expose var videoRecorderId: String
                 , @SerializedName("video_id") @Expose var videoId: String
                 , @SerializedName("video_title") @Expose var videoTitle: String
                 , @SerializedName("video_url") @Expose var videoUrl: String
                 , @SerializedName("video_tumbnail") @Expose var videoThumbnail: String
                 , @SerializedName("video_likes") @Expose var videoLikes: String
                 , @SerializedName("video_tags") @Expose var videoTags: List<String>)

data class User(@SerializedName("user_id") @Expose var userId: String)

class RealmVideo(@Required var id: String, @Required var path: String) : RealmObject()
