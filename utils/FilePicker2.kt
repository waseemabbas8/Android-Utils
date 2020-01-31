package com.peopleperfectae.utils

import android.content.Intent
import androidx.fragment.app.Fragment
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations


const val REQUEST_FILE = 1806


class FilePicker(private val fragment: Fragment) {

    fun start() {
        val intent = Intent(fragment.context, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowVideos(false)
                .setShowAudios(false)
                .setSingleChoiceMode(true)
                .setShowFiles(true)
                .enableImageCapture(true)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build()
        )
        fragment.startActivityForResult(intent, REQUEST_FILE)
    }

}