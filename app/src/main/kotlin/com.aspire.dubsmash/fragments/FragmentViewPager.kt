package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.bindView
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems
import org.jetbrains.anko.act

/**
 * Created by sia on 11/21/15.
 */
class FragmentViewPager : Fragment() {
    private val viewPagerTab: SmartTabLayout by bindView(R.id.view_pager_tab)
    private val viewPager: ViewPager by bindView(R.id.view_pager)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager.adapter = setUpViewPagerAdapter()
        viewPagerTab.setViewPager(viewPager)
    }

    private fun setUpViewPagerAdapter(): FragmentPagerItemAdapter {
        return FragmentPagerItemAdapter(
                fragmentManager, FragmentPagerItems.with(act)
                .add("صدا ها", FragmentSounds::class.java)
                .add("داب ها", FragmentVideos::class.java)
                .create())
    }
}
