package com.example.movieinfo.ui.popular_movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieinfo.R
import com.example.movieinfo.ui.single_movie_details.SingleMovie

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_test.setOnClickListener {
            val intent = Intent(this,SingleMovie::class.java)
            intent.putExtra("movieID",299534)
            this.startActivity(intent)
        }
    }
}
