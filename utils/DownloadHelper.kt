package ilmkidunya.softsolutions.com.onlinetestsquizzes.util

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import ilmkidunya.softsolutions.com.onlinetestsquizzes.R
import java.io.File

object DownloadHelper {

    private var downloadReference: Long = 0
    private lateinit var downloadManager: DownloadManager
    private lateinit var downloadtask: DownloadingTask

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("StringFormatInvalid")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId != downloadReference) {
                    context.unregisterReceiver(this)
                    return
                }
                val query = DownloadManager.Query()
                query.setFilterById(downloadReference)
                val cursor = downloadManager.query(query)
                cursor?.let {
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            var localFile = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

                            if (localFile.contains("file:///")) {
                                localFile = localFile.removePrefix("file:///").substringBeforeLast(File.separator)
                            }
                            Toast.makeText(context, context.resources.getString(R.string.saved), Toast.LENGTH_SHORT).show()
                            downloadtask.onDownloadComplete()

                        } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                            val message = context.resources.getString(R.string.error_download, cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON)))
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            downloadtask.onDownloadFailed()
                        }
                    }
                    cursor.close()
                }

                context.unregisterReceiver(this)

            }
        }
    }

    fun downloadFile(url: String, mimeType: String? = null, mContext: Context) {

        val guessFileName = URLUtil.guessFileName(url, null, mimeType)

        val context = mContext.applicationContext

        downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri)
        request.apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            //setAllowedOverRoaming(true)
            setTitle(guessFileName)
            setDescription("Downloading your file")
            setVisibleInDownloadsUi(true)
            allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

//            request.setDestinationUri(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)))
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, guessFileName)

            context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

            downloadReference = downloadManager.enqueue(this)
        }
    }

    fun setDownloadTaskListener(downloadingTask: DownloadingTask){
        this.downloadtask = downloadingTask
    }

    interface DownloadingTask {
        fun onDownloadComplete()
        fun onDownloadFailed()
    }

}