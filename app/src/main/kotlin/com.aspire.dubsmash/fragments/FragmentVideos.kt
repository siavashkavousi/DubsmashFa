package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.aspire.dubsmash.R
import com.aspire.dubsmash.adapters.AdapterVideos
import com.aspire.dubsmash.util.*
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

/**
 * Created by sia on 11/21/15.
 */
class FragmentVideos : Fragment() {
    val videosRecyclerView: RecyclerView by bindView(R.id.items_recycler_view)
    val switchLT: Switch by bindView(R.id.switch_lt)

    private var adapter: AdapterVideos? = null
    private var videos: MutableList<Video> = arrayListOf()
    private val isFirstRun = true
    private val group = 0
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
        val linearLayoutManager = LinearLayoutManager(activity)
        videosRecyclerView.layoutManager = linearLayoutManager
        adapter = AdapterVideos(videos)
        videosRecyclerView.adapter = adapter
        //fixme space item decoration should be added
        //        mVideosRecyclerView.addItemDecoration(SpacesItemDecoration(Constants.RECYCLE_VIEW_SPACE))
        //        mVideosRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
        //            @Override public void onLoadMore(int currentPage) {
        //                if (mSwitchLT.isChecked()) {
        //                    downloadVideos(Constants.LATEST, String.valueOf(++group), String.valueOf(quantity));
        //                } else {
        //                    downloadVideos(Constants.TRENDING, String.valueOf(++group), String.valueOf(quantity));
        //                }
        //            }
        //        });
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
