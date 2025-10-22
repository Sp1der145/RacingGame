package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.graphics.RectF
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.random.Random

class Updater(private val context: Context, private val layout: FrameLayout, private val size: Point, private val scoreTextView: TextView) {

    // --- Game stat variables
    private var playing = true
    private var score = 0f
    private var lives = 3
    private var lastObstacleSpawnTime = 0L
    private var speedMultiplier = 1f
    private var now = System.currentTimeMillis()

    // --- Game object variables
    private var playerCar: PlayerCar = PlayerCar(context, layout, size)
    private var obstacles = mutableListOf<Obstacle>()

    fun update() {
        if (!playing) return

        // --- Move player
        playerCar.update()

        // --- Move obstacles and check for collisions
        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obstacle = iterator.next()
            obstacle.update(speedMultiplier)

            // --- collisions
            if (RectF.intersects(obstacle.position, playerCar.getPosition())) {
                lives--
                obstacle.removeFromLayout()
                iterator.remove()

                if (lives <= 0) {
                    playing = false
                    showGameOver()
                    break
                }
            } else if (obstacle.isOffScreen(size.y)) {
                obstacle.removeFromLayout()
                iterator.remove()
            }
        }

        // --- Spawn obstacle every 3 seconds
        now = System.currentTimeMillis()
        if (now - lastObstacleSpawnTime > 3000) {
            obstacles.add(Obstacle(context, layout, size.x))
            lastObstacleSpawnTime = now
        }

        // --- Increase score and speed
        score += 0.016f
        speedMultiplier = 1f + (score / 1000f)

        // --- Update score text
        scoreTextView.post {
            scoreTextView.text = "Score: ${score.toInt()}  Lives: $lives"
        }
    }

    private fun showGameOver() {
        AlertDialog.Builder(context)
            .setTitle("Game Over")
            .setMessage("Your score: ${score.toInt()}")
            .setPositiveButton("Restart") { dialog, _ ->
                dialog.dismiss()
                restartGame()
            }
            .setCancelable(false)
            .show()
    }

    private fun restartGame() {
        lives = 3
        score = 0f
        playing = true
        speedMultiplier = 1f

        // --- Remove all obstacles
        for (obstacle in obstacles) {
            obstacle.removeFromLayout()
        }
        obstacles.clear()

        playerCar.resetPosition()
    }
}
