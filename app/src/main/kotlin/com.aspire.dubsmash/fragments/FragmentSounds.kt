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
import com.aspire.dubsmash.adapters.AdapterSounds
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.ctx
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

/**
 * Created by sia on 11/20/15.
 */
class FragmentSounds : Fragment() {
    private val switchLT: Switch by bindView(R.id.switch_lt)
    private val soundsRecyclerView: RecyclerView by bindView(R.id.items_recycler_view)

    private var adapter: AdapterSounds? = null
    private var sounds: MutableList<Sound> = arrayListOf()
    private var isFirstRun = true
    private var group = 0
    private val quantity = 15

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sounds_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpSwitch()
        if (isFirstRun) downloadInitialSounds()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) downloadInitialSounds()
    }

    private fun setUpSwitch() {
        switchLT.textOff = trending
        switchLT.textOn = latest
        switchLT.isChecked = true
    }

    private fun setUpRecyclerView() {
        isFirstRun = false
        val linearLayoutManager = LinearLayoutManager(ctx)
        soundsRecyclerView.layoutManager = linearLayoutManager
        adapter = AdapterSounds(sounds)
        soundsRecyclerView.adapter = adapter
        soundsRecyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener<LinearLayoutManager>(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (switchLT.isChecked)
                    downloadSounds(latest, (++group).toString(), quantity.toString())
                else
                    downloadSounds(trending, (++group).toString(), quantity.toString())
            }
        })
    }

    private fun downloadSounds(retrieveMode: String, group: String, quantity: String) {
        val result = networkApi.downloadSounds(retrieveMode, group, quantity)
        result.enqueue(object : Callback<MultipleResult<Sound>> {
            override fun onResponse(response: Response<MultipleResult<Sound>>, retrofit: Retrofit?) {
                if (response.isSuccess && response.body().ok == "ok") {
                    sounds.addAll(response.body().items)
                    if (isFirstRun) setUpRecyclerView() else adapter?.addData(sounds)
                }
            }

            override fun onFailure(t: Throwable?) {
                throw UnsupportedOperationException()
            }

        })
    }

    private fun downloadInitialSounds() {
        if (switchLT.isChecked)
            downloadSounds(latest, group.toString(), quantity.toString())
        else
            downloadSounds(trending, group.toString(), quantity.toString())

    }

    override fun onDetach() {
        super.onDetach()
        stopAndReleaseMediaPlayer(adapter?.mediaPlayer)
    }
}