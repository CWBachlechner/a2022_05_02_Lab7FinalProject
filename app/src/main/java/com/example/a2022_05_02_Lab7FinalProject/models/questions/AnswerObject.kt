package com.example.a2022_05_02_Lab7FinalProject.models.questions

import kotlin.properties.Delegates

class AnswerObject() {

    constructor(answerString: String, isTrue: Boolean) : this() {
        this.answerString = answerString
        this.isTrue = isTrue
    }

    lateinit var answerString: String
    var isTrue by Delegates.notNull<Boolean>()


}