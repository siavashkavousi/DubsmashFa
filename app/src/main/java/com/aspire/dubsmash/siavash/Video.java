package com.aspire.dubsmash.siavash;

/**
 * Created by sia on 10/4/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Video {
    @SerializedName("video_recordername") @Expose private String videoRecordername;
    @SerializedName("video_recorderid") @Expose private String videoRecorderid;
    @SerializedName("video_id") @Expose private String videoId;
    @SerializedName("video_title") @Expose private Object videoTitle;
    @SerializedName("video_url") @Expose private String videoUrl;
    @SerializedName("video_tumbnail") @Expose private String videoThumbnail;
    @SerializedName("video_likes") @Expose private String videoLikes;
    @SerializedName("video_tags") @Expose private List<String> videoTags = new ArrayList<String>();

    /**
     * @return The videoRecordername
     */
    public String getVideoRecordername() {
        return videoRecordername;
    }

    /**
     * @param videoRecordername The video_recordername
     */
    public void setVideoRecordername(String videoRecordername) {
        this.videoRecordername = videoRecordername;
    }

    /**
     * @return The videoRecorderid
     */
    public String getVideoRecorderid() {
        return videoRecorderid;
    }

    /**
     * @param videoRecorderid The video_recorderid
     */
    public void setVideoRecorderid(String videoRecorderid) {
        this.videoRecorderid = videoRecorderid;
    }

    /**
     * @return The videoId
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * @param videoId The video_id
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    /**
     * @return The videoTitle
     */
    public Object getVideoTitle() {
        return videoTitle;
    }

    /**
     * @param videoTitle The video_title
     */
    public void setVideoTitle(Object videoTitle) {
        this.videoTitle = videoTitle;
    }

    /**
     * @return The videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * @param videoUrl The video_url
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * @return The videoThumbnail
     */
    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    /**
     * @param videoThumbnail The video_thumbnail
     */
    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    /**
     * @return The videoLikes
     */
    public String getVideoLikes() {
        return videoLikes;
    }

    /**
     * @param videoLikes The video_likes
     */
    public void setVideoLikes(String videoLikes) {
        this.videoLikes = videoLikes;
    }

    /**
     * @return The videoTags
     */
    public List<String> getVideoTags() {
        return videoTags;
    }

    /**
     * @param videoTags The video_tags
     */
    public void setVideoTags(List<String> videoTags) {
        this.videoTags = videoTags;
    }

}
