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
class AdapterMyDubs(private val dubsPath: List<String>) : RecyclerView.Adapter<AdapterMyDubs.DubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dubs, parent, false)
        return DubViewHolder(view)
    }

    override fun onBindViewHolder(holder: DubViewHolder, position: Int) = holder.bindView(dubsPath[position])

    override fun getItemCount(): Int = dubsPath.size

    inner class DubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dubItem: VideoView by bindView(R.id.item_dub)
        val progressBar: ProgressBar by bindView(R.id.item_progress_bar)

        fun bindView(path: String) {
            dubItem.setVideoPath(path)
        }

        override fun onClick(v: View) {
            val id = v.id
            if (id == dubItem.id) {
                dubItem.start()
            }
        }
    }
}
