package ilmkidunya.softsolutions.com.onlinetestsquizzes.api

import ilmkidunya.softsolutions.com.onlinetestsquizzes.model.RequestState

data class ResponseBody<T>(
    var body: T?,
    var error: ResponseError?
)

data class ResponseError(
    var statusCode: Int,
    var message: String
)