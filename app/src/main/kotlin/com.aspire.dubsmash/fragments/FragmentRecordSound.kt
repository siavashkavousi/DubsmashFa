package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.FragmentId
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.tempSoundPath
import org.jetbrains.anko.act
import org.jetbrains.anko.onClick

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
    private val mediaRecorder = MediaRecorder()

    private var recording: Boolean = false
    private var recordingStartTime: Long = 0
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_sound, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    private fun stopRecordingViewsStyle() {
        mic.setImageResource(R.drawable.mic_off)
        startDone.setTextColor(resources.getColor(R.color.mic_off))
        tapToStart.text = "شروع را لمس کنید تا ضبط صدا آغاز شود"
        startDone.text = "شروع"
        timer.visibility = View.INVISIBLE
    }

    private fun startRecordingViewsStyle() {
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
        startRecordingViewsStyle()
        recordingStartTime = System.currentTimeMillis()
        mediaRecorder.setUp()
        mediaRecorder.start()
        startCountDownTimer()
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(10000, 100) {

            override fun onTick(millisUntilFinished: Long) {
                timer.text = (millisUntilFinished / 100).toString() + " میلی ثانیه"
            }

            override fun onFinish() {
                finishRecording()
            }
        }.start()
    }

    private fun finishRecording() {
        stopRecordingViewsStyle()
        mediaRecorder.stopAndReset()
        countDownTimer.cancel()
        callback.switchFragmentTo(FragmentId.SEND_SOUND)
    }

    private fun MediaRecorder.stopAndRelease() {
        stopAndReset()
        release()
    }

    private fun MediaRecorder.stopAndReset() {
        if (recording) stop()
        reset()
        recording = false
    }

    private inline fun safeCancel(function: () -> Unit) {
        if (recording) function()
    }

    private fun MediaRecorder.setUp() {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioEncodingBitRate(99999999)
        setAudioSamplingRate(99999999)
        setOutputFile(tempSoundPath)
        prepare()
    }

    override fun onResume() {
        super.onResume()
        stopRecordingViewsStyle()
    }

    override fun onPause() {
        super.onPause()
        safeCancel {
            countDownTimer.cancel()
            mediaRecorder.stopAndReset()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        safeCancel {
            countDownTimer.cancel()
            mediaRecorder.stopAndRelease()
        }
    }
}
