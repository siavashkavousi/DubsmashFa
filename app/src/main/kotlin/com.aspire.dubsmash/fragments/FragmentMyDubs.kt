package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aspire.dubsmash.R
import com.aspire.dubsmash.adapters.AdapterMyDubs
import com.aspire.dubsmash.util.bindView
import com.aspire.dubsmash.util.myDubsPath
import org.jetbrains.anko.act
import java.io.File

/**
 * Created by sia on 11/19/15.
 */
class FragmentMyDubs : Fragment() {
    val dubsRecyclerView: RecyclerView by bindView(R.id.items_recycler_view)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_sounds_dubs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dubsPath: MutableList<String> = arrayListOf()
        val dubsDirectory = File(myDubsPath)
        dubsDirectory.listFiles { it -> it.absolutePath.endsWith(".mp4") }?.forEach { dubsPath.add(it.absolutePath) }

        val adapter = AdapterMyDubs(dubsPath)
        setUpRecyclerView(adapter)
    }

    private fun setUpRecyclerView(adapter: AdapterMyDubs) {
        dubsRecyclerView.layoutManager = LinearLayoutManager(act)
        dubsRecyclerView.adapter = adapter
    }
}