package softsolutions.com.tutors.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

const val REQUEST_FILE = 1806
class FilePicker(private val context: Context) {

    fun start() {
        if (isPermissionNeed()){
            askStoragePermission(context as Activity)
        }else{
            showFileChooser()
        }
    }

    private fun askStoragePermission(activity: Activity) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    showFileChooser()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }
            })
            .check()

    }

    private fun isPermissionNeed() =
        if (Build.VERSION.SDK_INT >= 23) {
            context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        }else{
            false
        }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun showFileChooser() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {

            addCategory(Intent.CATEGORY_OPENABLE)

            val mimeTypes = arrayOf(
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/pdf",
                "application/x-rar-compressed"
            )

            type = "*/*"

            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        try {
            context as Activity
            context.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), REQUEST_FILE)
        } catch (ex: android.content.ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(
                context, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}