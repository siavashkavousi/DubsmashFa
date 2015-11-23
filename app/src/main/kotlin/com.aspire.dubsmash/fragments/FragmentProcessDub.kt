package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.VideoView
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.*
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.squareup.okhttp.Response
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import retrofit.Callback
import retrofit.Retrofit
import java.io.File
import java.io.FileOutputStream

/**
 * Created by hojjat on 9/29/15 modified by sia on 11/22/15.
 */
class FragmentProcessDub(val soundPath: String, val dubPath: String) : Fragment() {
    private val done: Button by bindView(R.id.done)
    private val tryAgain: LinearLayout by bindView(R.id.try_again)
    private val videoView: VideoView by bindView(R.id.video_view)

    private val dubProcessor = { processDub() }
    private val player = { playResult() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_process_dub, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewsStyle()
        setListeners()
        doAsyncAndWaitThenShowResult(ctx, "در حال پردازش داب شما!", dubProcessor, player)
    }

    private fun setViewsStyle() {

    }

    private fun setListeners() {
        tryAgain.onClick {
            //fixme go to record dub act
        }

        done.onClick {
            val result = networkApi.uploadVideo(getUserId(ctx), File(tempVideoPath), File("x"))
            result.enqueue(object : Callback<Response> {
                override fun onResponse(response: retrofit.Response<Response>?, retrofit: Retrofit?) {
                    throw UnsupportedOperationException()
                }

                override fun onFailure(t: Throwable?) {
                    throw UnsupportedOperationException()
                }

            })
            Toast.makeText(ctx, "داب شما با موفقیت ارسال شد", Toast.LENGTH_LONG).show()
        }
    }

    private fun processDub() {
        val movie = MovieCreator.build(videoPath)
        val sound = MovieCreator.build(soundPath)
        movie.addTrack(sound.tracks[0])
        val mp4File = DefaultMp4Builder().build(movie)
        val fc = FileOutputStream(File("x")).channel
        mp4File.writeContainer(fc)
        fc.close()
    }

    private fun playResult() {
        //        videoView.setVideoPath()
        videoView.setOnCompletionListener { it.start() }
        videoView.start()
    }
}