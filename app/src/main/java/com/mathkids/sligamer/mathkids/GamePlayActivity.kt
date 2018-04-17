package com.mathkids.sligamer.mathkids


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.lang.Integer.parseInt
import android.os.Handler
import android.os.SystemClock
import android.widget.*
import android.widget.AdapterView.OnItemClickListener

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
    private var timeInMilliseconds: Long = 0L

    // WATCH TIME CLASS
    private lateinit var watchTime: GameWatch

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
                questionID = getQuestion(passedLevel.toString())
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
        startTimer()
    }


    private fun getQuestion(level: String) : Int {
        // create the database for storing questions
        db = DbContextHelperClass(this)
        var q = db.getQuestion(0,level)
        question.text = q!!.questionProblem
        return q.questionID
    }

    private fun getQuestion(id: Int, level: String) : QuestionClass {
        // create the database for storing questions
        db = DbContextHelperClass(this)
        var q = db.getQuestion(id,level)
        return q!!
    }

    // METHOD FOR SETTINGS DEFAULTS
    private fun setupGameDefaults() {
        // set score to 0
        scoreTxt.text = "0"
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
        mHandler.postDelayed(displayBalloonsRunnable,  10)
    }

    // METHOD START THE TIMER
    private fun startTimer()
    {
        // TASK 2: SET THE START TIME AND CALL THE CUSTOM HANDLER
        watchTime.setStartTime(SystemClock.uptimeMillis())
        mHandler.postDelayed(updateTimerRunnable,  20)

    }

    private fun stopTimer()
    {
        // TASK 1: DISABLE THE START BUTTON
        // AND ENABLE STOP BUTTON

        // TASK 2: UPDATE THE STORED TIME VALUE
        watchTime.addStoredTime(timeInMilliseconds)

        // TASK 3: HANDLER CLEARS THE MESSAGE QUEUE
        mHandler.removeCallbacks(updateTimerRunnable)
    }

    private fun resetTimer(){
        // TASK 1: RESET THE WATCHTIME
        watchTime.resetWatchTime()

        // TASK 2: RESET VARIABLES TO 0
        timeInMilliseconds = 0L
        var minutes = 0
        var seconds= 0
        var milliseconds = 0

        // TASK 3: DISPLAY THE TIME IN THE TIMERVIEW
        timeDisplay.text = String.format("%02d", minutes) + ":" +
                String.format("%02d", seconds) + ":" +
                String.format("%02d", milliseconds)
    }

    // THREAD HANDLER FOR UPDATING TIMER
    private val updateTimerRunnable: Runnable = object: Runnable {
        override fun run() {
            // TASK 1: COMPUTE THE TIME DIFFERENCE
            timeInMilliseconds = (SystemClock.uptimeMillis() - watchTime.getStartTime())
            watchTime.setTimeUpdate(watchTime.getStoredTime() + timeInMilliseconds)
            var time = (watchTime.getTimeUpdate() / 1000)

            // TASK 2: COMPUTE MINUTES, SECONDS, AND MILLISECONDS
            var minutes = (time / 60)
            var seconds = (time % 60)
            var milliseconds = (watchTime.getTimeUpdate() % 1000)

            // TASK 3: DISPLAY THE TIME IN THE TIMERVIEW
            timeDisplay.text = String.format("%02d", minutes) + ":" +
                    String.format("%02d", seconds) + ":" +
                    String.format("%02d", milliseconds)

            // TASK 4: SPECIFY NO TIME LAPSE BETWEEN POSTING
            mHandler.postDelayed(this, 5)

            // TASK 4: CHECK THE LEVEL TIME HITS
            // LEVEL TIME GOTO NEXT LEVEL
            // OR RESTART LEVEL
            // TODO: TIMER LEVEL OPTIONS
        }
    }


    // THREAD HANDLER FOR UPDATING BALLOON GRIDVIEW
    private val displayBalloonsRunnable: Runnable = Runnable {

        // TASK 1: Display balloons grid
        gridView = findViewById(R.id.gridview)
        // GET QUESTIONs        var db = DbContextHelperClass(this)

        var questions: ArrayList<QuestionClass>? = db.getQuestions(0, level.toString())

        var madapter = ImageAdapter(applicationContext, questions)
        gridView.adapter = madapter

        // TASK 2: Implement On Item click listener
        gridView.onItemClickListener = OnItemClickListener { parent, view, position, id ->

            // TODO: add a fragment or intent here to execute this logic in thread handler
            view.tag = "$position" // get the question
           //view.tag = getQuestion(questionID, level.toString()).questionAnswer

            Toast.makeText(applicationContext, view.tag as String, Toast.LENGTH_SHORT).show()

            // tag will be assigned to all the balloon images this will be the answer
            val enteredNum = parseInt(view!!.tag.toString())
            answer = getQuestion(questionID, level.toString()).questionAnswer.toInt()
            if (answerTxt.text.toString().endsWith("?")) {
                answerTxt.text = """= $enteredNum"""
                val answerContent = answerTxt.text.toString()
                if (!answerContent.endsWith("?")) {
                    val enteredAnswer = Integer.parseInt(answerContent.substring(2))
                    val exScore = getScore()
                    if (enteredAnswer == answer) {
                        //correct
                        scoreTxt.text = """Score: ${exScore + 1}"""
                        //TODO: Touch event gesture class to do animations

                        // next question
                        var db = DbContextHelperClass(applicationContext)
                        db.getQuestion(0 , level.toString())

                        // restart thread
                        displayBalloons()
                    } else {
                        //incorrect
                        answerTxt.text = "= ?"
                        var currentImage = madapter.getView( position, view, parent) as ImageView
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

