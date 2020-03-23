package com.example.movieinfo.data.vo.popular


import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
    val page: Int,
    @SerializedName("results")
    val movieList: List<PopularMovieDetails>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)