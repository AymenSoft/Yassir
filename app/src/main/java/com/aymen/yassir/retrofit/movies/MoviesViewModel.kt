package com.aymen.yassir.retrofit.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aymen.yassir.models.MovieDetails
import com.aymen.yassir.models.MoviesItem
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel constructor(private val repository: MoviesRepository): ViewModel() {

    val moviesList = MutableLiveData<Array<MoviesItem>>()
    val movieDetails = MutableLiveData<MovieDetails>()
    val errorMessage = MutableLiveData<String>()

    fun getAllMovies(page: Int){
        val response = repository.getMovies(page)
        response.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val list = Gson().fromJson(response.body()!!.getAsJsonArray("results").toString(),Array<MoviesItem>::class.java)
                moviesList.postValue(list)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getMovieDetails(id: Int){
        val response = repository.getMovieDetails(id)
        response.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val movie = Gson().fromJson(response.body()!!.toString(),MovieDetails::class.java)
                movieDetails.postValue(movie)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }

}