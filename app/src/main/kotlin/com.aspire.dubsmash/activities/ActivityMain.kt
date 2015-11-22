package com.aspire.dubsmash.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.*

/**
 * Created by sia on 11/18/15.
 */
class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val drawer: DrawerLayout by bindView(R.id.drawer_layout)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val navigationView: NavigationView by bindView(R.id.nav_view)

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)
        toolbar.defaultHamburgerAction(drawer)
        toolbar.defaultTitleStyle()

        if (isFirstRun(this)) {

        } else {

        }
        registerUserIfNeeded(this)
        createDirectories()
    }

    /**
     * compatibility method for calling fragments from other activities
     * @param intent
     */
    private fun whichFragment(intent: Intent) {
        if (intent.extras != null) {
            intent.extras.getString(whichFragment)
        }
    }

    private fun switchFragmentTo(fragmentName: String, vararg values: String) {
        //        val fragmentManager = fragmentManager
        //        if (fragmentName.equals(viewPagerFragment)) {
        //            fragmentManager.replaceFragment(FragmentViewPager())
        //        } else if (fragmentName.equals(soundsFragment)) {
        //            fragmentManager.replaceFragment(FragmentSounds())
        //        } else if (fragmentName.equals(videosFragment)) {
        //            fragmentManager.replaceFragment(FragmentVideos())
        //        } else if (fragmentName.equals(mySoundsFragment)) {
        //            fragmentManager.replaceFragment(FragmentMySounds())
        //        } else if (fragmentName.equals(myVideosFragment)) {
        //            fragmentManager.replaceFragment(FragmentMyDubs())
        //        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_view_pager) {
            switchFragmentTo(viewPagerFragment)
        } else if (id == R.id.nav_dubs) {
            switchFragmentTo(myVideosFragment)
        } else if (id == R.id.nav_sounds) {
            switchFragmentTo(mySoundsFragment)
        } else if (id == R.id.nav_add_sounds) {
            //            startActivity(Intent(this, ActivityAddSound::class.java))
        } else if (id == R.id.nav_download) {
            switchFragmentTo(downloadFragment)
        }

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
