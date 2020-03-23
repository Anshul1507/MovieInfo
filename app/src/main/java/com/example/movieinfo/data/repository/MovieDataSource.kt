package com.example.movieinfo.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieinfo.data.api.FIRST_PAGE
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.vo.popular.PopularMovieDetails
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService: MovieDBInterface,private val compositeDisposable: CompositeDisposable):
    PageKeyedDataSource<Int, PopularMovieDetails>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovieDetails>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
           apiService.getPopularMovies(page)
               .subscribeOn(Schedulers.io())
               .subscribe(
                   {
                        callback.onResult(it.movieList,null,page+1)
                       networkState.postValue(NetworkState.LOADED)
                   },
                   {
                        networkState.postValue(NetworkState.ERROR)
                       Log.e("MovieDataSource", it.message!!)
                   }
               )

        )
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieDetails>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key){
                            callback.onResult(it.movieList,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message!!)
                    }
                )

        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieDetails>
    ) {

    }

}