package com.mathkids.sligamer.mathkids

import java.util.ArrayList


/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Mania
 * Question class for getting questions using an object QuestionClass
 * Plugin Support with kotlin_version = '1.2.40'
 */
class QuestionClass {

    // DECLARE VARIABLES
    var questionID: Int = 0
    var questionOperator = ""
    var questionCoefficientOne = ""
    var questionCoefficientTwo = ""
    var questionProblem:String = ""
    var questionAnswer:String = ""
    var questionLevel:String = ""
    var ballonImageId:Int = 0

    var wrongAnswers: ArrayList<String>? = null

    private lateinit var questionClass: QuestionClass

    constructor()

    constructor(id: Int, coefficientOne: String, operator: String,coefficientTwo: String, answer: String, level: String, image:Int)
    {
        questionID = id
        questionCoefficientOne = coefficientOne
        questionCoefficientTwo = coefficientTwo
        questionOperator = operator
        questionProblem = "$coefficientOne $operator $coefficientTwo"
        questionAnswer = answer
        questionLevel = level
        ballonImageId = image
    }

    fun getQuestion(q: QuestionClass): QuestionClass {
        return q
    }

    fun setQuestion(q: QuestionClass)
    {
        questionClass = q
    }
}