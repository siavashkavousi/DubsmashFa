package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aspire.dubsmash.R
import com.aspire.dubsmash.SpacesItemDecoration
import com.aspire.dubsmash.adapters.AdapterMySounds
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.mySoundsPath
import com.aspire.dubsmash.util.recyclerViewSpace
import com.aspire.dubsmash.util.stopAndRelease
import org.jetbrains.anko.act
import java.io.File

/**
 * Created by sia on 11/19/15.
 */
class FragmentMySounds : Fragment() {
    private val soundsRecyclerView: RecyclerView by bindView(R.id.items_recycler_view)

    private val soundsPath: MutableList<String> = arrayListOf()
    private val soundsTitle: MutableList<String> = arrayListOf()

    private var adapter: AdapterMySounds? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_sounds_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getSoundFiles()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        soundsRecyclerView.layoutManager = LinearLayoutManager(act)
        soundsRecyclerView.addItemDecoration(SpacesItemDecoration(recyclerViewSpace))
        adapter = AdapterMySounds(act, soundsPath, soundsTitle)
        soundsRecyclerView.adapter = adapter
    }

    private fun getSoundFiles() {
        val soundsDirectory = File(mySoundsPath)
        soundsDirectory.listFiles { it -> it.absolutePath.endsWith(".m4a") }?.forEach {
            soundsPath.add(it.absolutePath)
            soundsTitle.add(it.name)
        }
    }

    override fun onDetach() {
        super.onDetach()
        adapter?.mediaPlayer?.stopAndRelease()
    }
}