package com.softsolutions.ilamkidunya.css.utils

import android.content.Intent
import androidx.fragment.app.Fragment
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations

const val REQUEST_IMAGE = 1893
const val REQUEST_VIDEO = 1843
const val REQUEST_FILE = 1863

class FilePicker(private val fragment: Fragment) {

    fun start(requestCode: Int) {
        var imagesEnabled = false
        var videosEnabled = false
        var filesEnabled = false

        when(requestCode) {
            REQUEST_IMAGE -> imagesEnabled = true
            REQUEST_VIDEO -> videosEnabled = true
            REQUEST_FILE -> filesEnabled = true
        }

        val intent = Intent(fragment.context, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(imagesEnabled)
                .setShowVideos(videosEnabled)
                .setSingleChoiceMode(true)
                .setShowFiles(filesEnabled)
                .enableImageCapture(imagesEnabled)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build()
        )

        fragment.startActivityForResult(intent, requestCode)
    }
}