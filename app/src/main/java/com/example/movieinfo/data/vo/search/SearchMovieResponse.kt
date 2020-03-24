package com.example.movieinfo.data.vo.search


import com.google.gson.annotations.SerializedName

data class SearchMovieResponse(
    @SerializedName("Actors")
    val actors: String,
    @SerializedName("Title")
    val movieTitle: String,
    @SerializedName("Director")
    val director: String,
    @SerializedName("Genre")
    val genre: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    @SerializedName("Language")
    val language: String,
    @SerializedName("Response")
    val response: String
)