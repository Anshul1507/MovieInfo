package com.example.movieinfo.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "910cb471f3326152066529eef1b406b2"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original/"

const val FIRST_PAGE = 1
const val MOVIES_PER_PAGE = 20
//    https://image.tmdb.org/t/p/original/yFSIUVTCvgYrpalUktulvk3Gi5Y.jpg
//    https://api.themoviedb.org/3/movie/299534?api_key=910cb471f3326152066529eef1b406b2
//    https://api.themoviedb.org/3/movie/popular?api_key=910cb471f3326152066529eef1b406b2&page=1


object MovieDBClient {

    fun getClient(): MovieDBInterface {
        val requestInterceptor = Interceptor {

            val url = it .request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieDBInterface::class.java)
    }
}