package com.mathkids.sligamer.mathkids

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import java.lang.Integer.parseInt
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.timerTask


/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Math Mania
 * Plugin Support with kotlin_version = '1.2.41'
 */
class GamePlayActivity : Activity() {

    // UI: ELEMENTS
    private lateinit var question: TextView
    private lateinit var answerTxt: TextView
    private lateinit var scoreTxt: TextView
    private lateinit var levelNumber: TextView
    private lateinit var gridView: GridView
    private lateinit var timeDisplay: TextView

    // DECLARED GAME VARIABLES
    private var level: Int = 0
    private var answer: Int = 0

    // DECLARED DB ACCESS VARIABLE
    private lateinit var db: DbContextHelperClass

    // HANDLERS
    private lateinit var gHandler: Handler
    private lateinit var nHandler: Handler

    private lateinit var balloonAnimation: AnimationDrawable

    // ONCREATE VIEW
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // TASK 1: SET THE LAYOUT AND UI ELEMENTS
        setContentView(R.layout.playgame_layout)
        question = findViewById(R.id.question)
        answerTxt = findViewById(R.id.answer)
        scoreTxt = findViewById(R.id.score)
        timeDisplay = findViewById(R.id.timerView)
        levelNumber = findViewById(R.id.lvlView)

        // TASK 2: GET INTENT LEVEL SELECTION FROM LEVEL SELECTION ACTIVITY
        val extras = intent.extras
        when {
            extras != null -> {
                val passedLevel = extras.getInt("level", -1)
                when {
                    passedLevel >= 0 ->
                        level = passedLevel
                }
            }
        }

        // TASK 3: SET GAME DEFAULTS
        setupGameDefaults()

        // TASK 4:  INSTANTIATE A HANDLER TO RUN ON THE UI THREAD
        gHandler = Handler()
        nHandler = Handler()

        // TASK 5: RUN HANDLER TO DISPLAY BALLOON GRID
        displayBalloons()

        // TASK 6: INSTANTIATE THE OBJECT THAT MODELS THE STOPWAT
        startTimer()
    }

    // METHOD FOR SETTINGS DEFAULTS
    private fun setupGameDefaults() {
        // set score to 0
        scoreTxt.text = """Score: ${0}"""
        // set the answer to default
        answerTxt.text = "= ?"

        val myScore = getSharedPreferences("MyScores", Context.MODE_PRIVATE)
        val editor = myScore.edit().apply {
        }
        // clear
        editor.clear()
        editor.apply()
    }

    // METHOD TO GET CURRENT USER SCORE
    private fun getScore(): Int {
        val scoreStr = scoreTxt.text.toString()
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ") + 1))
    }

    // METHOD TO TRACK WHAT LEVEL
    private fun getLevel(): Int {
        return level
    }

    // METHOD TO DISPLAY BALLOON GRID IN ANOTHER THREAD
    private fun displayBalloons() {
        gHandler.postDelayed(displayBalloonsRunnable, 0)
    }

    // METHOD START THE COUNTDOWN TIMER
    private fun startTimer() {
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeDisplay.text = String.format("%02d", millisUntilFinished / 1000)
                val currentscore: Int = getScore()

                if (currentscore >= 10 && (millisUntilFinished / 1000) <= 30 && level == 0) {
                    // start level 2
                    level = 1
                    displayBalloons()
                }
                if (currentscore >= 15 && (millisUntilFinished / 1000) <= 45 && level == 1) {
                    // start level 3
                    level = 2
                    displayBalloons()
                }
                if (currentscore >= 30 && (millisUntilFinished / 1000) <= 60 && level == 2) {
                    nHandler.postDelayed(gameNotificationsRunnable, 0)
                }
            }

            override fun onFinish() {
                nHandler.postDelayed(gameNotificationsRunnable, 0)
            }
        }.start()
    }

    // METHOD TO SAVE SHARED PREFERENCES SCORES
    private fun saveScores() {
        val myScore = getSharedPreferences("MyScores", Context.MODE_PRIVATE)
        val editor = myScore.edit().apply {
            putString("Level", getLevel().toString())
            putInt("Score", getScore())
        }
        // save
        editor.apply()
    }

    // METHOD TO RETRIEVE SHARED PREFERENCES SCORES
    private fun getScores(): SharedPreferences {
        return getSharedPreferences("MyScores", Context.MODE_PRIVATE)
    }

    // METHOD TO ANIMATE POP BALLOON
    private fun startAnimationFromBackgroundThread(currentImage: ImageView) {
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit({
            // this runs on a background thread
            //Log.v("AnimateBalloons", "Worker thread id:" + Thread.currentThread().id)
            this@GamePlayActivity.runOnUiThread {
                //Log.v("AnimateBalloons", "Animation thread id:" + Thread.currentThread().id)
                currentImage.setBackgroundResource(R.drawable.pop_balloon_animation)
                balloonAnimation = currentImage.background as AnimationDrawable
                val time = balloonAnimation.numberOfFrames * balloonAnimation.getDuration(0)
                balloonAnimation.start()
                val timer = Timer()
                timer.schedule(timerTask { balloonAnimation.stop() }, time.toLong() + 100L)
            }
        })
    }

    // THREAD HANDLER FOR UPDATING BALLOON GRIDVIEW
    private val displayBalloonsRunnable: Runnable = object : Runnable {
        override fun run() {

            // set level
            levelNumber.text = getLevel().toString()
            // set the answer to default
            answerTxt.text = "= ?"

            //TODO: optimize refine query to not show questions with same answer
            // TASK 1: Display balloons grid
            gridView = findViewById(R.id.gridview)
            db = DbContextHelperClass(applicationContext)
            val questions: ArrayList<QuestionClass>? = db.getQuestions(0, level.toString())
            val random = Random()
            val randomQuestion = random.nextInt(questions!!.count())
            question.text = questions[randomQuestion].questionProblem
            answer = questions[randomQuestion].questionAnswer.toInt()
            val madapter = ImageAdapter(applicationContext, questions)
            gridView.adapter = madapter

            // TASK 2: Implement On Item click listener
            gridView.onItemClickListener = OnItemClickListener { parent, view, position, id ->

                // view tag Holds the answer to the question
                view.tag = view.tag

                // tag will be assigned to all the balloon images this will be the answer
                val enteredNum = parseInt(view!!.tag.toString())

                if (answerTxt.text.toString().endsWith("?")) {
                    answerTxt.text = """= $enteredNum"""
                    val answerContent = answerTxt.text.toString()
                    if (!answerContent.endsWith("?")) {
                        val enteredAnswer = Integer.parseInt(answerContent.substring(2))
                        val exScore = getScore()
                        if (enteredAnswer == answer) {
                            //correct
                            scoreTxt.text = """Score: ${exScore + 1}"""

                            // add score to shared preferences
                            saveScores()

                            // set the answer to view
                            answerTxt.text = answer.toString()

                            // restart thread
                            gHandler.postDelayed(this, 0)

                        } else {
                            //incorrect
                            // enable click
                            answerTxt.text = "= ?"
                            val currentImage = madapter.getView(position, view, parent) as ImageView
                            currentImage.setBackgroundResource(R.drawable.balloontransparent)
                            startAnimationFromBackgroundThread(currentImage)
                            currentImage.setImageResource(R.mipmap.response_balloon)
                        }
                    }
                } else {
                    answerTxt.text = enteredNum.toString()
                }
            }
        }
    }

    // THREAD HANDLER FOR UPDATING GAME NOTIFICATIONS
    private val gameNotificationsRunnable: Runnable = object : Runnable {
        override fun run() {

            val builder = AlertDialog.Builder(this@GamePlayActivity)
            val results = getScores()
            val inflater = LayoutInflater.from(this@GamePlayActivity)
            val customDialog = inflater.inflate(R.layout.scoreboard_layout, null)
            val scoreBoardImageView = customDialog.findViewWithTag("scoreBoardImageView") as ImageView
            scoreBoardImageView.setBackgroundResource(R.drawable.clipboard)
            val resultsView = customDialog.findViewWithTag("resultsTextView") as TextView
            resultsView.text = results.all.toString()
            builder.setTitle("Great Try!").setIcon(R.drawable.ic_notifications_black_24dp)
                    .setView(customDialog)

            // Setting Positive "Yes" Btn
            builder.setPositiveButton("Try Again",
                    { dialog, which ->
                        // start new game
                        val playIntent = Intent(this@GamePlayActivity, SelectLevelActivity().javaClass)
                        startActivity(playIntent)
                    })

            // Setting Negative "NO" Btn
            builder.setNegativeButton("Exit Game",
                    { dialog, which ->
                        // go to main menu
                        val playIntent = Intent(applicationContext, MainActivity().javaClass)
                        startActivity(playIntent)
                    })


            val ad = builder.create()
            ad.show()
        }
    }
}

