package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.aspire.dubsmash.R
import com.aspire.dubsmash.util.bindView
import org.jetbrains.anko.onClick

/**
 * Created by sia on 11/22/15.
 */
class FragmentAddSound : Fragment() {
    private val record: Button by bindView(R.id.record)
    private val import: Button by bindView(R.id.import_)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_sound, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun setTexts() {
        record.text = "ضبط صدا"
        import.text = "انتخاب از آهنگ ها"
    }

    private fun setListeners() {
        record.onClick {
//            startActivity(android.content.Intent(this@ActivityAddSound, ActivityRecordSound::class.java))
        }
        import.onClick {
//            val intent = android.content.Intent()
//            intent.setType("audio/*")
//            intent.setAction(android.content.Intent.ACTION_GET_CONTENT)
//            startActivityForResult(android.content.Intent.createChooser(intent, "انتخاب فایل صوتی"), 1)
        }
    }
}

//    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent) {
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                val uri = data.getData()
//                startActivity(android.content.Intent(this@ActivityAddSound, ActivityCutSound::class.java).putExtra(Constants.SOUND_PATH, getRealPathFromURI(uri)))
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun getRealPathFromURI(contentURI: android.net.Uri): String {
//        val result: String
//        val cursor = getContentResolver().query(contentURI, null, null, null, null)
//        if (cursor == null) {
//            // Source is Dropbox or other similar local file path
//            result = contentURI.getPath()
//        } else {
//            cursor!!.moveToFirst()
//            val idx = cursor!!.getColumnIndex(android.provider.MediaStore.Images.ImageColumns.DATA)
//            result = cursor!!.getString(idx)
//            cursor!!.close()
//        }
//        return result
//    }
//}
