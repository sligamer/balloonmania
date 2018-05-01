package com.mathkids.sligamer.mathkids

import android.os.AsyncTask
import android.util.Log
import java.lang.Thread.sleep

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * StopWatch Class for level timing
 * Plugin Support with kotlin_version = '1.2.40'
 */
class GameWatch {


    // DECLARE TIME ELEMENTS
    private var mStartTime: Long = 0L
    private var mTimeUpdate: Long = 0L
    private var mStoredTime: Long = 0L

    constructor()
    {
        mStartTime = 0L
        mTimeUpdate = 0L
        mStoredTime = 0L
    }

    fun resetWatchTime()
    {
        mStartTime = 0L
        mTimeUpdate = 0L
        mStoredTime = 0L
    }

    fun setStartTime(startTime: Long)
    {
        mStartTime = startTime
    }

    fun getStartTime()
            : Long
    {
        return mStartTime
    }

    fun setTimeUpdate(timeUpdate: Long)
    {
        mTimeUpdate = timeUpdate
    }

    fun getTimeUpdate()
            : Long
    {
        return mTimeUpdate
    }

    fun addStoredTime(timeInMilliseconds: Long)
    {
        mStoredTime += timeInMilliseconds
    }

    fun getStoredTime()
            : Long {
        return mStoredTime
    }
}
