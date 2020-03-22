package com.example.movieinfo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieinfo.R
import com.example.movieinfo.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*

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
