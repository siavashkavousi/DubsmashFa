package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aspire.dubsmash.R
import com.aspire.dubsmash.activities.ActivityMain
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import java.io.File

/**
 * Created by hojjat on 10/3/15 modified by sia on 11/22/15.
 */
class FragmentSendSound : Fragment() {
    private val done: Button by bindView(R.id.done)
    private val playPause: ImageButton by bindView(R.id.play_pause)
    private val seekBar: SeekBar by bindView(R.id.progress)
    private val soundTitle: EditText by bindView(R.id.sound_title)
    private val tags: EditText by bindView(R.id.tags_input)

    private val finalSoundFile: File by lazy { generateSoundFile(soundTitle.text.toString().trim { it <= ' ' }) }
    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }
    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer.create(ctx, Uri.parse(tempSoundPath)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_sound, container, false)
        if (act is ActivityMain) setUpToolbar()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewsStyle()
        setListeners()
    }

    private fun setUpToolbar() {
        val toolbar = (act as ActivityMain).toolbar
        toolbar.setTitle("ارسال صدا")
    }

    private fun setViewsStyle() {

    }

    private fun setListeners() {
        playPause.onClick {
            if (!mediaPlayer.isPlaying) startPlaying()
            else finishPlaying()
        }
        done.onClick {
            if (soundTitle.text.toString().trim { it <= ' ' }.length > 0) {
                executor.execute {
                    saveToMySounds()
                    sendSoundToServer()
                }
                mediaPlayer.stopAndReset()
                callback.switchFragmentTo(FragmentId.MY_SOUNDS, finalSoundFile.absolutePath)
            } else Toast.makeText(ctx, "عنوان صدا را وارد کنید!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSoundToServer() {
        val result = networkApi.uploadSound(getUserId(ctx), finalSoundFile, soundTitle.text.toString(), tags.text.toString())
        result.execute()
    }

    private fun saveToMySounds() {
        val tempFile = File(tempSoundPath)
        tempFile.copyTo(finalSoundFile)
    }

    private fun startPlaying() {
        playPause.setImageResource(R.drawable.pause_big)
        mediaPlayer.start()
        seekBar.max = mediaPlayer.duration
        executor.execute {
            while (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                Thread.sleep(100)
            }
        }
        mediaPlayer.setOnCompletionListener { finishPlaying() }
    }

    private fun finishPlaying() {
        playPause.setImageResource(R.drawable.play_big)
        seekBar.progress = 0
        mediaPlayer.stopAndReset()
    }

    override fun onPause() {
        super.onPause()
        finishPlaying()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishPlaying()
        mediaPlayer.stopAndRelease()
    }
}