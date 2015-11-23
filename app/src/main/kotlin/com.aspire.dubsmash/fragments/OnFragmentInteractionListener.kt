package com.aspire.dubsmash.fragments

import com.aspire.dubsmash.util.FragmentId

/**
 * Created by sia on 11/21/15.
 */
interface OnFragmentInteractionListener {
    fun switchFragmentTo(fragmentId: FragmentId, vararg values: String)
}