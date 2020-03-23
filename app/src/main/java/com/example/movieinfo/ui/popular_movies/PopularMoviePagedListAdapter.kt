package com.example.movieinfo.ui.popular_movies

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieinfo.R
import com.example.movieinfo.data.api.POSTER_BASE_URL
import com.example.movieinfo.data.repository.NetworkState
import com.example.movieinfo.data.vo.popular.PopularMovieDetails
import com.example.movieinfo.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_single_movie.view.*
import kotlinx.android.synthetic.main.item_popular_movies.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class PopularMoviePagedListAdapter(public val context: Context): PagedListAdapter<PopularMovieDetails,RecyclerView.ViewHolder>(MovieDiffCallback()){

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.item_popular_movies,parent,false)
            return MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRows(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRows()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRows() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(movie: PopularMovieDetails?,context: Context){
            itemView.cv_popular_movie_title.text = movie?.title
            itemView.cv_popular_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_popular_movie)

            itemView.setOnClickListener {
                val intent = Intent(context,SingleMovie::class.java)
                intent.putExtra("movieID",movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.progress_bar_item.visibility = View.VISIBLE
            }else{
                itemView.progress_bar_item.visibility = View.GONE
            }

            if(networkState != null && networkState == NetworkState.ERROR){
                itemView.error_message_item.visibility = View.VISIBLE
                itemView.error_message_item.text = networkState.msg
            }else if (networkState != null && networkState == NetworkState.ENDOFLIST){
                itemView.error_message_item.visibility = View.VISIBLE
                itemView.error_message_item.text = networkState.msg
            }else{
                itemView.error_message_item.visibility = View.GONE
            }
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<PopularMovieDetails>(){
        override fun areItemsTheSame(
            oldItem: PopularMovieDetails,
            newItem: PopularMovieDetails
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovieDetails,
            newItem: PopularMovieDetails
        ): Boolean {
            return oldItem == newItem
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRows()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRows()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount()) //remove the progress bar at the end
            }else{
                notifyItemInserted(super.getItemCount()) //add the progress bar at the end
            }
        }else if(hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount-1)
        }
    }
}