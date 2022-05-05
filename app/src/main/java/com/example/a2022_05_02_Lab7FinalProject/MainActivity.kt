package com.example.a2022_05_02_Lab7FinalProject

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.a2022_05_02_Lab7FinalProject.controllers.NextQuestion
import com.example.a2022_05_02_Lab7FinalProject.models.questions.AllQuestions
import com.example.a2022_05_02_Lab7FinalProject.models.questions.AnswerList
import com.example.a2022_05_02_Lab7FinalProject.models.questions.AnswerObject
import com.example.a2022_05_02_Lab7FinalProject.models.questions.Question
import com.example.a2022_05_02_Lab7FinalProject.models.score.Score
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var getDataButton: Button
    private lateinit var basicQuestionView: TextView
    private lateinit var scoreView: TextView
    private lateinit var imageView: ImageView

    private lateinit var answerbutton1: Button
    private lateinit var answerbutton2: Button
    private lateinit var answerbutton3: Button
    private lateinit var answerbutton4: Button

    private lateinit var playAgainButton: Button

    lateinit var jsonObjects: JSONObject

    val scoreKeep: Score = Score()

    var pos = 0

    var ans1 = false
    var ans2 = false
    var ans3 = false
    var ans4 = false

    var butNum1 = 0
    var butNum2 = 1
    var butNum3 = 2
    var butNum4 = 3

    var correct = mutableListOf(ans1, ans2, ans3, ans4)

    var totalCorrect = 0

    private var nextQuestion: NextQuestion = NextQuestion()
    var allQuestions = AllQuestions()

    var numQues = 0

    val urlJSON = "http://192.168.1.162:8080/questions";
    val urlIMAGE = "http://192.168.1.162:8080/static/";

    fun getImage(url: String) {
        val queue = Volley.newRequestQueue(this)
        val imageRequest = ImageRequest(
            url,
            { response: Bitmap ->
                // Display the first 500 characters of the response string.
                imageView.setImageBitmap(response)
            },
            0, 0,
            ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,

            { error -> basicQuestionView.text = "Error: ${error}" })

        queue.add(imageRequest)
    }

    fun updateButtons(){
        answerbutton1.text = nextQuestion.allQuestions.allQuestions[pos].answers.getAnswer(0).answerString
        answerbutton2.text = nextQuestion.allQuestions.allQuestions[pos].answers.getAnswer(1).answerString
        answerbutton3.text = nextQuestion.allQuestions.allQuestions[pos].answers.getAnswer(2).answerString
        answerbutton4.text = nextQuestion.allQuestions.allQuestions[pos].answers.getAnswer(3).answerString

        for (i in 0 until nextQuestion.allQuestions.allQuestions[pos].answers.numberOfAnswers()) {
            correct[i] = nextQuestion.allQuestions.allQuestions[pos].answers.getAnswer(i).isTrue
        }
    }

    fun buttonPress(butNum : Int){
        if (pos == numQues-1) {
            if (nextQuestion.isCorrect(correct[butNum])) {
                scoreKeep.inc()
                totalCorrect += 1
                scoreView.setText("Score: ${scoreKeep.getScore()}")
                Toast.makeText(baseContext, "Correct + 1 Points!", Toast.LENGTH_SHORT).show()
            } else {
                scoreKeep.dec()
                scoreView.setText("Score: ${scoreKeep.getScore()}")
                Toast.makeText(baseContext, "Incorrect - 1 Points!", Toast.LENGTH_SHORT).show()
            }
            basicQuestionView.setText("Good Job You Made It To The End! You Got ${totalCorrect}/${numQues} Right!")
            playAgainButton.isVisible = true
            playAgainButton.setOnClickListener {
                pos = 0
                totalCorrect = 0
                Toast.makeText(baseContext, "Keep racking up that score!", Toast.LENGTH_SHORT).show()
                playAgainButton.isVisible = false
                updateButtons()
                basicQuestionView.text = nextQuestion.allQuestions.allQuestions[pos].question
                getImage("$urlIMAGE${allQuestions.allQuestions[pos].image}")
            }
        } else {
            pos += 1
            if (nextQuestion.isCorrect(correct[butNum])) {
                scoreKeep.inc()
                totalCorrect += 1
                scoreView.setText("Score: ${scoreKeep.getScore()}")
                Toast.makeText(baseContext, "Correct + 1 Points!", Toast.LENGTH_SHORT).show()
            } else {
                scoreKeep.dec()
                scoreView.setText("Score: ${scoreKeep.getScore()}")
                Toast.makeText(baseContext, "Incorrect - 1 Points!", Toast.LENGTH_SHORT).show()
            }
            basicQuestionView.setText(nextQuestion.allQuestions.allQuestions[pos].question)
            updateButtons()
            getImage("$urlIMAGE${allQuestions.allQuestions[pos].image}")
        }
    }

    fun retrieveJSON() {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            urlJSON,
            null,
            { response ->
                // Display the first 500 characters of the response string.
                jsonObjects = JSONObject(response.toString())
                var jsonarray: JSONArray = jsonObjects.getJSONArray("questions")
                var sizeArray: Int = jsonarray.length()
                for (i in 0 until sizeArray) {
                    var question: Question = Question()
                    var json_question: JSONObject = jsonarray.getJSONObject(i)
                    question.question = json_question.getString("question")
                    var answerString = json_question.getString("answers")
                    var answerList = AnswerList()
                    val json_answers: JSONArray = JSONArray(answerString)
                    var ansSize = json_answers.length()
                    for (j in 0 until ansSize) {
                        var ans: AnswerObject = AnswerObject()
                        var json_answer = json_answers.getJSONObject(j)
                        ans.answerString = json_answer.getString("answer")
                        ans.isTrue = json_answer.getBoolean("TF")
                        answerList.addAnswer(ans)
                    }
                    question.answers = answerList
                    question.image = json_question.getString("image")
                    allQuestions.addQuestion(question)
                    nextQuestion.allQuestions.addQuestion(question)
                    numQues = allQuestions.numberOfQuestions()
                }
                getImage("$urlIMAGE${allQuestions.allQuestions[pos].image}")
                basicQuestionView.text = nextQuestion.allQuestions.allQuestions[pos].question
                updateButtons()

                answerbutton1.setOnClickListener {
                    buttonPress(butNum1)
                }

                answerbutton2.setOnClickListener {
                    buttonPress(butNum2)
                }

                answerbutton3.setOnClickListener {
                    buttonPress(butNum3)
                }

                answerbutton4.setOnClickListener {
                    buttonPress(butNum4)
                }
            },
            { error ->  basicQuestionView.text = "Error: ${error}" })

        queue.add(jsonObjectRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDataButton = findViewById(R.id.get_data_button)
        basicQuestionView = findViewById(R.id.basic_question_view)
        imageView = findViewById(R.id.imageView)

        answerbutton1 = findViewById(R.id.button_answer1)
        answerbutton2 = findViewById(R.id.button_answer2)
        answerbutton3 = findViewById(R.id.button_answer3)
        answerbutton4 = findViewById(R.id.button_answer4)

        playAgainButton = findViewById(R.id.playAgainButton)

        answerbutton1.isVisible = false
        answerbutton2.isVisible = false
        answerbutton3.isVisible = false
        answerbutton4.isVisible = false

        playAgainButton.isVisible = false

        scoreView = findViewById((R.id.scoreView))
        scoreView?.text = "Score: ${scoreKeep.getScore()}"

        getDataButton.setOnClickListener {
            answerbutton1.isVisible = true
            answerbutton2.isVisible = true
            answerbutton3.isVisible = true
            answerbutton4.isVisible = true

            getDataButton.isVisible = false

            retrieveJSON()
        }
    }
}
