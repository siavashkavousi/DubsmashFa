package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aspire.dubsmash.R
import com.aspire.dubsmash.activities.ActivityMain
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.*

/**
 * Created by hojjat on 10/2/15 modified by sia on 11/22/15.
 */
class FragmentCutSound(val sourcePath: String) : Fragment() {
    private val done: Button by bindView(R.id.done)
    private val playPause: ImageButton by bindView(R.id.play_pause)
    private val scrollView: HorizontalScrollView by bindView(R.id.scroll_view)
    private val musicImage: ImageView by bindView(R.id.music_image)
    private val start: SeekBar by bindView(R.id.start)
    private val end: SeekBar by bindView(R.id.end)
    private val progress: SeekBar by bindView(R.id.progress)

    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null

    private var recording: Boolean = false
    private var shortInput = false
    private var cuttingIsDone: Boolean = false
    private var goNextAsCuttingFinished: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cut_sound, container, false)
        if (act is ActivityMain) setUpToolbar()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        maximizeDeviceVolume()
        setUpMediaPlayer()
        setUpViewState()
        setListeners()
    }

    private fun setUpToolbar() {
        val toolbar = (act as ActivityMain).toolbar
        toolbar.setTitle("بریدن صدا")
    }

    private fun setListeners() {
        start.onSeekBarChangeListener {
            //fixme should fix swap functionality here
            //            if(start.progress > end.progress){
            //            }
        }
        done.onClick {
            goNextAsCuttingFinished = true
            if (!recording) {
                if (cuttingIsDone)
                    next()
                else
                    startProcess()
            }
        }
        playPause.onClick { startProcess() }
    }

    private fun maximizeDeviceVolume() {
        val audioManager = ctx.audioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
    }

    private fun startProcess() {
        if (getSelectedRangeLength() < fileMinLength) {
            Toast.makeText(ctx, "بازه انتخاب شده خیلی کوتاه است", Toast.LENGTH_LONG).show()
            return
        }
        recording = true
        setUpMediaRecorder()
        mediaPlayer?.seekTo(getSelectedRangeStart())
        mediaPlayer?.start()
        setUpProgressBar(getSelectedRangeStart(), getSelectedRangeLength())
        mediaRecorder?.start()
        setViewStateOnRecording()
        executor.execute {
            Thread.sleep(getSelectedRangeLength().toLong())
            onUiThread { finishedProcess() }
        }
    }

    private fun setUpMediaPlayer() {
        mediaPlayer = MediaPlayer.create(ctx, Uri.parse(sourcePath))
        if (mediaPlayer!!.duration <= fileMaxLength) shortInput = true
    }

    private fun setUpViewState() {
        setUpMusicImage()
        setUpSeekBars()
        setTexts()
    }

    private fun setUpMusicImage() {
        setMusicImageSize()
        setMusicImageDrawable()
    }

    private fun setMusicImageSize() {
        val layoutParams = musicImage.layoutParams
        if (shortInput)
            layoutParams.width = getDisplaySize(act).x
        else
            layoutParams.width = ((mediaPlayer!!.duration / fileMaxLength) * getDisplaySize(act).x).toInt()
        layoutParams.height = BitmapFactory.decodeResource(resources, R.drawable.wave).height
        musicImage.layoutParams = layoutParams
    }

    private fun setMusicImageDrawable() {
        val tileMe = BitmapDrawable(BitmapFactory.decodeResource(resources, R.drawable.wave))
        tileMe.tileModeX = Shader.TileMode.REPEAT
        musicImage.setBackgroundDrawable(tileMe)
    }

    private fun setUpSeekBars() {
        start.max = if (shortInput) mediaPlayer!!.duration else fileMaxLength
        end.max = start.max
        start.progress = if (shortInput) 0 else (0.35 * start.max).toInt()
        end.progress = if (shortInput) end.max else (0.7 * end.max).toInt()
        progress.isEnabled = false
        progress.max = start.max
    }

    private fun setTexts() {
        done.text = "ادامه"
    }

    private fun finishedProcess() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setViewStateOnRecording() {
        playPause.isEnabled = true
    }

    private fun setUpProgressBar(selectedRangeStart: Int, selectedRangeLength: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setUpMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setAudioEncodingBitRate(99999999)
        mediaRecorder!!.setAudioSamplingRate(99999999)
        mediaRecorder!!.setOutputFile(tempSoundPath)
        mediaRecorder!!.prepare()
    }

    private fun next() {
        mediaPlayer?.stopAndRelease()
        //        startActivity(Intent(this, ActivitySendSound::class.java).putExtra(Constants.SOUND_PATH, resPath))
        //        finish()
    }

    private fun getSelectedRangeLength(): Int {
        return end.progress - start.progress
    }

    private fun getSelectedRangeStart(): Int {
        if (shortInput)
            return start.progress
        else
            return ((scrollView.scrollX.toFloat() / musicImage.width) * mediaPlayer!!.duration).toInt() + start.progress
    }

}