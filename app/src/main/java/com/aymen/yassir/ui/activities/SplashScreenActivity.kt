package com.aymen.yassir.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aymen.yassir.R
import com.aymen.yassir.databinding.ActivitySplashScreenBinding
import com.aymen.yassir.ui.popups.NoInternetConnexionPopupActivity
import com.aymen.yassir.utils.GOOGLE_URL
import java.net.HttpURLConnection
import java.net.URL

/**
 * startup activity
 * start HomeScreenActivity
 * @author Aymen Masmoudi
 * */
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private var isPaused:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //extend interface layout to full screen
        binding.rlSplash.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    }

    //test internet connection
    fun isConnected(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected) {
                // Network is available but check if we can get access from the server.
                val url = URL(GOOGLE_URL)
                val urlc: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 2000 // Timeout 2 seconds.
                urlc.connect()
                if (urlc.responseCode == 200){ // Successful response.
                    return true
                } else {//server offline
                    serverConnexionProblems(resources.getString(R.string.splash_server_down))
                }
            } else {//no internet
                serverConnexionProblems(resources.getString(R.string.splash_no_internet))
            }
        } catch (e: Exception) {//server inaccessible
            e.printStackTrace()
            serverConnexionProblems(resources.getString(R.string.splash_server_unreachable))
        }
        return false
    }

    //enable server connexion errors
    private fun serverConnexionProblems(message:String){
        runOnUiThread {
            if (message == resources.getString(R.string.splash_no_internet)){
                startActivity(Intent(this@SplashScreenActivity, NoInternetConnexionPopupActivity::class.java))
            }else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    //start test every 5 seconds
    override fun onResume() {
        super.onResume()
        isPaused = false
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    do {
                        // Thread will sleep for 2 seconds
                        sleep(2000)
                    }while (!isPaused && !isConnected())
                    if (!isPaused && isConnected()){
                        startActivity(Intent(this@SplashScreenActivity, HomeScreenActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        // start thread
        background.start()
    }

    //pause test when activity is paused
    override fun onPause() {
        isPaused = true
        super.onPause()
    }

}
