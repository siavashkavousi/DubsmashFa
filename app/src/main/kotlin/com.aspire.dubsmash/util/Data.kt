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
                 , @SerializedName("sound_sendername") @Expose var senderName: String
                 , @SerializedName("sound_senderid") @Expose var senderId: String
                 , @SerializedName("sound_id") @Expose var Id: String
                 , @SerializedName("sound_title") @Expose var title: String
                 , @SerializedName("sound_url") @Expose var url: String
                 , @SerializedName("sound_likes") @Expose var likes: String
                 , @SerializedName("sound_tags") @Expose var tags: List<String>)

data class Dub(@SerializedName("video_recordername") @Expose var recorderName: String
               , @SerializedName("video_recorderid") @Expose var recorderId: String
               , @SerializedName("video_id") @Expose var id: String
               , @SerializedName("video_title") @Expose var title: String
               , @SerializedName("video_url") @Expose var url: String
               , @SerializedName("video_tumbnail") @Expose var thumbnail: String
               , @SerializedName("video_likes") @Expose var likes: String
               , @SerializedName("video_tags") @Expose var tags: List<String>)

data class User(@SerializedName("user_id") @Expose var userId: String)

class RealmVideo(@Required var id: String, @Required var path: String) : RealmObject()
