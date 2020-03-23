package com.example.movieinfo.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.vo.popular.PopularMovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int,PopularMovieDetails>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, PopularMovieDetails> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}