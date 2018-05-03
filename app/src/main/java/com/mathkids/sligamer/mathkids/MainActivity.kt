package com.mathkids.sligamer.mathkids

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.ViewTreeObserver
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.animation.*
import android.widget.ImageView
import java.util.*

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Main Activity Start Screen
 * Responsible for seeding database and sending to instructions activity
 * Plugin Support with kotlin_version = '1.2.40'
 */
class MainActivity : AppCompatActivity(), View.OnClickListener{

    // ADD ANIMATION
    lateinit var mStarter: Button
    var mContainer: ViewGroup? = null
    private val sAccelerator = AccelerateInterpolator()
    private val sDecelerator = DecelerateInterpolator()
    private val sLinearInterpolator = LinearInterpolator()
    var SHORT_DURATION: Long = 100
    var MEDIUM_DURATION: Long = 200
    var REGULAR_DURATION: Long = 300
    var LONG_DURATION: Long = 500
    private val sDurationScale = 1f
    private lateinit var startGameBtn: Button
    private lateinit var redBalloon: ImageView
    private lateinit var blueBalloon: ImageView
    private lateinit var greenBalloon: ImageView
    private lateinit var animation : TranslateAnimation

    private lateinit var balloonAnimation: AnimationDrawable
    private lateinit var animationHandler: Handler
    private lateinit var databaseHandler: Handler
    private lateinit var db: DbContextHelperClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        // TASK 1: SET LAYOUTS AND UI ELEMENTS
        setContentView(R.layout.activity_main) //TODO: add main drawables for this main activity splash screen

        // START RUNNABLES
        animationHandler = Handler()
        animationHandler.postDelayed(animateBalloonsRunnable,  50)

        // TASK 2: SETUP THE QUESTIONS DATABASE
        databaseHandler = Handler()
        databaseHandler.postDelayed(setupDatabaseRunnable,  0)

        startGameBtn = findViewById(R.id.startGameBtn)
        startGameBtn.setOnClickListener(this)


}

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }



    // ONCLICK EVENT IF CLICK SEND INTENT TO LEVEL SELECTION MENU
   override fun onClick(v: View?) {
        //start instructions activity
        val playIntent = Intent(this, SelectLevelActivity().javaClass)
        this.startActivity(playIntent)
    }

    // THREAD HANDLER FOR UPDATING BALLOON GRIDVIEW
    private val setupDatabaseRunnable: Runnable = object: Runnable {
        override fun run() {
            //TODO: do we add settings to check if database of questions does not exist.
            // Allow for a settings screen to create db and new randomized questions
            // TODO move this database constructor to another activity perhaps in thread handler as well
            db = DbContextHelperClass(applicationContext)
        }
    }

    private val animateBalloonsRunnable: Runnable = object: Runnable {
        override fun run() {
            redBalloon = findViewById(R.id.imageViewRed)
            redBalloon.setBackgroundResource(R.drawable.red_balloon_animation)
            /*        balloonAnimation = redBalloon.background as AnimationDrawable
                 balloonAnimation.start()*/

            blueBalloon = findViewById(R.id.imageViewBlue)
            blueBalloon.setBackgroundResource(R.drawable.blue_balloon_animation)
            /*           balloonAnimation = blueBalloon.background as AnimationDrawable
                     balloonAnimation.start()*/

            greenBalloon = findViewById(R.id.imageViewGreen)
            greenBalloon.setBackgroundResource(R.drawable.green_balloon_animation)
            balloonAnimation = greenBalloon.background as AnimationDrawable
            balloonAnimation.start()

            /*  // Load the animation like this
              var animSlide = AnimationUtils.loadAnimation(applicationContext,
                      R.anim.floating_balloons)

              // Start the animation like this
              redBalloon.startAnimation(animSlide)*/


            animation = TranslateAnimation(50.0f, 100.0f,0.0f, 100.0f)
            animation.duration = 5000
            animation.repeatCount = 10
            animation.repeatMode = 2
            animation.fillAfter = true
            blueBalloon.startAnimation(animation)


            animation = TranslateAnimation(0.0f, 100.0f,0.0f, 100.0f)
            animation.duration = 5000
            animation.repeatCount = 10
            animation.repeatMode = 2
            animation.fillAfter = true
            redBalloon.startAnimation(animation)

        }
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
