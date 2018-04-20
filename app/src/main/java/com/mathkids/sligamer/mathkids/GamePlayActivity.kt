package com.mathkids.sligamer.mathkids


import android.os.AsyncTask.execute
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.lang.Integer.parseInt
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AlertDialog
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import java.util.*

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Plugin Support with kotlin_version = '1.2.31'
 */

class GamePlayActivity: AppCompatActivity(){

    private lateinit var db: DbContextHelperClass
    private lateinit var question: TextView
    private lateinit var answerTxt: TextView
    private lateinit var scoreTxt: TextView
    private lateinit var gridView: GridView

    private var level: Int  = 0
    private var questionID: Int = 0
    private var answer: Int = 0

    // UI: ELEMENTS: BUTTON TOGGLE
    private lateinit var timeDisplay: TextView

    // TIME ELEMENTS
    private lateinit var mHandler: Handler

    // WATCH TIME CLASS
    private lateinit var watchTime: GameWatch

    private val gameOptions = arrayOf("Instructions", "Start Game")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // TASK 1: SET THE LAYOUT AND UI ELEMENTS
        setContentView(R.layout.playgame_layout)

        question = findViewById(R.id.question)
        answerTxt = findViewById(R.id.answer)
        scoreTxt = findViewById(R.id.score)
        timeDisplay = findViewById(R.id.timerView)


        // TASK 2: SET INTENT TO PASS LEVEL SELECTION TO NEXT ACTIVITY
        val extras = intent.extras
        when {
            extras != null -> {
                val passedLevel = extras.getInt("level", -1)
                when {
                    passedLevel >= 0 ->
                        level = passedLevel
                }
                //questionID = getQuestion(passedLevel.toString())
            }
        }

        // TASK 3: SET GAME DEFAULTS
        setupGameDefaults()

        // TASK 4: INSTANTIATE THE OBJECT THAT MODELS THE STOPWATCH TIME
        watchTime = GameWatch()

        // TASK 5:  INSTANTIATE A HANDLER TO RUN ON THE UI THREAD
        mHandler = Handler()
        displayBalloons()

        // TASK: 6 START THE TIMER
        // TODO: change the timer to seconds add thread to stop game if level time reached
        //startTimer()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Timer Begins").setSingleChoiceItems(
                gameOptions,
                0
        ) { dialog, which ->
            startTimer()
            dialog.dismiss()
        }

        // TASK 3: SHOW THE DIALOG
        val ad = builder.create()
        ad.show()
    }


    // METHOD FOR SETTINGS DEFAULTS
    private fun setupGameDefaults() {
        // set score to 0
        scoreTxt.text = """Score: ${0}"""
        // set the answer to default
        answerTxt.text = "= ?"
    }

    // METHOD TO GET CURRENT USER SCORE
    private fun getScore(): Int {
        val scoreStr = scoreTxt.text.toString()
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ") + 1))
    }

    // METHOD TO DISPLAY BALLOON GRID IN ANOTHER THREAD
    private fun displayBalloons() {
        mHandler.postDelayed(displayBalloonsRunnable,  0)
    }

    // METHOD START THE TIMER
    private fun startTimer()
    {
        // TASK 2: SET THE START TIME AND CALL THE CUSTOM HANDLER
        watchTime.setStartTime(SystemClock.elapsedRealtime())
        mHandler.postDelayed(updateTimerRunnable,  0)

    }

    // THREAD HANDLER FOR UPDATING TIMER
    private val updateTimerRunnable: Runnable = object: Runnable {
        override fun run() {

            // TASK 1: COMPUTE THE TIME DIFFERENCE
            val time = (watchTime.getTimeUpdate() / 1000)

            // TASK 2: COMPUTE SECONDS
            val seconds = (time % 60)

            // TASK 3: DISPLAY THE TIME IN THE TIMERVIEW
            timeDisplay.text = String.format("%02d", seconds)

            // TASK 4: SPECIFY NO TIME LAPSE BETWEEN POSTING
            mHandler.postDelayed(this, 5)

            // TASK 4: CHECK THE LEVEL TIME HITS
            // LEVEL TIME GOTO NEXT LEVEL
            // OR RESTART LEVEL
            // TODO: TIMER LEVEL OPTIONS
            when (seconds){
                60L -> {
                    // TODO: create intent, fragment, or  animation to prompt players time ran out start next level
                }
            }
        }
    }


    // THREAD HANDLER FOR UPDATING BALLOON GRIDVIEW
    private val displayBalloonsRunnable: Runnable = object: Runnable {
        override fun run() {

            // TASK 1: Display balloons grid
            gridView = findViewById(R.id.gridview)
            db = DbContextHelperClass(applicationContext)
            val questions: ArrayList<QuestionClass>? = db.getQuestions(0, level.toString())
            val random = Random()
            val randomQuestion = random.nextInt(questions!!.count())
            question.text = questions!![randomQuestion].questionProblem
            answer = questions!![randomQuestion].questionAnswer.toInt()

            var madapter = ImageAdapter(applicationContext, questions)
            gridView.adapter = madapter

            // TASK 2: Implement On Item click listener
            gridView.onItemClickListener = OnItemClickListener { parent, view, position, id ->

                // TODO: add a fragment or intent here to execute this logic in thread handler
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

                            // set the answer to default
                            answerTxt.text = "= ?"
                            //TODO: Touch event gesture class to do animations

                            // restart thread
                            mHandler.postDelayed(this,  0)
                        } else {
                            //incorrect
                            answerTxt.text = "= ?"
                            var currentImage = madapter.getView(position, view, parent) as ImageView
                            //TODO: Touch event gesture class to do animations
                            currentImage.setImageResource(R.mipmap.response_balloon)

                        }
                    }
                } else {
                    answerTxt.text = enteredNum.toString()
                }
            }
        }
    }
}

