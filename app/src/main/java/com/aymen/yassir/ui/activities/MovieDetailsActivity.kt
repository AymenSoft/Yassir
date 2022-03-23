package com.aymen.yassir.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aymen.yassir.R
import com.aymen.yassir.databinding.ActivityMovieDetailsBinding
import com.aymen.yassir.models.Genre
import com.aymen.yassir.models.MovieDetails
import com.aymen.yassir.retrofit.API
import com.aymen.yassir.utils.API_KEY
import com.aymen.yassir.utils.BIG_POSTER_URL
import com.aymen.yassir.utils.GestureListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * show movie details
 * @author Aymen Masmoudi
 * */
@SuppressLint("ClickableViewAccessibility")
class MovieDetailsActivity: AppCompatActivity(), View.OnTouchListener, GestureListener.GestureInterface {

    private lateinit var binding: ActivityMovieDetailsBinding

    //detect user finger gestures
    private lateinit var gestureDetector: GestureDetector

    //detect body position to slide it up and down
    private var bodyPosition = 0 //0:default;1:bottom;2top

    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //extend interface layout to full screen
        binding.layoutDetails.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //get gesture detector instance
        gestureDetector = GestureDetector(this, GestureListener(this))
        //listen to view swipe events
        binding.rlBody.setOnTouchListener(this)

        binding.imgBack.setOnClickListener {
            finish()
        }

        //get movie id
        val data = intent
        movieId = data.getIntExtra("movieId", 0)
        if (movieId > 0) getMovieDetails() else finish()

    }

    //get movie details
    private fun getMovieDetails(){
        API.MOVIES_API.getMovieDetails(movieId, API_KEY)
            .enqueue(object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    //parse data from result
                    val movie = Gson().fromJson(response.body()!!.toString(),MovieDetails::class.java)

                    binding.tvName.text = movie.title
                    binding.tvRelease.text = String.format(resources.getString(R.string.release), movie.releaseDate)
                    binding.tvOverview.text = String.format(resources.getString(R.string.overview), movie.overview)

                    movieCategories(movie.genres)

                    val poster = "$BIG_POSTER_URL${movie.posterPath}"
                    Glide.with(this@MovieDetailsActivity)
                        .load(poster)
                        .into(binding.imgPoster)

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    finish()
                }

            })
    }

    //show movie categories as tags
    private fun movieCategories(genres: List<Genre>){
        val categories = mutableListOf<String>()
        for (genre in genres){
            categories.add(genre.name)
        }
        binding.tagCategories.tags = categories
    }

    //detect touch events
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    //on swipe up events
    override fun swipeUp() {
        when (bodyPosition) {
            0 -> {
                binding.layoutDetails.transitionToState(R.id.top)
                bodyPosition = 2
            }
            1 -> {
                binding.layoutDetails.transitionToState(R.id.middle)
                bodyPosition = 0
            }
        }
    }

    //on swipe down events
    override fun swipeDown() {
        when (bodyPosition) {
            0 -> {
                binding.layoutDetails.transitionToState(R.id.bottom)
                bodyPosition = 1
            }
            2 -> {
                binding.layoutDetails.transitionToState(R.id.middle)
                bodyPosition = 0
            }
        }
    }

}