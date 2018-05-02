package com.mathkids.sligamer.mathkids

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.view.animation.LinearInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.AccelerateInterpolator
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
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ImageView

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

    private lateinit var balloonAnimation: AnimationDrawable
    private lateinit var db: DbContextHelperClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        // TASK 1: SET LAYOUTS AND UI ELEMENTS
        setContentView(R.layout.activity_main) //TODO: add main drawables for this main activity splash screen

        //mStarter = findViewById<Button>(R.id.startGameBtn)
        //mContainer = findViewById(R.id.container)
        //mStarter.setOnTouchListener(funButtonListener)
        //mStarter.animate().duration = 100
/*        redBalloon = findViewById(R.id.imageViewRed)
        redBalloon.setBackgroundResource(R.drawable.red_balloon_animation)
        balloonAnimation = redBalloon.background as AnimationDrawable
        balloonAnimation.start()*/

     /*   blueBalloon = findViewById(R.id.imageViewBlue)
        blueBalloon.setBackgroundResource(R.drawable.blue_balloon_animation)
        balloonAnimation = blueBalloon.background as AnimationDrawable
        balloonAnimation.start()

        greenBalloon = findViewById(R.id.imageViewGreen)
        greenBalloon.setBackgroundResource(R.drawable.green_balloon_animation)
        balloonAnimation = greenBalloon.background as AnimationDrawable
        balloonAnimation.start()*/

        startGameBtn = findViewById(R.id.startGameBtn)
        startGameBtn.setOnClickListener(this)

        // TASK 2: SETUP THE QUESTIONS DATABASE
        //TODO: do we add settings to check if database of questions does not exist.
        // Allow for a settings screen to create db and new randomized questions
        // TODO move this database constructor to another activity perhaps in thread handler as well
        db = DbContextHelperClass(this)
}

    override fun onResume() {
        super.onResume()
//        mContainer!!.scaleX = 1F
//        mContainer!!.scaleY = 1F
//        mContainer!!.alpha = 1F
//        mStarter.visibility = View.INVISIBLE
//        mContainer!!.viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener)
    }

    override fun onPause() {
        super.onPause()
        //mStarter.removeCallbacks(mSquishRunnable)
    }



    // ONCLICK EVENT IF CLICK SEND INTENT TO LEVEL SELECTION MENU
   override fun onClick(v: View?) {
        //start instructions activity
        val playIntent = Intent(this, SelectLevelActivity().javaClass)
        this.startActivity(playIntent)
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
