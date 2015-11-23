package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick

/**
 * Created by hojjat on 10/3/15 modified and converted to kotlin by sia on 11/22/15.
 */
class FragmentFirstRun : Fragment() {
    private val done: Button by bindView(R.id.done)
    private val nameInput: EditText by bindView(R.id.editText)
    private val callback: OnFragmentInteractionListener by lazy { act as OnFragmentInteractionListener }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first_run, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewsStyle()
        setListeners()
    }

    private fun setViewsStyle() {

    }

    private fun setListeners() {
        done.onClick {
            setUserName(ctx, nameInput.text.toString())
            registerUser(ctx)
            setIsFirstRun(ctx, false)
            callback.switchFragmentTo(FragmentId.VIEW_PAGER)
        }

        nameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (nameInput.text.toString().trim { it <= ' ' }.length > 0)
                    done.isEnabled = true
                else
                    done.isEnabled = false
            }

        })
        nameInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (done.isEnabled) done.performClick()
            }
            false
        }
    }
}