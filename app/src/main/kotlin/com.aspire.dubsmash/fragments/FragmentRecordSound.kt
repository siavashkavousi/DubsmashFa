package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.FragmentId
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.executor
import com.aspire.dubsmash.util.tempSoundPath
import org.jetbrains.anko.act
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onUiThread

/**
 * Created by hojjat on 9/30/15 modified by sia on 11/22/15.
 */
class FragmentRecordSound : Fragment() {
    private val tapToStart: TextView by bindView(R.id.tap_to_start)
    private val record: LinearLayout by bindView(R.id.record)
    private val timer: TextView by bindView(R.id.timer)
    private val mic: ImageButton by bindView(R.id.mic)
    private val startDone: TextView by bindView(R.id.start_done)

    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }

    private lateinit var mediaRecorder: MediaRecorder
    private var recording: Boolean = false
    private var recordingStartTime: Long = 0
    private var length: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_sound, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    private fun startRecordingViewsStyle() {
        mic.setImageResource(R.drawable.mic_off)
        startDone.setTextColor(resources.getColor(R.color.mic_off))
        tapToStart.text = "شروع را لمس کنید تا ضبط صدا آغاز شود"
        startDone.text = "شروع"
    }

    private fun stopRecordingViewsStyle() {
        mic.setImageResource(R.drawable.mic_on)
        startDone.setTextColor(resources.getColor(R.color.mic_on))
        startDone.text = "تمام"
        tapToStart.text = "برای پایان ضبط تمام را لمس کنید"
        timer.visibility = View.VISIBLE
    }

    private fun setListeners() {
        record.onClick {
            if (recording) {
                if (System.currentTimeMillis() - recordingStartTime > 1500)
                    finishRecording()
            } else
                startRecording()
        }
    }

    private fun startRecording() {
        recording = true
        stopRecordingViewsStyle()
        recordingStartTime = System.currentTimeMillis()
        mediaRecorder.start()
        executor.execute {
            while (recording) {
                length = System.currentTimeMillis() - recordingStartTime
                if (length < 10 * 1000) {
                    onUiThread { timer.text = "%.1f".format((10000 - length) / 1000.toFloat()) + " ثانیه" }
                } else {
                    finishRecording()
                }

                Thread.sleep(100)
            }
        }
    }

    private fun finishRecording() {
        stopAndReleaseMediaRecorder()
        callback.switchFragmentTo(FragmentId.SEND_SOUND)
    }

    private fun stopAndReleaseMediaRecorder() {
        recording = false
        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()
    }

    private fun setUpRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setAudioEncodingBitRate(99999999)
        mediaRecorder.setAudioSamplingRate(99999999)
        mediaRecorder.setOutputFile(tempSoundPath)
        mediaRecorder.prepare()
    }

    override fun onResume() {
        super.onResume()
        setUpRecorder()
        startRecordingViewsStyle()
    }

    override fun onPause() {
        super.onPause()
        stopAndReleaseMediaRecorder()
    }
}
