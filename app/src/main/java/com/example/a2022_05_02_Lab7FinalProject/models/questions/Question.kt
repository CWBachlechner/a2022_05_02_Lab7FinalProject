package com.example.a2022_05_02_Lab7FinalProject.models.questions


class Question  {
    lateinit var answers: AnswerList
    lateinit var question: String
    lateinit var image: String

    constructor (answers: AnswerList, question: String, image: String) {
        this.answers = answers
        this.question = question
        this.image = image

    }
    constructor() {}
}
