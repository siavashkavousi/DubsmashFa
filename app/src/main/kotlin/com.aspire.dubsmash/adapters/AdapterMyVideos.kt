package com.aspire.dubsmash.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.VideoView
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.bindView

/**
 * Created by sia on 10/3/15.
 */
class AdapterMyVideos(private val videosPath: List<String>) : RecyclerView.Adapter<AdapterMyVideos.VideosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_videos, parent, false)
        return VideosViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) = holder.bindView(videosPath[position])

    override fun getItemCount(): Int = videosPath.size

    inner class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mVideoItem: VideoView by bindView(R.id.item_video)
        val mProgressBar: ProgressBar by bindView(R.id.item_progress_bar)

        fun bindView(path: String) {
            mVideoItem.setVideoPath(path)
        }

        override fun onClick(v: View) {
            val id = v.id
            if (id == mVideoItem.id) {
                mVideoItem.start()
            }
        }
    }
}
