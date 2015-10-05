package com.aspire.dubsmash.siavash;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by sia on 10/3/15.
 */

public class Sound {
    @SerializedName("sound_sendername") @Expose private String soundSendername;
    @SerializedName("sound_senderid") @Expose private String soundSenderid;
    @SerializedName("sound_id") @Expose private String soundId;
    @SerializedName("sound_title") @Expose private String soundTitle;
    @SerializedName("sound_url") @Expose private String soundUrl;
    @SerializedName("sound_likes") @Expose private String soundLikes;
    @SerializedName("sound_tags") @Expose private List<String> soundTags = new ArrayList<>();

    /**
     *
     * @return
     * The soundSendername
     */
    public String getSoundSendername() {
        return soundSendername;
    }

    /**
     *
     * @param soundSendername
     * The sound_sendername
     */
    public void setSoundSendername(String soundSendername) {
        this.soundSendername = soundSendername;
    }

    /**
     *
     * @return
     * The soundSenderid
     */
    public String getSoundSenderid() {
        return soundSenderid;
    }

    /**
     *
     * @param soundSenderid
     * The sound_senderid
     */
    public void setSoundSenderid(String soundSenderid) {
        this.soundSenderid = soundSenderid;
    }

    /**
     *
     * @return
     * The soundId
     */
    public String getSoundId() {
        return soundId;
    }

    /**
     *
     * @param soundId
     * The sound_id
     */
    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    /**
     *
     * @return
     * The soundTitle
     */
    public String getSoundTitle() {
        return soundTitle;
    }

    /**
     *
     * @param soundTitle
     * The sound_title
     */
    public void setSoundTitle(String soundTitle) {
        this.soundTitle = soundTitle;
    }

    /**
     *
     * @return
     * The soundUrl
     */
    public String getSoundUrl() {
        return soundUrl;
    }

    /**
     *
     * @param soundUrl
     * The sound_url
     */
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    /**
     *
     * @return
     * The soundLikes
     */
    public String getSoundLikes() {
        return soundLikes;
    }

    /**
     *
     * @param soundLikes
     * The sound_likes
     */
    public void setSoundLikes(String soundLikes) {
        this.soundLikes = soundLikes;
    }

    /**
     *
     * @return
     * The soundTags
     */
    public List<String> getSoundTags() {
        return soundTags;
    }

    /**
     *
     * @param soundTags
     * The sound_tags
     */
    public void setSoundTags(List<String> soundTags) {
        this.soundTags = soundTags;
    }

}