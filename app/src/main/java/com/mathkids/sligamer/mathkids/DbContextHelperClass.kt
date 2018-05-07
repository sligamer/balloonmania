package com.mathkids.sligamer.mathkids

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Math Mania
 * SQLLiteOpenHelper class to create a database of
 * random math problems
 * Plugin Support with kotlin_version = '1.2.41'
 */
class DbContextHelperClass: SQLiteOpenHelper {

    // TASK 1: DEFINE THE DATABASE AND TABLE
    // TASK 2: DEFINE THE COLUMNS IN THE TABLE
    companion object {

        // DB AND TABLE
        const val DATABASE_VERSION: Int = 14
        const val DATABASE_NAME: String = "math_DB"
        const val DATABASE_TABLE: String = "math_Questions"

        // COLUMNS
        const val KEY_QUESTION_ID: String = "_id"
        const val KEY_QUESTION_OPERATOR: String = "operator"
        const val KEY_QUESTION_COEFFICIENT_ONE: String = "coefficientone"
        const val KEY_QUESTION_COEFFICIENT_TW0: String = "coefficienttwo"
        const val KEY_QUESTION_ANSWER: String = "answer"
        const val KEY_QUESTION_LEVEL: String = "level"

    }

    // DECLARE RANDOM QUESTION GENERATOR
    private var answer = 0
    private var operator = 0
    private var operand1 = 0
    private var operand2 = 0
    private val ADD_OPERATOR = 0
    private val SUBTRACT_OPERATOR = 1
    private val MULTIPLY_OPERATOR = 2
    private val DIVIDE_OPERATOR = 3 //TODO: fix bug with divide operator
    private val levelNames = arrayOf("Easy", "Medium", "Hard")
    private val operators = arrayOf("+", "-", "x") //TODO: fix , "/"
    private val easyLevel = arrayOf(intArrayOf(1, 11, 21), intArrayOf(1, 5, 10), intArrayOf(2, 5, 10), intArrayOf(2, 3, 5))
    private val hardLevel = arrayOf(intArrayOf(10, 25, 50), intArrayOf(10, 20, 30), intArrayOf(5, 10, 15), intArrayOf(10, 50, 100))
    private var questionCount: Int = 0
    private var random: Random? = null

    constructor(context: Context) : super(context, DATABASE_NAME, null, DATABASE_VERSION) {
        this.writableDatabase
    }

    // CREATE DATABASE TABLE
    override fun onCreate(db: SQLiteDatabase?) {

        var sqlInsertStatement = "CREATE TABLE " + DATABASE_TABLE + "(" +
                KEY_QUESTION_ID + " integer primary key autoincrement not null, " +
                KEY_QUESTION_COEFFICIENT_ONE + " text not null, " +
                KEY_QUESTION_OPERATOR + " text not null, " +
                KEY_QUESTION_COEFFICIENT_TW0 + " text not null, " +
                KEY_QUESTION_ANSWER + " text not null, " +
                KEY_QUESTION_LEVEL + " text not null" + ")"

        db!!.execSQL(sqlInsertStatement)

        // CREATE AN INDEX TO INCREASE PERFORMANCE
        db!!.execSQL("create unique index math_Questions__id_uindex\n" +
                "  on math_Questions (\"_id\");")

        questionCount = 0

        val questions = ArrayList<QuestionClass>()

        for( level in levelNames) {
            for (i in 0..100) {
                val passedLevel = levelNames.indexOf(level)
                val q = questionGenerator(passedLevel)
                questions.add(q)
            }
        }

        for (q in questions) {

            val initialValues = ContentValues()
            initialValues.put("coefficientone", q.questionCoefficientOne)
            initialValues.put("operator", q.questionOperator)
            initialValues.put("coefficienttwo", q.questionCoefficientTwo)
            initialValues.put("answer", q.questionAnswer)
            initialValues.put("level", q.questionLevel)

            db.insert(DATABASE_TABLE, null, initialValues)

            Log.d("questionInsert", "question inserted: " + q.questionProblem)
        }
    }

    // IF DATABASE DOES NOT EXIST CREATE IT AND SEED THE DATA
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        var sqlInsertStatement = "DROP TABLE IF EXISTS $DATABASE_TABLE"

        db!!.execSQL(sqlInsertStatement)

        onCreate(db)
    }

    // METHOD FOR RANDOMLY CREATING EQUATIONS
    private fun questionGenerator(level: Int): QuestionClass {

        val q = QuestionClass()
        random = Random()

        operator = random!!.nextInt(operators.count())
        operand1 = getOperand(level)
        operand2 = getOperand(level)

        when (operator) {
            SUBTRACT_OPERATOR -> while (operand2 > operand1) {
                operand1 = getOperand(level)
                operand2 = getOperand(level)
            }
            // TODO: fix the divide by zero errors
           /* DIVIDE_OPERATOR -> while (((operand1 / operand2) % 1 > 0)) {
                operand1 = getOperand(level)
                operand2 = getOperand(level)
            }*/
        }

        when (operator) {
            ADD_OPERATOR -> {
                answer = operand1 + operand2
            }

            SUBTRACT_OPERATOR -> {
                answer = operand1 - operand2
            }

            MULTIPLY_OPERATOR -> {
                answer = operand1 * operand2
            }
            // TODO: fix the divide by zero errors
            // TODO: expand divide answers to 3 decimal rounded
          /*  DIVIDE_OPERATOR -> {
                answer = operand1 / operand2
            }*/

        }

        // TASK: FILL QUESTION
        q.questionOperator = operators[operator]
        q.questionCoefficientOne = operand1.toString()
        q.questionCoefficientTwo = operand2.toString()
        q.questionAnswer = answer.toString()
        q.questionLevel = level.toString()
        q.questionProblem = operand1.toString() + " " + operators[operator] + " " + operand2.toString()

        // RETURN A QUESTION
        return q
    }

    // METHOD FOR RANDOMLY GETTING COEFFICIENT OPERATORS
    private fun getOperand(level: Int): Int {
        return random!!.nextInt(
                hardLevel[operator][level] - easyLevel[operator][level] + 1)+easyLevel[operator][level]
    }

    fun getQuestion(id: Int, level: String) : QuestionClass?
    {
        var db =  this.readableDatabase
        var questionClass: QuestionClass? = null

        var cursor: Cursor? = null

        cursor = when {
            id > 0 -> db.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE _id = ?",
                    arrayOf( "$id"))
            else -> {
                db.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE level = ? ORDER BY RANDOM() LIMIT 1",
                        arrayOf("$level"))
            }
        }

        if(cursor.moveToFirst()) {

            questionClass = QuestionClass(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("coefficientone")),
                    cursor.getString(cursor.getColumnIndex("operator")),
                    cursor.getString(cursor.getColumnIndex("coefficienttwo")),
                    cursor.getString(cursor.getColumnIndex("answer")),
                    cursor.getString(cursor.getColumnIndex("level")),
                    R.mipmap.balloon
            )
            cursor.close()
        }
        db.close()

        return questionClass
    }

    fun getQuestions(id: Int, level: String) : ArrayList<QuestionClass>?
    {
        var db =  this.readableDatabase
        var questionClassList = ArrayList<QuestionClass>()
        var maxQuestionCount = 0
        when(level) {
            "0" -> {
                maxQuestionCount = 10
            }
            "1" -> {
                maxQuestionCount = 13
            }
            "2" -> {
                maxQuestionCount = 19
            }
        }

        var cursor: Cursor?

        cursor = when {
            id > 0 -> db.rawQuery("SELECT DISTINCT * FROM $DATABASE_TABLE WHERE _id = ?",
                    arrayOf( "$id"))
            else -> {
                db.rawQuery("SELECT DISTINCT * FROM $DATABASE_TABLE WHERE level = ? ORDER BY RANDOM() LIMIT ?",
                        arrayOf("$level", "$maxQuestionCount"))
            }
        }

        if(cursor.moveToFirst()) {
            while (cursor.moveToNext())
            questionClassList!!.add(QuestionClass(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("coefficientone")),
                    cursor.getString(cursor.getColumnIndex("operator")),
                    cursor.getString(cursor.getColumnIndex("coefficienttwo")),
                    cursor.getString(cursor.getColumnIndex("answer")),
                    cursor.getString(cursor.getColumnIndex("level")),
                    R.mipmap.balloon
            ))
        }
        cursor.close()
        db.close()

        questionClassList.shuffle()
        return questionClassList
    }

}