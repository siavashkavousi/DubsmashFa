package com.aspire.dubsmash.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.VideoView
import com.aspire.dubsmash.ApplicationBase
import com.aspire.dubsmash.R
import com.aspire.dubsmash.fragments.OnFragmentInteractionListener
import com.aspire.dubsmash.util.*
import io.realm.Realm
import org.jetbrains.anko.onClick
import java.io.File
import java.io.FileOutputStream

/**
 * Created by sia on 11/20/15.
 */
class AdapterDubs(private val act: Activity, private var dubs: List<Dub>) : RecyclerView.Adapter<AdapterDubs.DubViewHolder>() {
    private var counter = 0
    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dubs, parent, false)
        return DubViewHolder(view)
    }

    override fun onBindViewHolder(holder: DubViewHolder, position: Int) {
        holder.bindView(dubs[position])
    }

    override fun getItemCount(): Int {
        return dubs.size
    }

    fun addData(data: List<Dub>) {
        dubs = data
        notifyDataSetChanged()
    }

    inner class DubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dubItem: VideoView by bindView(R.id.item_dub)
        private val imageItem: ImageButton by bindView(R.id.item_image)
        private val progressBar: ProgressBar by bindView(R.id.item_progress_bar)

        private lateinit var dub: Dub
        private val name = "dub$counter.mp4"
        private val path = tempPath + File.separator + name

        init {
            setListeners()
        }

        fun bindView(dub: Dub) {
            this.dub = dub
            //            Glide.with(context).load(Util.BASE_URL + File.separator + video.getVideoThumbnail()).placeholder(R.drawable.placeholder).crossFade().into(imageItem)
        }

        private fun setListeners() {
            imageItem.onClick {
                imageItem.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                var realmVideo = ApplicationBase.realm.containsVideo()
                if (realmVideo == null) {
                    downloadDubAndSaveToFile()
                } else {
                    progressBar.visibility = View.INVISIBLE
                    dubItem.setVideoPath(path)
                    dubItem.start()
                    //                    sendVideoIntent(path)
                }
            }
        }

        private fun Realm.containsVideo(): RealmVideo? {
            val video = ApplicationBase.realm.where(RealmVideo::class.java).contains("id", dub.id).findFirst()
            if (video != null) {
                return video
            }
            return null
        }

        private fun downloadDubAndSaveToFile() {
            signalToPlayDub.reset()
            executor.execute {
                val result = networkApi.downloadSingleData(dub.url)
                val response = result.execute()
                FileOutputStream(path).write(response.body().body().bytes())
                ApplicationBase.realm.write(RealmVideo(dub.id, path))
                signalToPlayDub.countDown()
            }
        }

        //        fun onCompleted() {
        //            progressBar.visibility = View.INVISIBLE
        //            videoItem.setVideoPath(path)
        //            videoItem.start()
        //            counter++
        //        }

        //        private fun sendVideoIntent(path: String) {
        //            val intent = Intent(Intent.ACTION_VIEW)
        //            intent.setType("video/*")
        //            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        //            context.startActivity(intent)
        //        }
    }
}