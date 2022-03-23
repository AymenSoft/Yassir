package com.aymen.yassir.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * movies api list
 * @author Aymen Masmoudi
 * */
interface MoviesAPI {

    /**
     * get movies list from server
     * @param api api key for security
     * @param sort sort movies by
     * @param page number of page for navigation
     * @return json object
     * */
    @GET("discover/movie")
    fun getMoviesList(
        @Query("api_key") api: String,
        @Query("sort_by") sort: String,
        @Query("page") page: Int
    ): Call<JsonObject>

    /**
     * get movie details by id from server
     * @param id actor id
     * @param api api key for security
     * @return json object
     * */
    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") api: String
    ): Call<JsonObject>

}
