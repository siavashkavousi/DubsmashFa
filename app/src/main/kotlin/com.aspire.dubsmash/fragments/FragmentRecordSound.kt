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
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.executor
import org.jetbrains.anko.onClick
import java.io.IOException

/**
 * Created by hojjat on 9/30/15 modified by sia on 11/22/15.
 */
class FragmentRecordSound : Fragment() {
    private val tapToStart: TextView by bindView(R.id.tap_to_start)
    private val record: LinearLayout by bindView(R.id.record)
    private val timer: TextView by bindView(R.id.timer)
    private val mic: ImageButton by bindView(R.id.mic)
    private val startDone: TextView by bindView(R.id.start_done)

    private var mediaRecorder: MediaRecorder? = null

    private var recording: Boolean = false
    private var recordingStartTime: Long = 0
    private var length: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_sound, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewsStyle()
        setListeners()
    }

    private fun setViewsStyle() {

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
        mic.setImageResource(R.drawable.mic_on)
        timer.visibility = View.VISIBLE
        startDone.text = "تمام"
        tapToStart.text = "برای پایان ضبط تمام را لمس کنید"
        startDone.setTextColor(resources.getColor(R.color.mic_on))
        recordingStartTime = System.currentTimeMillis()
        recording = true
        mediaRecorder?.start()
        executor.execute {
            //fixme if problem
//            if ((length = System.currentTimeMillis() - recordingStartTime) < 10 * 1000) {
//                timer.text = "%.1f".format((10000 - length) / 1000.toFloat()) + " ثانیه"
//            } else {
//                finishRecording()
//            }
//            Thread.sleep(100)
        }
    }

    private fun finishRecording() {
        recording = false
        mediaRecorder!!.stop()
        mediaRecorder!!.reset()
        mediaRecorder!!.release()
        mediaRecorder = null
        //fixme
        //        startActivity(new Intent(this, ActivityRecordDub.class));
        //        val intent = Intent(this, ActivitySendSound::class.java)
        //        intent.putExtra(Constants.SOUND_PATH, resultSoundPath)
        //        startActivity(intent)
        //        finish()
    }

    private fun initRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setAudioEncodingBitRate(99999999)
        mediaRecorder!!.setAudioSamplingRate(99999999)
        //fixme hheheh
        //        mediaRecorder!!.setOutputFile(resultSoundPath)
        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        initRecorder()
        mic.setImageResource(R.drawable.mic_off)
        startDone.setTextColor(resources.getColor(R.color.mic_off))
        tapToStart.text = "شروع را لمس کنید تا ضبط صدا آغاز شود"
        startDone.text = "شروع"
    }
}
