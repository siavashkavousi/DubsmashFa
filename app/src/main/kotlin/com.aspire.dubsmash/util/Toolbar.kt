package com.aspire.dubsmash.util

import android.content.Context
import android.graphics.Typeface
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.TextView
import com.aspire.dubsmash.R
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick

/**
 * Created by sia on 11/16/15.
 */
class Toolbar : Toolbar {
    private val toolbarTitle: TextView by bindView(R.id.toolbar_title)
    private val toolbarRight: ImageButton by bindView(R.id.toolbar_action)
    private val toolbarLeft: ImageButton by bindView(R.id.toolbar_hamburger)

    constructor(context: Context) : super(context) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setUp(context)
    }

    private fun setUp(context: Context) {
        context.layoutInflater.inflate(R.layout.layout_toolbar, this, true)
    }

    fun setTitle(title: String) {
        toolbarTitle.text = title
    }

    fun setTitleStyle(typeface: Typeface) {
        toolbarTitle.typeface = typeface
    }

    fun defaultTitleStyle() {
        toolbarTitle.typeface = getFont(context, Font.AFSANEH)
    }

    fun setActionItemVisibility(visibility: Int) {
        toolbarRight.visibility = visibility
    }

    fun setLeftAction(function: () -> Unit) {
        if (toolbarRight.visibility != VISIBLE) setActionItemVisibility(VISIBLE)
        toolbarRight.onClick {
            function()
        }
    }

    fun setRightAction(function: () -> Unit) {
        toolbarLeft.onClick {
            function()
        }
    }

    fun defaultHamburgerAction(drawer: DrawerLayout) {
        toolbarLeft.onClick {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END)
            } else {
                drawer.openDrawer(GravityCompat.END)
            }
        }
    }
}