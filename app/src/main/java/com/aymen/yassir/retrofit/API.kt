package com.aymen.yassir.retrofit

/**
 * get api instance
 * @author Aymen Masmoudi
 * */
object API {

    // get movies api instance
    val MOVIES_API : MoviesAPI by lazy {
        RetrofitInstance().retrofit.create(MoviesAPI::class.java)
    }

}