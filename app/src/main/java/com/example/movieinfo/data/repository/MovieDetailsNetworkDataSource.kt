package com.example.movieinfo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(private val apiService:MovieDBInterface,private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource","${it.message}")
                        }
                    )

            )
        }catch (e: Exception){
            Log.e("MovieDetailsDataSource",e.message!!)
        }
    }
}