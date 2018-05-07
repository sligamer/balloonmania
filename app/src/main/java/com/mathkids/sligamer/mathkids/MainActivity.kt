package com.mathkids.sligamer.mathkids


import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.view.animation.AnimationUtils
import java.util.concurrent.Executors


/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Math Mania
 * Main Activity Start Screen
 * Responsible for seeding database and sending to instructions activity
 * Plugin Support with kotlin_version = '1.2.41'
 */
class MainActivity : Activity(), View.OnClickListener{

    // DECLARE UI ELEMENTS
    private lateinit var startGameBtn: Button
    private lateinit var db: DbContextHelperClass
    private lateinit var redBalloon: ImageView
    private lateinit var blueBalloon: ImageView
    private lateinit var greenBalloon: ImageView
    private lateinit var animation: TranslateAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 1: SET LAYOUTS AND UI ELEMENTS
        setContentView(R.layout.activity_main)

        // START UI THREADS
        startAnimationFromBackgroundThread()

        startGameBtn = findViewById(R.id.startGameBtn)
        startGameBtn.setOnClickListener(this)
    }

    private fun startAnimationFromBackgroundThread() {
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit({
            // this runs on a background thread
            Log.v("AnimateBalloons", "Worker thread id:" + Thread.currentThread().id)
            this@MainActivity.runOnUiThread {
                Log.v("AnimateBalloons", "Animation thread id:" + Thread.currentThread().id)
                blueBalloon = findViewById(R.id.imageViewBlue)
                blueBalloon.setBackgroundResource(R.drawable.blue_balloon_animation)

                redBalloon = findViewById(R.id.imageViewRed)
                redBalloon.setBackgroundResource(R.drawable.red_balloon_animation)

                greenBalloon = findViewById(R.id.imageViewGreen)
                greenBalloon.setBackgroundResource(R.drawable.green_balloon_animation)

                animation = TranslateAnimation(-100.0f, 100.0f, 0.0f, 0.0f)
                animation.duration = 5000
                animation.repeatCount = 100
                animation.repeatMode = 2
                animation.fillAfter = true
                greenBalloon.startAnimation(animation)

                animation = TranslateAnimation(50.0f, 100.0f, 10.0f, 400.0f)
                animation.duration = 5000
                animation.repeatCount = 100
                animation.repeatMode = 2
                animation.fillAfter = true
                blueBalloon.startAnimation(animation)


                animation = TranslateAnimation(0.0f, 80.0f, 0.0f, 280.0f)
                animation.duration = 5000
                animation.repeatCount = 100
                animation.repeatMode = 2
                animation.fillAfter = true
                redBalloon.startAnimation(animation)

                val balloonAnimation = greenBalloon.background as AnimationDrawable
                balloonAnimation.start()


            }
        })
    }
    // ONCLICK EVENT IF CLICK SEND INTENT TO LEVEL SELECTION MENU
    override fun onClick(v: View) {
        Thread(Runnable {
            // a potentially  time consuming task
            db = DbContextHelperClass(applicationContext)
            db.close()
        }).start()

        val playIntent = Intent(this, SelectLevelActivity().javaClass)
        this.startActivity(playIntent)
    }

    override fun onBackPressed() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        if(id == R.string.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
