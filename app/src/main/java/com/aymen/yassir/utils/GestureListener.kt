package com.aymen.yassir.utils

import android.view.GestureDetector
import android.view.MotionEvent
import java.lang.Exception
import kotlin.math.abs

/**
 * detect user finger gesture to know if user swipe up or down
 * @author Aymen Masmoudi
 * */
class GestureListener(private val gestureInterface: GestureInterface?) : GestureDetector.SimpleOnGestureListener(){

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return true
    }


    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val result = false
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffY) > abs(diffX)) {
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        gestureInterface!!.swipeDown()
                    } else {
                        gestureInterface!!.swipeUp()
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return result
    }

    interface GestureInterface {
        fun swipeUp()
        fun swipeDown()
    }


}