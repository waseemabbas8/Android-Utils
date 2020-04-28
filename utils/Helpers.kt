package ilmkidunya.softsolutions.com.onlinetestsquizzes.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import android.provider.MediaStore.Images
import android.view.View
import android.widget.Button
import ilmkidunya.softsolutions.com.onlinetestsquizzes.R


class Helpers {

    companion object{
	fun makeProgressButton(view: Button, loadingTextRes: Int){
            view.showProgress {
                buttonTextRes = loadingTextRes
                progressColor = view.currentTextColor
            }
        }
	
        fun showInfoDialog(title: String, msg: String, context: Context){
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok"
                ) { dialog, id ->

                }.create().show()
        }

        fun showMinSecTime(millisUntilFinished: Long) : String{
            val time = "" + String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            return time
        }

        fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
            itemView.setOnClickListener {
                event.invoke(adapterPosition, itemViewType)
            }
            return this
        }

        fun getDateString(isoDate: String) : String{
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val dateFormat = SimpleDateFormat("dd MMM yyyy")

            return try {
                val date = inputDateFormat.parse(isoDate)
                val c = Calendar.getInstance()
                c.time = date
                dateFormat.format(c.time)
            } catch (e: ParseException) {
                isoDate
            }
        }

        fun getFileName() : String{
            val tsLong = System.currentTimeMillis()/1000
            return tsLong.toString()
        }

        fun getMimeType(url: String): String? {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }

        fun getMineType(uri: Uri?, context: Context?) : String? {
            return try {
                val cR = context?.contentResolver
                val mime = MimeTypeMap.getSingleton()
                mime.getExtensionFromMimeType(uri?.let { cR?.getType(it) })
            }catch (e: Exception){
                ""
            }
        }

        fun getFirstWordOfString(str: String) : String = str.substringBefore(" ")

        fun shareImage(bitmap: Bitmap, context: Context){
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Here is my Quiz Score")
            val path = Images.Media.insertImage(context.contentResolver, bitmap, "", null)
            val screenshotUri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            intent.type = "image/*"
            context.startActivity(Intent.createChooser(intent, "Share image via..."))
        }

        fun getBitmapFromUrlAndShare(url: String, context: Context, view: View){
            view.isEnabled = false
            if (view is Button){
                view.text = "Please Wait..."
            }

            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        view.isEnabled = true
                        if (view is Button){
                            view.text = context.getString(R.string.label_share_score)
                        }
                        shareImage(resource, context)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
        }

    }
	
	object FileHelper {
        fun getTextFromResources(context: Context, resId: Int) = context.resources.openRawResource(resId).use { inputStream ->
            inputStream.bufferedReader().use {
                it.readText()
            }
        }
		
		private fun ContentResolver.getFileName(fileUri: Uri, context: Context): String {

            var name = ""
            val returnCursor = this.query(fileUri, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                name = returnCursor.getString(nameIndex) ?: getFileName() + "." + getMineType(fileUri, context)
                returnCursor.close()
            }

            return name
        }

        fun createSelectedFileCopy(uri: Uri, context: Context): File? {
            val parcelFileDescriptor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                context.contentResolver.openFileDescriptor(uri, "r", null)
            } else {
                null
            }
            parcelFileDescriptor?.let {
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(context.cacheDir, context.contentResolver.getFileName(uri, context))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                return file
            }
            return null
        }
    }
}