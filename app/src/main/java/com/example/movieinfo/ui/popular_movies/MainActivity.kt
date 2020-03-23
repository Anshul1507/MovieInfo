package com.example.movieinfo.ui.popular_movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieinfo.R
import com.example.movieinfo.data.api.MovieDBClient
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.ui.single_movie_details.SingleMovie
import com.example.movieinfo.ui.single_movie_details.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularMovieViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3 //network view type occupy all 3 spans
            }
        }

        recycler_view_popular_movies.layoutManager = gridLayoutManager
        recycler_view_popular_movies.setHasFixedSize(true)
        recycler_view_popular_movies.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            text_error_popular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel() : PopularMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T: ViewModel?> create(modelClass: Class<T>):T{
                return PopularMovieViewModel(movieRepository) as T
            }
        })[PopularMovieViewModel::class.java]
    }
}
