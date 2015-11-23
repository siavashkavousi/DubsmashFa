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
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.getUserId
import com.aspire.dubsmash.util.networkApi
import com.squareup.okhttp.Response
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import retrofit.Callback
import retrofit.Retrofit
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by hojjat on 10/3/15 modified by sia on 11/22/15.
 */
class FragmentSendSound : Fragment() {
    private val done: Button by bindView(R.id.done)
    private val playPause: ImageButton by bindView(R.id.play_pause)
    private val progressbar: SeekBar by bindView(R.id.progress)
    private val soundTitle: EditText by bindView(R.id.sound_title)
    private val tags: EditText by bindView(R.id.tags_input)

//    internal var audioPath: String
//    internal var finalSoundPath: String
    internal var mediaPlayer: MediaPlayer? = null

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
            if (mediaPlayer == null)
                startPlaying()
            else
                finishPlaying()
        }
        done.onClick {
            if (soundTitle.text.toString().trim { it <= ' ' }.length > 0) {
                next()
            } else {
                Toast.makeText(ctx, "عنوان صدا را وارد کنید!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun next() {
        //        addMetadata(); problem with samsung
        saveToMySounds()
        sendSoundToServer()
        //fixme
        //        val intent = Intent(this, ActivityMain::class.java)
        //        intent.putExtra(Constants.WHICH_FRAGMENT, Constants.FRAGMENT_MY_SOUNDS)
        //        intent.putExtra(Constants.SOUND_PATH, audioPath)
        //        startActivity(intent)
        //        finish()
    }

    private fun sendSoundToServer() {
        // fixme file path
        val result = networkApi.uploadSound(getUserId(ctx), File("x"), soundTitle.text.toString(), tags.text.toString())
        result.enqueue(object : Callback<Response> {
            override fun onResponse(response: retrofit.Response<Response>?, retrofit: Retrofit?) {
                throw UnsupportedOperationException()
            }

            override fun onFailure(t: Throwable?) {
                throw UnsupportedOperationException()
            }

        })
    }

    private fun saveToMySounds() {
//        try {
//            val `in` = FileInputStream(audioPath)
//            finalSoundPath = Util.getNextSoundPath(soundTitle.text.toString().trim { it <= ' ' })
//            val out = FileOutputStream(finalSoundPath)
//            val buf = ByteArray(1024)
//            var len: Int
//            while ((len = `in`.read(buf)) > 0) {
//                out.write(buf, 0, len)
//            }
//            `in`.close()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    //    private void addMetadata() {
    //        try {
    //            TagOptionSingleton.getInstance().setAndroid(true);
    //            AudioFile audioFile = AudioFileIO.read(new File(audioPath));
    //            Tag newTag = audioFile.getTag();
    //            newTag.setField(FieldKey.TITLE, soundTitle.getText().toString());
    //            newTag.setField(FieldKey.ARTIST, Util.getUsersName(ActivitySendSound.this));
    //            audioFile.commit();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            Toast.makeText(ActivitySendSound.this, "Exception!   1", Toast.LENGTH_SHORT).show();
    //        }
    //    }

    private fun startPlaying() {
//        playPause.setImageResource(R.drawable.pause_big)
//        mediaPlayer = MediaPlayer.create(this@ActivitySendSound, Uri.parse(audioPath))
//        mediaPlayer!!.start()
//        progressbar.max = mediaPlayer!!.duration
//        Thread(Runnable {
//            while (mediaPlayer != null) {
//                runOnUiThread {
//                    try {
//                        progressbar.progress = mediaPlayer!!.currentPosition
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//                try {
//                    Thread.sleep(100)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }).start()
//        mediaPlayer!!.setOnCompletionListener { finishPlaying() }
    }

    private fun finishPlaying() {
        playPause.setImageResource(R.drawable.play_big)
        progressbar.progress = 0
        if (mediaPlayer!!.isPlaying)
            mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        mediaPlayer!!.release()
        mediaPlayer = null
        val c: Char?
    }
}