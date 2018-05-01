package com.mathkids.sligamer.mathkids

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Select Level Screen
 * Plugin Support with kotlin_version = '1.2.40'
 */
class SelectLevelActivity: AppCompatActivity() {

    // DECLARE VARIABLES
    private val levelNames = arrayOf("Easy", "Medium", "Hard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 1: CREATE LAYOUT AND UI ELEMENTS
        setContentView(R.layout.instructions_layout)

        // TASK 2: BUILD ALERT DIALOG TO DISPLAY AVAILABLE OPTIONS
        // TODO: may turn this into a fragment?

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a level")
                .setSingleChoiceItems(levelNames, 0) { dialog, which ->
                    dialog.dismiss()
                    startPlay(which)
                }

        // TASK 3: SHOW THE DIALOG
        val ad = builder.create()
        ad.show()
    }

    // METHOD TO START GAME
     private fun startPlay(chosenLevel: Int) {
        val playIntent = Intent(this, GamePlayActivity().javaClass)
        playIntent.putExtra("level", chosenLevel)
        this.startActivity(playIntent)
    }
}