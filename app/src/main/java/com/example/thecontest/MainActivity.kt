package com.example.thecontest

import android.os.Bundle
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

// main app
class MainActivity : AppCompatActivity() {
    // late init variables allowing the data to be assigned later, but creating the reference now
    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // grab layout elements
//        scoreText = findViewById(R.id.score_text)


        val scoreBtn = findViewById<Button>(R.id.scoreBtn)
        val stealBtn = findViewById<Button>(R.id.steal)
        val resetBtn = findViewById<Button>(R.id.reset)

        // create sounds
        val clickSound = MediaPlayer.create(this, R.raw.beep_short)
        val winSound = MediaPlayer.create(this, R.raw.siren_whistle)

        // set our state from saved if it exists
        if (savedInstanceState != null) {
            currentScore = savedInstanceState.getInt("currentScore")
        }

        updateUI()

        // set up button listeners
        scoreBtn.setOnClickListener {
            // play sound
            clickSound.start()
            // log click
            Log.d("TriviaGame", "score btn clicked")
            // add score
//            currentScore = min(15, currentScore + 1)

            // if we have 15 points or more, win the game
            if (currentScore >= 15) {
                winSound.start()
                Log.d("TriviaGame", "15 points, you win!")
            } else {
                currentScore++
            }
            updateUI()
        }

        stealBtn.setOnClickListener {
            // play sound
            clickSound.start()
            // log click
            Log.d("TriviaGame", "steal btn clicked")
            // steal score
//            currentScore = max(0, currentScore - 1)
            if (currentScore > 0) {
                currentScore -= 1
            }
            updateUI()
        }

        resetBtn.setOnClickListener {
            // play sound
            clickSound.start()
            // log click
            Log.d("TriviaGame", "reset btn clicked")
            // reset score
            currentScore = 0
            updateUI()
        }
    }

    private fun updateUI() {
        val score = findViewById<TextView>(R.id.score)
        score.text = currentScore.toString()
    }

    // save state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentScore", currentScore)
    }

    // when returning to the main game screen, check if we have won. If we have, reset the game.
    override fun onResume() {
        super.onResume()
        // Check if the user has finished the game
        if (currentScore >= 15) {
            currentScore = 0
            updateUI()
        }
    }
}