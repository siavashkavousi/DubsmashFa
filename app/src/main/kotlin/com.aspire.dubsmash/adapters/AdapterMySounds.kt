package com.aspire.dubsmash.adapters

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.aspire.dubsmash.R
import com.aspire.dubsmash.fragments.OnFragmentInteractionListener
import com.aspire.dubsmash.util.FragmentId
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.stopAndReleaseMediaPlayer

/**
 * Created by sia on 11/19/15.
 */
class AdapterMySounds(private val act: Activity, private val soundsPath: List<String>, private val soundsTitle: List<String>) : RecyclerView.Adapter<AdapterMySounds.SoundsViewHolder>() {
    private var isPlaying: Boolean = false
    private var playingSoundButton: ImageButton? = null
    var mediaPlayer: MediaPlayer? = null
    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mysounds, parent, false)
        return SoundsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundsViewHolder, position: Int) = holder.bindView(soundsTitle[position], soundsPath[position])

    override fun getItemCount(): Int = soundsPath.size

    inner class SoundsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val item: RelativeLayout by bindView(R.id.item)
        val title: TextView by bindView(R.id.title)
        val playSound: ImageButton by bindView(R.id.play_sound)
        lateinit private var path: String

        init {
            setListeners()
        }

        fun bindView(title: String, path: String) {
            this.title.text = title
            this.path = path
        }

        private fun setListeners() {
            playSound.setOnClickListener(this)
            item.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val id = v.id
            if (id == playSound.id) {
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

                if (playingSoundButton === playSound) {
                    playSound.setImageResource(R.drawable.ic_pause)
                    mediaPlayer = MediaPlayer.create(itemView.context, Uri.parse(path))
                    mediaPlayer!!.start()
                    mediaPlayer!!.setOnCompletionListener {
                        playSound.setImageResource(R.drawable.ic_play)
                        mediaPlayer!!.reset()
                        mediaPlayer!!.release()
                        mediaPlayer = null
                        isPlaying = false
                    }
                }
            } else if (id == item.id) {
                //fixme arguments which should be sent to record dub fragment
                callback.switchFragmentTo(FragmentId.RECORD_DUB)
            }
        }
    }
}