package com.aspire.dubsmash.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.aspire.dubsmash.R
import com.aspire.dubsmash.fragments.*
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.withArguments

/**
 * Created by sia on 11/18/15.
 */
class ActivityMain : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {
    private val drawer: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.nav_view)
    val toolbar: Toolbar by bindView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)
        toolbar.defaultHamburgerAction(drawer)
        toolbar.defaultTitleStyle()

        switchFragmentTo(FragmentId.MY_SOUNDS)

        //        if (isFirstRun(this))
        //            switchFragmentTo(FragmentId.FIRST_RUN)
        //        else
        //            switchFragmentTo(FragmentId.VIEW_PAGER)

        //        registerUserIfNeeded(this)
        //        createDirectories()
    }

    override fun switchFragmentTo(fragmentId: FragmentId, vararg values: String) {
        when (fragmentId) {
            FragmentId.VIEW_PAGER -> fragmentManager.replaceFragment(FragmentViewPager())
            FragmentId.ADD_SOUND -> fragmentManager.replaceFragment(FragmentAddSound())
        //            FragmentId.CUT_SOUND ->fragmentManager.replaceFragment(FragmentCutSound())
            FragmentId.FIRST_RUN -> fragmentManager.replaceFragment(FragmentFirstRun())
            FragmentId.MY_SOUNDS -> fragmentManager.replaceFragment(FragmentMySounds())
            FragmentId.MY_DUBS -> fragmentManager.replaceFragment(FragmentMyDubs())
        //            FragmentId.PROCESS_DUB->fragmentManager.replaceFragment(FragmentProcessDub())
            FragmentId.RECORD_DUB -> fragmentManager.replaceFragment(FragmentRecordDub().withArguments(SOUND_PATH to values[0]))
            FragmentId.RECORD_SOUND -> fragmentManager.replaceFragment(FragmentRecordSound())
            FragmentId.SEND_SOUND -> fragmentManager.replaceFragment(FragmentSendSound())
            FragmentId.SOUNDS -> fragmentManager.replaceFragment(FragmentSounds())
            FragmentId.DUBS -> fragmentManager.replaceFragment(FragmentDubs())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        //        if (id == R.id.nav_view_pager) {
        //            switchFragmentTo(viewPagerFragment)
        //        } else if (id == R.id.nav_dubs) {
        //            switchFragmentTo(myVideosFragment)
        //        } else if (id == R.id.nav_sounds) {
        //            switchFragmentTo(mySoundsFragment)
        //        } else if (id == R.id.nav_add_sounds) {
        //            //            startActivity(Intent(this, ActivityAddSound::class.java))
        //        } else if (id == R.id.nav_download) {
        //            switchFragmentTo(downloadFragment)
        //        }

        drawer.closeDrawer(GravityCompat.END)
        return true
    }
}

//    /**
//     * compatibility method for calling fragments from other activities
//     *
//     * @param intent
//     */
//    private void whichFragment(Intent intent) {
//        if (intent.getExtras() != null) {
//            String fragmentName = intent.getExtras().getString(Constants.WHICH_FRAGMENT, Constants.NO_FRAGMENT);
//            if (fragmentName.equals(Constants.FRAGMENT_MY_SOUNDS)) {
//                switchFragmentTo(Constants.FRAGMENT_MY_SOUNDS);
//            } else if (fragmentName.equals(Constants.FRAGMENT_MY_DUBS)) {
//                switchFragmentTo(Constants.FRAGMENT_MY_DUBS);
//            } else if (fragmentName.equals(Constants.FRAGMENT_DOWNLOADS)) {
//
//            }
//        }
//    }
