package com.aymen.yassir.retrofit.movies

import com.aymen.yassir.retrofit.API
import com.aymen.yassir.utils.API_KEY

class MoviesRepository{

    fun getMovies(page: Int) = API.MOVIES_API.getMoviesList(API_KEY, "popularity.desc", page)

    fun getMovieDetails(id: Int) = API.MOVIES_API.getMovieDetails(id, API_KEY)

}