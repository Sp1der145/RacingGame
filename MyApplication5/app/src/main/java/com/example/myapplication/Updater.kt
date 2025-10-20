package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.random.Random

class Updater(context: Context, layout: FrameLayout, private val size: Point): Runnable {
    private var playing = true
    private var score = 0
    private var lives = 3
    private var lastObstacleSpawnTime = 0L
    private var startTime = System.currentTimeMillis()
    public var playerCar: PlayerCar? = null
    public var textView: TextView? = null


    init {
        textView = TextView(context)
        textView?.text = "Score: $score Lives: $lives"
        textView?.textSize = 20f;
        textView?.translationX = 100f;
        textView?.translationY = 100f;
        layout.addView(textView)

        playerCar = PlayerCar(context, layout)

    }

    override fun run() {
        var fps: Long

        while (playing) {
            /*
            val startFrameTime = System.currentTimeMillis()
            val timeThisFrame = startFrameTime - startTime

            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
            else {
                fps = 60
            }

            startTime = startFrameTime
            */


            update(60)
            Thread.sleep(16)
            //draw()
        }
    }

    private fun update(fps: Long) { // always 60
        // Increase score
        score += 1


        textView?.text = "Score: $score Lives: $lives"
        // Move player
        playerCar!!.update(fps)

        // Dynamic difficulty: increase obstacle speed over time
        val speedMultiplier = 1f + (score / 2000f)

        /*
        // Spawn obstacles periodically
        val now = System.currentTimeMillis()
        if (now - lastObstacleSpawnTime > 1500) {
            obstacles.add(Obstacle(context, size.x, size.y, speedMultiplier))
            lastObstacleSpawnTime = now
        }

        // Move obstacles and check collisions
        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obj = iterator.next()
            obj.update(fps)

            // Remove if off screen
            if (obj.position.top > size.y) {
                iterator.remove()
                continue
            }

            // Collision detection
            if (RectF.intersects(obj.position, playerCar.position)) {
                lives--
                iterator.remove()

                if (lives <= 0) {
                    paused = true
                    playing = false
                    showGameOverDialog()
                    break
                }
            }
        }
        */

    }
}