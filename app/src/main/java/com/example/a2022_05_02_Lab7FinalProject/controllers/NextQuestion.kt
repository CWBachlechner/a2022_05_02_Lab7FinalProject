package com.example.a2022_05_02_Lab7FinalProject.controllers

import com.example.a2022_05_02_Lab7FinalProject.models.questions.AllQuestions
import java.util.*


class NextQuestion {

    val allQuestions: AllQuestions = AllQuestions()

    var question: Int = 0
    val total_qs: Int = allQuestions.numberOfQuestions()

    fun isCorrect(rightOrWrong: Boolean) : Boolean {
        return rightOrWrong
    }
}