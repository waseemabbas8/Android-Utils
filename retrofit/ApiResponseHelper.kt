package softsolutions.com.tutors.utils

import androidx.lifecycle.liveData
import retrofit2.HttpException
import retrofit2.Response
import softsolutions.com.tutors.data.remote.ResponseBody
import softsolutions.com.tutors.data.remote.ResponseError
import java.lang.Exception

object ApiResponseHelper {

    fun <T> create(response: Response<T>) : ResponseBody<T> {

        try {
            if (response.isSuccessful){
                return ResponseBody(response.body(), null)
            }
            return ResponseBody(
                null,
                ResponseError(response.code(), response.message())
            )
        }catch (ex: HttpException){
            return ResponseBody(
                null,
                ResponseError(ex.code(), ex.message())
            )
        }catch (e: Exception){
            return ResponseBody(
                null,
                ResponseError(0, e.localizedMessage)
            )
        }
    }

}