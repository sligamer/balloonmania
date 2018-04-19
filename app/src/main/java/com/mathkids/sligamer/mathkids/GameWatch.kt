package com.mathkids.sligamer.mathkids

import android.os.AsyncTask
import android.util.Log
import java.lang.Thread.sleep

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * StopWatch Class for level timing
 * Plugin Support with kotlin_version = '1.2.31'
 */
class GameWatch : AsyncTask<Int, Int, Int>{

    // Great idea for implementing an timer async tasks

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to [.execute]
     * by the caller of this task.
     *
     * This method can call [.publishProgress] to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     *
     * @return A result, defined by the subclass of this task.
     *
     * @see .onPreExecute
     * @see .onPostExecute
     *
     * @see .publishProgress
     */
    override fun doInBackground(vararg params: Int?): Int {
        // setting the default number of levels ..
        var LevelsCountSteps: Int? = 3
        return 0
    }

    private fun sleep(stepDur: Int?) {
        try {
            val mDelay: Long = 1000
            Thread.sleep(mDelay)
        } catch (e: InterruptedException) {
            Log.e("Execute Timer", e.toString())
        }

    }

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
