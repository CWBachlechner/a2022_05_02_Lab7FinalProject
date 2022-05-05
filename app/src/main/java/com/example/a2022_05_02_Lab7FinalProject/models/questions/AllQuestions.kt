package com.example.a2022_05_02_Lab7FinalProject.models.questions

class AllQuestions {
    var allQuestions = mutableListOf<Question>()

    fun addQuestion(question: Question) {
        allQuestions.add(question)
    }

    fun numberOfQuestions() : Int {
        return allQuestions.size
    }


}