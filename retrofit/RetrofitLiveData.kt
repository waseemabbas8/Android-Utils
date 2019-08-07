package ilmkidunya.softsolutions.com.onlinetestsquizzes.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ilmkidunya.softsolutions.com.onlinetestsquizzes.model.RequestState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitLiveData<T>(private val call: Call<T>, private var retry: Int = 3) : LiveData<ResponseBody<T>>(),
    Callback<T> {

    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun onActive() {
        if (!call.isCanceled && !call.isExecuted) call.enqueue(this)
    }

    override fun onFailure(call: Call<T>?, t: Throwable) {
        Log.e("Response Error", "retry $retry", t)

        retry -= 1
        if (retry > 0) {
            call?.clone()?.enqueue(this)
        } else {
            val err = ResponseError(0, t.localizedMessage ?: "")
            postValue(ResponseBody(null, err))
            updateState(RequestState.ERROR)
        }
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        try {
            if (response != null) {
                if (response.isSuccessful) {
                    postValue(ResponseBody(response.body(), null))
                    updateState(RequestState.DONE)
                } else {
                    val err = ResponseError(response.code(), response.errorBody()?.string() ?: "")
                    Log.w("Response Error", err.message)
                    postValue(ResponseBody(null, err))
                    updateState(RequestState.ERROR)
                }
            }
        } catch (e: Exception) {
            val err = ResponseError(0, e.localizedMessage ?: "")
            postValue(ResponseBody(null, err))
            updateState(RequestState.ERROR)
        }
    }

    fun cancel() {
        if (!call.isCanceled) {
            call.cancel()
        }
    }

    private fun updateState(state: RequestState) {
        this.state.postValue(state)
    }

    fun retry(){
        call.clone().enqueue(this)
        updateState(RequestState.LOADING)
    }
}