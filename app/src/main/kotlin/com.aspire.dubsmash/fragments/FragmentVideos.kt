package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.aspire.dubsmash.EndlessRecyclerOnScrollListener
import com.aspire.dubsmash.R
import com.aspire.dubsmash.adapters.AdapterVideos
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

/**
 * Created by sia on 11/21/15.
 */
class FragmentVideos : Fragment() {
    private val videosRecyclerView: RecyclerView by bindView(R.id.items_recycler_view)
    private val switchLT: Switch by bindView(R.id.switch_lt)

    private var adapter: AdapterVideos? = null
    private var videos: MutableList<Video> = arrayListOf()
    private val isFirstRun = true
    private var group = 0
    private val quantity = 15

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sounds_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpSwitch()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) {
            downloadInitialVideos()
            downloadVideos(latest, "1", "1")
        }
    }

    private fun setUpSwitch() {
        switchLT.textOff = trending
        switchLT.textOn = latest
        switchLT.isChecked = true
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(ctx)
        videosRecyclerView.layoutManager = linearLayoutManager
        adapter = AdapterVideos(act, videos)
        videosRecyclerView.adapter = adapter
        videosRecyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (switchLT.isChecked)
                    downloadVideos(latest, (++group).toString(), quantity.toString())
                else
                    downloadVideos(trending, (++group).toString(), quantity.toString())
            }

        })
    }

    private fun downloadVideos(retrieveMode: String, group: String, quantity: String) {
        val result = networkApi.downloadVideos(retrieveMode, group, quantity)
        result.enqueue(object : Callback<MultipleResult<Video>> {
            override fun onResponse(response: Response<MultipleResult<Video>>, retrofit: Retrofit?) {
                if (response.isSuccess && response.body().ok == "ok") {
                    videos.addAll(response.body().items)
                    if (isFirstRun) setUpRecyclerView() else adapter?.addData(videos)
                }
            }

            override fun onFailure(t: Throwable?) {
                throw UnsupportedOperationException()
            }

        })
    }

    private fun downloadInitialVideos() {
        if (switchLT.isChecked) {
            downloadVideos(latest, group.toString(), quantity.toString())
        } else {
            downloadVideos(trending, group.toString(), quantity.toString())
        }
    }
}
