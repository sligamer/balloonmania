package com.mathkids.sligamer.mathkids

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Main Activity Start Screen
 * Responsible for seeding database and sending to instructions activity
 * Plugin Support with kotlin_version = '1.2.31'
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var startGameBtn: Button
    private lateinit var db: DbContextHelperClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 1: SET LAYOUTS AND UI ELEMENTS
        setContentView(R.layout.activity_main) //TODO: add main drawables for this main activity splash screen

        startGameBtn = findViewById(R.id.startGameBtn)
        startGameBtn.setOnClickListener(this)

        // TASK 2: SETUP THE QUESTIONS DATABASE
        //TODO: do we add settings to check if database of questions does not exist.
        // Allow for a settings screen to create db and new randomized questions

        db = DbContextHelperClass(this)
        db.writableDatabase
}

    // ONCLICK EVENT IF CLICK SEND INTENT TO LEVEL SELECTION MENU
    override fun onClick(v: View?) {
        //start instructions activity
        val playIntent = Intent(this, SelectLevelActivity().javaClass)
        this.startActivity(playIntent)
    }
}
