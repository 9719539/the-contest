package com.example.thecontest

import android.os.Bundle
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// screen using constraint layout to display final score
class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_layout)

        val finalScore = intent.getIntExtra("score", 0)
        val scoreText: TextView = findViewById(R.id.score)
        scoreText.text = finalScore.toString()
    }
}

// data class to hold questions
data class Question(val text: String, val answers: List<String>, val correctAnswer: Int)

// main app
class MainActivity : AppCompatActivity() {
    // lateinit variables allowing the data to be assigned later, but creating the reference now
    private var currentScore = 0
    private lateinit var questionText: TextView
    private lateinit var scoreText: TextView
    private lateinit var score: TextView
    private lateinit var answerButtons: List<Button>
    private lateinit var clickSound: MediaPlayer
    private lateinit var winSound: MediaPlayer
    private lateinit var questions: List<Question>
    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the trivia questions from strings file
        questions = listOf(
            Question(
                getString(R.string.question1),
                listOf(
                    getString(R.string.q1answer1),
                    getString(R.string.q1answer2),
                    getString(R.string.q1answer3)
                ),
                0
            ),
            Question(
                getString(R.string.question2),
                listOf(
                    getString(R.string.q2answer1),
                    getString(R.string.q2answer2),
                    getString(R.string.q2answer3)
                ),
                1
            ),
            Question(
                getString(R.string.question3),
                listOf(
                    getString(R.string.q3answer1),
                    getString(R.string.q3answer2),
                    getString(R.string.q3answer3)
                ),
                0
            )
        )

        // grab layout elements
        questionText = findViewById(R.id.question_text)
        scoreText = findViewById(R.id.score_text)
        score = findViewById(R.id.score)
        answerButtons = listOf(
            findViewById(R.id.answer1),
            findViewById(R.id.answer2),
            findViewById(R.id.answer3)
        )

        // create sounds
        clickSound = MediaPlayer.create(this, R.raw.beep_short)
        winSound = MediaPlayer.create(this, R.raw.siren_whistle)

        // set our state from saved if it exists
        if (savedInstanceState != null) {
            currentScore = savedInstanceState.getInt("currentScore")
            currentQuestion = savedInstanceState.getInt("currentQuestion")
        }

        updateUI()

        // for each of our buttons, create onClick listeners
        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // when clicked, play sound and log that it was pressed
                clickSound.start()
                Log.d("TriviaGame", "Button $index clicked")
                // if the answer is correct, give a point and log it
                if (index == questions[currentQuestion].correctAnswer) {
                    currentScore++
                    Log.d("TriviaGame", "Correct answer! Score: $currentScore")
                }
                currentQuestion++

                // if we have 5 points or more, win the game
                if (currentScore >= 5) {
                    winSound.start()
                    Log.d("TriviaGame", "5 points, you win!")
                    // create intent to transition to new screen
                    val intent =
                        Intent(this, ScoreActivity::class.java).putExtra("score", currentScore)
                    startActivity(intent)
                } else {
                    updateUI()
                }
            }
        }
    }

    private fun updateUI() {
        // loop back to first question if we're at the end of question lengths
        if (currentQuestion >= questions.size) {
            currentQuestion = 0
        }
        val q = questions[currentQuestion]
        // update UI elements
        questionText.text = q.text
        scoreText.text = getString(R.string.scoreText)
        score.text = currentScore.toString()
        answerButtons.forEachIndexed { i, btn -> btn.text = q.answers[i] }
    }

    // save state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentScore", currentScore)
        outState.putInt("currentQuestion", currentQuestion)
    }

    // when returning to the main game screen, check if we have won. If we have, reset the game.
    override fun onResume() {
        super.onResume()
        // Check if the user has finished the game
        if (currentScore >= 5 || currentQuestion >= questions.size) {
            currentScore = 0
            currentQuestion = 0
            updateUI()
        }
    }
}