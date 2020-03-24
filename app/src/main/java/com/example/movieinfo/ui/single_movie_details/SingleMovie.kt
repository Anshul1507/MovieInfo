package com.example.movieinfo.ui.single_movie_details

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import com.bumptech.glide.Glide
import com.example.movieinfo.R
import com.example.movieinfo.data.api.MovieDBClient
import com.example.movieinfo.data.api.MovieDBInterface
import com.example.movieinfo.data.api.TMDB_POSTER_BASE_URL
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*


class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var titleMovie: String
    private lateinit var movieHomePageURL: String
    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    var customTabsSession: CustomTabsSession? = null
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        mContext = applicationContext
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val movieId: Int = intent.getIntExtra("movieID",1)

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
            titleMovie = it.title
            movieHomePageURL = it.homepage
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            text_error.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        fab_trailer.setOnClickListener {
            val ytTitle : String = "http://www.youtube.com/results?search_query=${titleMovie}"
            ytParser(ytTitle)
        }

        fab_homepage.setOnClickListener {
            customTabLinking(movieHomePageURL)
        }
    }

    private fun ytParser(id: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(id)
        startActivity(intent)
    }

    private fun bindUI(it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " mins."
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = TMDB_POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(image_poster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel{
        return ViewModelProvider(this, object : Factory {
            override fun <T: ViewModel?> create(modelClass: Class<T>):T{
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun customTabLinking(url: String?) {
        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                customTabsClient: CustomTabsClient
            ) { //pre-warning means to fast the surfing
                mClient = customTabsClient
                mClient!!.warmup(0L)
                customTabsSession = mClient!!.newSession(null)
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                mClient = null
            }
        }
        CustomTabsClient.bindCustomTabsService(
            mContext,
            "com.android.chrome",
            customTabsServiceConnection as CustomTabsServiceConnection
        )
        val uri = Uri.parse(url)
        //Create an Intent Builder
        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        intentBuilder.setSecondaryToolbarColor(
            ContextCompat.getColor(
                mContext,
                R.color.colorPrimaryDark
            )
        )
        //Set Start and Exit Animations
        intentBuilder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left)
        intentBuilder.setExitAnimations(
            mContext,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        //build custom tabs intent
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.intent.setPackage("com.android.chrome")
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intentBuilder.setShowTitle(true)
        intentBuilder.enableUrlBarHiding()
        customTabsIntent.launchUrl(mContext, uri)
    }
}
