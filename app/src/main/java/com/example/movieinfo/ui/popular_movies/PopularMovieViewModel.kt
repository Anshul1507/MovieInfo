package com.example.movieinfo.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.data.vo.popular.PopularMovieDetails
import io.reactivex.disposables.CompositeDisposable

class PopularMovieViewModel(private val movieRepository: MoviePagedListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<PopularMovieDetails>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}