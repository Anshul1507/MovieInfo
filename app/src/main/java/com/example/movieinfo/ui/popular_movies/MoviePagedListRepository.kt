package com.example.movieinfo.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieinfo.data.api.MOVIES_PER_PAGE
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.repository.MovieDataSource
import com.example.movieinfo.data.repository.MovieDataSourceFactory
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.data.vo.popular.PopularMovieDetails
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService: MovieDBInterface){

    lateinit var moviePagedList: LiveData<PagedList<PopularMovieDetails>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable): LiveData<PagedList<PopularMovieDetails>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MOVIES_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviePagedList
    }
    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource,NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}