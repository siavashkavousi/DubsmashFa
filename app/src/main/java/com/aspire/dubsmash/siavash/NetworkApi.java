package com.aspire.dubsmash.siavash;


import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * network interface
 * Created by sia on 10/1/15.
 */
public interface NetworkApi {
    @FormUrlEncoded @POST("/") void getHost(@Field("getHost") String msg, Callback<SingleResult<Host>> callback);

    @Multipart @POST("/uploadSound.php") void uploadSound(@Part("sound_senderid") String soundSenderId, @Part("sound_file") TypedFile soundFile
            , @Part("sound_title") String soundTitle, @Part("sound_tags") String tags, Callback<Response> callback);

    @Multipart @POST("/uploadVideo.php") void uploadVideo(@Part("video_recorderid") String videoRecorderId, @Part("video_file") TypedFile videoFile
            , @Part("video_title") String videoTitle, @Part("video_tumbnail") TypedFile videoThumbnail, @Part("video_tags") String tags, Callback<Response> callback);

    @FormUrlEncoded @POST("/getSound.php") void downloadSound(@Field("retrieve_mode") String retrieve, @Field("bunch") String group
            , @Field("qty") String quantity, @Field("tag") String tags, Callback<MultipleResult<Sound>> callback);

    @FormUrlEncoded @POST("/getVideo.php") void downloadVideo(@Field("retrieve_mode") String retrieve, @Field("bunch") String group
            , @Field("qty") String quantity, @Field("tag") String tags, Callback<MultipleResult<Video>> callback);

    @FormUrlEncoded @POST("/likeSound.php") void getSoundLikes(@Field("user_id") String userId, @Field("sound_id") String soundId, Callback<Response> callback);

    @FormUrlEncoded @POST("/likeVideo.php") void getVideoLikes(@Field("user_id") String userId, @Field("video_id") String videoId, Callback<Response> callback);

    @FormUrlEncoded @POST("/register.php") void registerUsername(@Field("user_name") String username, Callback<SingleResult<User>> callback);

    @GET("/{url}") void downloadSingleData(@Path("url") String url, Callback<Response> callback);

    @GET("/{url}") void downloadMultipleData(@Part("url") String url, Callback<List<Response>> callback);
}
