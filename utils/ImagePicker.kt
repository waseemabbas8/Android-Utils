package softsolutions.com.tutors.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import softsolutions.com.tutors.BuildConfig
import softsolutions.com.tutors.R
import java.io.File

const val REQUEST_GALLERY_IMAGE = 1879
const val REQUEST_CAMERA_IMAGE = 1839

class ImagePicker(private val context: Context) {

    fun start() {
        showSelectionDialog()
    }

    private fun showSelectionDialog() {
        val dialogItems = listOf(
            context.getString(R.string.camera_option),
            context.getString(R.string.sd_card_option)
        )
        val adapter = ArrayAdapter(context, android.R.layout.select_dialog_item, dialogItems)
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Chose Image")
            .setAdapter(adapter) { _, pos ->
                when (pos) {
                    0 -> {
                        if (isPermissionNeed()) {
                            askStoragePermission(context as Activity, true)
                        } else {
                            takePhotoFromCamera()
                        }
                        return@setAdapter
                    }
                    1 -> {
                        if (isPermissionNeed()) {
                            askStoragePermission(context as Activity, false)
                        } else {
                            pickFromGallery()
                        }
                        return@setAdapter
                    }
                }
            }
            .show()
    }

    private fun isPermissionNeed() =
        if (Build.VERSION.SDK_INT >= 23) {
            context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        }else{
            false
        }

    private fun askStoragePermission(activity: Activity, isCamera: Boolean) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if (isCamera){
                        takePhotoFromCamera()
                    }else{
                        pickFromGallery()
                    }
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

    private fun pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        context as Activity
        context.startActivityForResult(intent, REQUEST_GALLERY_IMAGE)

    }

    private fun takePhotoFromCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference to access to future access
        val photoFileName = Helpers.FileHelper.getFileName()+".jpg"
        photoFile = Helpers.FileHelper.getFileFromUri(context, photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", photoFile!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.packageManager) != null) {
            // Start the image capture intent to take photo
            context as Activity
            context.startActivityForResult(intent, REQUEST_CAMERA_IMAGE)
        }

    }

    companion object {
        var photoFile: File? = null
    }
}