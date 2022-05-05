package com.example.a2022_05_02_Lab7FinalProject.models.questions

class AnswerList() {
     private var answerList = mutableListOf<AnswerObject>()

    constructor(answerList: List<AnswerObject>) : this() {
        this.answerList = answerList.toMutableList()
    }

    fun numberOfAnswers() : Int {
        return answerList.size
    }

    fun addAnswer(answerObject: AnswerObject) {
        answerList.add(answerObject)
    }

    fun getAnswer(pos: Int) : AnswerObject {
        return answerList[pos]
    }
}