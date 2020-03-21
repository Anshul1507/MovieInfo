package com.example.movieinfo.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.repository.MovieDetailsNetworkDataSource
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService:MovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int):LiveData<MovieDetails>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}