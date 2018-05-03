package com.mathkids.sligamer.mathkids

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.lang.Integer.parseInt
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AlertDialog
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import java.util.*
import android.util.Log
import kotlinx.android.synthetic.main.playgame_layout.*


/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Plugin Support with kotlin_version = '1.2.40'
 */

class GamePlayActivity: AppCompatActivity() {

    private lateinit var db: DbContextHelperClass
    private lateinit var question: TextView
    private lateinit var answerTxt: TextView
    private lateinit var scoreTxt: TextView
    private lateinit var levelNumber: TextView
    private lateinit var gridView: GridView

    private var level: Int  = 0
    private var answer: Int = 0

    // UI: ELEMENTS: BUTTON TOGGLE
    private lateinit var timeDisplay: TextView

    // TIME ELEMENTS
    private lateinit var tHandler: Handler
    private lateinit var gHandler: Handler
    private lateinit var nHandler: Handler

    // WATCH TIME CLASS
    private lateinit var watchTime: GameWatch

    private val gameOptions = arrayOf("Instructions", "Start Game")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // TASK 1: SET THE LAYOUT AND UI ELEMENTS
        setContentView(R.layout.playgame_layout)

        // Note that the Toolbar defined in the layout has the id "game_toolbar"
        setSupportActionBar(findViewById(R.id.game_toolbar))
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        question = findViewById(R.id.question)
        answerTxt = findViewById(R.id.answer)
        scoreTxt = findViewById(R.id.score)
        timeDisplay = findViewById(R.id.timerView)
        levelNumber = findViewById(R.id.lvlView)


        // TASK 2: SET INTENT TO PASS LEVEL SELECTION TO NEXT ACTIVITY
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

        // TASK 4: INSTANTIATE THE OBJECT THAT MODELS THE STOPWATCH TIME
        watchTime = GameWatch()

        // TASK 5:  INSTANTIATE A HANDLER TO RUN ON THE UI THREAD
        tHandler = Handler()
        gHandler = Handler()
        nHandler = Handler()


/*        val builder = AlertDialog.Builder(this)
        builder.setTitle("Timer Begins").setSingleChoiceItems(
                gameOptions,
                0
        ) { dialog, which ->
            dialog.dismiss()
            displayBalloons()
            // TASK: 6 START THE TIMER
            // TODO: change the timer to seconds add thread to stop game if level time reached
            startTimer()
        }

        // TASK 3: SHOW THE DIALOG
        val ad = builder.create()
        ad.show()*/

        displayBalloons()
        startTimer()
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

    private fun getLevel(): Int {
        return level
    }

    // METHOD TO DISPLAY BALLOON GRID IN ANOTHER THREAD
    private fun displayBalloons() {
        gHandler.postDelayed(displayBalloonsRunnable,  0)
    }

    // METHOD START THE TIMER
    private fun startTimer()
    {
        // TASK 1: SET TIME
        watchTime.setStartTime(SystemClock.uptimeMillis())

        // TASK 2: SET THE START TIME AND CALL THE CUSTOM HANDLER
        tHandler.postDelayed(updateTimerRunnable,  0)

    }

    // THREAD HANDLER FOR UPDATING TIMER
    private val updateTimerRunnable: Runnable = object: Runnable {
        override fun run() {

            // TASK 1: COMPUTE THE TIME DIFFERENCE
            watchTime.setTimeUpdate(SystemClock.elapsedRealtime())
            val time = (watchTime.getTimeUpdate() / 1000)

            // TASK 2: COMPUTE SECONDS
            val seconds = (time % 60)

            // TASK 3: DISPLAY THE TIME IN THE TIMERVIEW
            timeDisplay.text = String.format("%02d", seconds)

            // TASK 4: CHECK THE LEVEL TIME HITS
            // LEVEL TIME GOTO NEXT LEVEL
            // OR RESTART LEVEL
            // TODO: TIMER LEVEL OPTIONS
            var currentscore: Int = getScore()

            if(currentscore >= 2 && level == 0)
            {
                // start level 2
                level = 1
                nHandler.post(notificationRunnable)
            }
            if(currentscore >= 3 && level == 1)
            {
                // start level 3
                level = 2
                nHandler.post(notificationRunnable)
            }
            if(currentscore >= 30 && level == 2)
            {
                // show game won when reach 50
                nHandler.post(notificationRunnable)
            }
            tHandler.post(this)
        }

    }


    // THREAD HANDLER FOR UPDATING BALLOON GRIDVIEW
    private val displayBalloonsRunnable: Runnable = object: Runnable {
        override fun run() {

            // set level
            levelNumber.text = getLevel().toString()

            //TODO: fix bug with equations having same answer
            // TASK 1: Display balloons grid
            gridView = findViewById(R.id.gridview)
            db = DbContextHelperClass(applicationContext)
            val questions: ArrayList<QuestionClass>? = db.getQuestions(0, level.toString())
            val random = Random()
            val randomQuestion = random.nextInt(questions!!.count())
            question.text = questions!![randomQuestion].questionProblem
            answer = questions!![randomQuestion].questionAnswer.toInt()

            // gridview height
            val gVH = gridView.height
            val gVW = gridView.width
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
                            gHandler.postDelayed(this, 5)
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

            gridView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                Log.d("Action", "onLayoutChange(): ")

            }
        }
    }

    private val notificationRunnable: Runnable = object: Runnable {
        override fun run() {

            when(level) {
                0 -> {
                    Toast.makeText(applicationContext, "Well done starting level 2...", Toast.LENGTH_LONG )

                }
                1 -> {
                    Toast.makeText(applicationContext, "Well done starting level 2...", Toast.LENGTH_LONG )

                }
                3 -> {
                    Toast.makeText(applicationContext, "Well done starting level 2...", Toast.LENGTH_LONG )

                }
            }
        }

    }
}

