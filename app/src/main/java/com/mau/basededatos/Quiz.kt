package com.mau.basededatos

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil

class Quiz : AppCompatActivity() {

    private lateinit var optionA: TextView
    private lateinit var optionB: TextView
    private lateinit var optionC: TextView
    private lateinit var optionD: TextView
    private lateinit var questionNumber: TextView
    private lateinit var question: TextView
    private lateinit var score: TextView
    private lateinit var checkout1: TextView
    private lateinit var checkout2: TextView
    private var currentIndex = 0
    private var mscore = 0
    private var qn = 1
    private lateinit var progressBar: ProgressBar
    private var currentQuestion = 0
    private var currentOptionA = 0
    private var currentOptionB = 0
    private var currentOptionC = 0
    private var currentOptionD = 0

    val questionBank = arrayOf(
        Answerclass(R.string.question_1, R.string.question1_A, R.string.question1_B, R.string.question1_C, R.string.question1_D, R.string.answer_1),
        Answerclass(R.string.question_2, R.string.question_2A, R.string.question_2B, R.string.question_2C, R.string.question_2D, R.string.answer_2),
        Answerclass(R.string.question_3, R.string.question_3A, R.string.question_3B, R.string.question_3C, R.string.question_3D, R.string.answer_3),
        Answerclass(R.string.question_4, R.string.question_4A, R.string.question_4B, R.string.question_4C, R.string.question_4D, R.string.answer_4),
        Answerclass(R.string.question_5, R.string.question_5A, R.string.question_5B, R.string.question_5C, R.string.question_5D, R.string.answer_5),
        Answerclass(R.string.question_6, R.string.question_6A, R.string.question_6B, R.string.question_6C, R.string.question_6D, R.string.answer_6),
        Answerclass(R.string.question_7, R.string.question_7A, R.string.question_7B, R.string.question_7C, R.string.question_7D, R.string.answer_7),
        Answerclass(R.string.question_8, R.string.question_8A, R.string.question_8B, R.string.question_8C, R.string.question_8D, R.string.answer_8)
    )

    private val PROGRESS_BAR = ceil(100.toDouble() / questionBank.size).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)

        question = findViewById(R.id.question)
        score = findViewById(R.id.score)
        questionNumber = findViewById(R.id.QuestionNumber)

        checkout1 = findViewById(R.id.selectoption)
        checkout2 = findViewById(R.id.CorrectAnswer)
        progressBar = findViewById(R.id.progress_bar)

        currentQuestion = questionBank[currentIndex].getQuestionid()
        question.text = getString(currentQuestion)
        currentOptionA = questionBank[currentIndex].getOptionA()
        optionA.text = getString(currentOptionA)
        currentOptionB = questionBank[currentIndex].getOptionB()
        optionB.text = getString(currentOptionB)
        currentOptionC = questionBank[currentIndex].getOptionC()
        optionC.text = getString(currentOptionC)
        currentOptionD = questionBank[currentIndex].getOptionD()
        optionD.text = getString(currentOptionD)
        optionA.setOnClickListener {
            checkAnswer(currentOptionA)
            updateQuestion()
        }

        optionB.setOnClickListener {
            checkAnswer(currentOptionB)
            updateQuestion()
        }

        optionC.setOnClickListener {
            checkAnswer(currentOptionC)
            updateQuestion()
        }

        optionD.setOnClickListener {
            checkAnswer(currentOptionD)
            updateQuestion()
        }
    }

    private fun checkAnswer(userSelection: Int) {
        val correctAnswer = getString(questionBank[currentIndex].getAnswerid())

        checkout1.text = getString(userSelection)
        checkout2.text = correctAnswer

        val m = checkout1.text.toString().trim()
        val n = checkout2.text.toString().trim()

        if (m == n) {
            Toast.makeText(applicationContext, "Correcta", Toast.LENGTH_SHORT).show()
            mscore++
        } else {
            Toast.makeText(applicationContext, "Incorrecta", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size

        if (currentIndex == 0) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Game Over")
            alert.setCancelable(false)
            alert.setMessage("Tu puntuación: $mscore puntos")
            alert.setPositiveButton("Cerrar Aplicación") { _, _ -> finish() }
            alert.setNegativeButton("No") { _, _ ->
                mscore = 0
                qn = 1
                progressBar.progress = 0
                score.text = "Puntuación: $mscore/${questionBank.size}"
                questionNumber.text = "Pregunta $qn/${questionBank.size}"
            }
            alert.show()
        }

        currentQuestion = questionBank[currentIndex].getQuestionid()
        question.text = getString(currentQuestion)
        currentOptionA = questionBank[currentIndex].getOptionA()
        optionA.text = getString(currentOptionA)
        currentOptionB = questionBank[currentIndex].getOptionB()
        optionB.text = getString(currentOptionB)
        currentOptionC = questionBank[currentIndex].getOptionC()
        optionC.text = getString(currentOptionC)
        currentOptionD = questionBank[currentIndex].getOptionD()
        optionD.text = getString(currentOptionD)

        qn++
        if (qn <= questionBank.size) {
            questionNumber.text = "Pregunta $qn/${questionBank.size}"
        }
        score.text = "Puntuación: $mscore/${questionBank.size}"
        progressBar.incrementProgressBy(PROGRESS_BAR)
    }

    inner class Answerclass(
        private val questionid: Int,
        private val optionA: Int,
        private val optionB: Int,
        private val optionC: Int,
        private val optionD: Int,
        private val answerid: Int
    ) {
        fun getQuestionid(): Int {
            return questionid
        }

        fun getOptionA(): Int {
            return optionA
        }

        fun getOptionB(): Int {
            return optionB
        }

        fun getOptionC(): Int {
            return optionC
        }

        fun getOptionD(): Int {
            return optionD
        }

        fun getAnswerid(): Int {
            return answerid
        }
    }
}