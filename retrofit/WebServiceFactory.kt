package com.peopleperfectae.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WebServiceFactory {

    private const val PEOPLE_API_URL = "https://ppiapi.peoplei.tech/api/"
    private var apiService: PeopleApi? = null

    fun createPeopleApiWithAuthHeader(accessToken: String) = createWithAuthHeader(accessToken)

    private fun createWithAuthHeader(accessToken: String): PeopleApi {
        if (apiService == null) {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(OAuthInterceptor("Bearer", accessToken))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            apiService = Retrofit.Builder()
                .baseUrl(PEOPLE_API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PeopleApi::class.java)
        }

        return apiService!!
    }

    class OAuthInterceptor(private val tokenType: String, private val accessToken: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()
            request = request.newBuilder().header("Authorization", "$tokenType $accessToken").build()
            return chain.proceed(request)
        }
    }

}