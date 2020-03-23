package com.example.movieinfo.data.api

import com.example.movieinfo.data.vo.MovieDetails
import com.example.movieinfo.data.vo.popular.PopularMovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page" ) page: Int): Single<PopularMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

}