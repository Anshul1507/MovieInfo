package com.example.movieinfo.data.api

import com.example.movieinfo.data.vo.MovieDetails
import com.example.movieinfo.data.vo.popular.PopularMovieResponse
import com.example.movieinfo.data.vo.search.SearchMovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page" ) page: Int): Single<PopularMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("i={imdb_id}")
    fun getMovieByImdbId(@Path("imdb_id") imdbID: Int): Single<SearchMovieResponse>

    @GET("t={movie_title}")
    fun getMovieByTitle(@Path("movie_title") movieTitle: String): Single<SearchMovieResponse>

}