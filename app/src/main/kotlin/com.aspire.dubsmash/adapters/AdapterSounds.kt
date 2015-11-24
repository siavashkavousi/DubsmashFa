package com.aspire.dubsmash.adapters

import android.app.Activity
import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.aspire.dubsmash.R
import com.aspire.dubsmash.fragments.OnFragmentInteractionListener
import com.aspire.dubsmash.util.*
import com.squareup.okhttp.Response
import retrofit.Callback
import retrofit.Retrofit
import java.io.FileOutputStream

/**
 * Created by sia on 11/20/15.
 */
class AdapterSounds(private val act: Activity, private var sounds: List<Sound>) : RecyclerView.Adapter<AdapterSounds.SoundsViewHolder>() {
    private var isPlaying: Boolean = false
    private var playingSoundButton: ImageButton? = null
    var mediaPlayer: MediaPlayer? = null
    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sounds, parent, false)
        return SoundsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundsViewHolder, position: Int) = holder.bindView(sounds[position])

    override fun getItemCount(): Int = sounds.size

    fun addData(data: List<Sound>) {
        sounds = data
        notifyDataSetChanged()
    }

    inner class SoundsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val item: RelativeLayout by bindView(R.id.item)
        private val title: TextView by bindView(R.id.title)
        private val uploadedBy: TextView by bindView(R.id.uploaded_by)
        private val likeButton: ImageButton by bindView(R.id.like)
        private val playSound: ImageButton by bindView(R.id.play_sound)

        private val context = itemView.context
        private lateinit var sound: Sound

        init {
            setListeners()
        }

        fun bindView(sound: Sound) {
            title.text = sound.soundTitle
            if (sound.isLiked)
                likeButton.setImageResource(R.drawable.favorite_selected)
            else
                likeButton.setImageResource(R.drawable.favorite_unselected)
            uploadedBy.text = sound.soundSenderName
            this.sound = sound
        }

        private fun setListeners() {
            item.setOnClickListener(this)
            likeButton.setOnClickListener(this)
            playSound.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val id = v.id
            if (id == likeButton.id) {
                if (!sound.isLiked) {
                    likeButton.setImageResource(R.drawable.favorite_selected)
                    val result = networkApi.getSoundLikes(getUserId(context), sound.soundId)
                    result.enqueue(object : Callback<Response> {
                        override fun onResponse(response: retrofit.Response<Response>, retrofit: Retrofit?) {
                            throw UnsupportedOperationException()
                        }

                        override fun onFailure(t: Throwable?) {
                            throw UnsupportedOperationException()
                        }
                    })
                    sound.isLiked = true
                } else {
                    likeButton.setImageResource(R.drawable.favorite_unselected)
                    sound.isLiked = false
                }
            } else if (id == playSound.id) {
                if (isPlaying) {
                    stopAndReleaseMediaPlayer(mediaPlayer)
                    playingSoundButton?.setImageResource(R.drawable.ic_play)
                    playingSoundButton = playSound
                    playSound.setImageResource(R.drawable.download)
                } else {
                    playingSoundButton = playSound
                    playSound.setImageResource(R.drawable.download)
                    isPlaying = true
                }
                val result = networkApi.downloadSingleData(sound.soundUrl)
                result.enqueue(object : Callback<Response> {
                    override fun onResponse(response: retrofit.Response<Response>, retrofit: Retrofit?) {
                        FileOutputStream(tempSoundPath).write(response.body().body().bytes())
                        if (playingSoundButton === playSound) {
                            playSound.setImageResource(R.drawable.ic_pause)
                            mediaPlayer = MediaPlayer.create(context, Uri.parse(tempSoundPath))
                            mediaPlayer!!.start()
                            mediaPlayer!!.setOnCompletionListener {
                                playSound.setImageResource(R.drawable.ic_play)
                                mediaPlayer!!.reset()
                                mediaPlayer!!.release()
                                mediaPlayer = null
                                isPlaying = false
                            }
                        }
                    }

                    override fun onFailure(t: Throwable?) {
                        throw UnsupportedOperationException()
                    }

                })
            } else if (id == item.id) {
                val progressDialog = ProgressDialog(context)
                progressDialog.setMessage("کمی صبر کنید")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val result = networkApi.downloadSingleData(sound.soundUrl)
                result.enqueue(object : Callback<Response> {
                    override fun onResponse(response: retrofit.Response<Response>, retrofit: Retrofit?) {
                        progressDialog.dismiss()
                        FileOutputStream(tempSoundPath).write(response.body().body().bytes())
                        //fixme arguments which should be sent to record dub fragment
                        callback.switchFragmentTo(FragmentId.RECORD_DUB)
                    }

                    override fun onFailure(t: Throwable?) {
                        progressDialog.dismiss()
                        Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
