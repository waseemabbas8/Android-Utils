package softsolutions.com.tutors.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(app: Application) : Boolean {
        val connectivityManger = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManger.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}