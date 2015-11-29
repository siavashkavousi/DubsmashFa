package com.aspire.dubsmash.util

import com.squareup.okhttp.Response
import retrofit.Call
import retrofit.http.*
import java.io.File

/**
 * Created by sia on 11/18/15.
 */
interface NetworkApi {
    @FormUrlEncoded @POST() fun getHost(@Field("getHost") msg: String): Call<SingleResult<Host>>

    @Multipart @POST("uploadSound.php") fun uploadSound(@Part("sound_senderid") senderId: String, @Part("sound_file") soundFile: File,
                                                        @Part("sound_title") title: String, @Part("sound_tags") tags: String): Call<Response>

    @Multipart @POST("uploadVideo.php") fun uploadDub(@Part("video_recorderid") recorderId: String, @Part("video_file") dubFile: File,
                                                        @Part("video_tumbnail") thumbnail: File, @Part("video_title") title: String? = null,
                                                        @Part("video_tags") tags: String? = null): Call<Response>

    @FormUrlEncoded @POST("getSound.php") fun downloadSounds(@Field("retrieve_mode") retrieve: String, @Field("bunch") group: String,
                                                             @Field("qty") quantity: String, @Field("tag") tags: String? = null): Call<MultipleResult<Sound>>

    @FormUrlEncoded @POST("getVideo.php") fun downloadDubs(@Field("retrieve_mode") retrieve: String, @Field("bunch") group: String,
                                                             @Field("qty") quantity: String, @Field("tag") tags: String? = null): Call<MultipleResult<Dub>>

    @FormUrlEncoded @POST("likeSound.php") fun getSoundLikes(@Field("user_id") userId: String, @Field("sound_id") id: String): Call<Response>

    @FormUrlEncoded @POST("likeVideo.php") fun getDubLikes(@Field("user_id") userId: String, @Field("video_id") id: String): Call<Response>

    @FormUrlEncoded @POST("register.php") fun registerUsername(@Field("user_name") username: String): Call<SingleResult<User>>

    @GET("{url}") fun downloadSingleData(@Path("url") url: String): Call<Response>

    @GET("{url}") fun downloadMultipleData(@Part("url") url: String): Call<List<Response>>
}