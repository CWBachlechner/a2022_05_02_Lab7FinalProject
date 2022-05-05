package com.example.a2022_05_02_Lab7FinalProject.models.score

class Score {

    private var score: Int = 0

    fun inc(): Int {
        score += 1
        return score
    }

    fun dec(): Int {
        score -= 1
        return score
    }

    fun getScore(): Int {
        return score
    }

}