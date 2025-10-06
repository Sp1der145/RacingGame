package com.example.racinggameinitialcode

//NOTE FOR FUTURE CONNER HERE IS THE NEXT PART ON THE WEBSITE
//"Next, we can add the following code to the update function."

import android.content.Context
//import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent

class KotlinDrivingView(context: Context,private val size: Point): SurfaceView(context),
    Runnable {

    // This is our thread
    private val gameThread = Thread(this)

    // A boolean which we will set and unset
    private var playing = false

    // Game is paused at the start
    private var paused = true

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // The players ship
    private var playerCar: PlayerCar = PlayerCar(context, size.x, size.y)

    // Some Obstacles
    private val obstacles = ArrayList<Obstacle>()
    private var numObstacles = 0

    // The score
    private var score = 0

    // Lives
    private var lives = 3

    private var highScore =  0

    // How menacing should the sound be?
    private var menaceInterval: Long = 1000

    //                      SOUND RELATED SET UP IF WE WANT
    // Which menace sound should play next
    //private var uhOrOh: Boolean = false
    // When did we last play a menacing sound
    //private var lastMenaceTime = System.currentTimeMillis()

    private fun prepareLevel() {
        // Here we will initialize the game objects
        // Build an army of invaders
        Obstacle.numberOfObstacles = 0
        numObstacles = 0
        for (column in 0..10) {
            for (row in 0..5) {
                obstacles.add(Obstacle(context, row, column, size.x, size.y))
                numObstacles++
            }
        }
    }

    override fun run() {
        // This variable tracks the game frame rate
        var fps: Long = 0

        while (playing) {

            // Capture the current time
            val startFrameTime = System.currentTimeMillis()

            // Update the frame
            if (!paused) {
                update(fps)
            }

            // Draw the frame
            draw()

            // Calculate the fps rate this frame
            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    private fun update(fps: Long) {
        // Update the state of all the game objects

        // Move the player's ship
        playerCar.update(fps)
    }

    private fun draw() {
        // Make sure our drawing surface is valid or the game will crash
        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            canvas.drawBitmap(playerCar.bitmap, playerCar.position.left,playerCar.position.top, paint)

            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 70f
            canvas.drawText("Score: $score Lives: $lives HI: $highScore", 20f, 75f, paint)

            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas)
        }
    }

    // If DrivingActivity is paused/stopped
    // then shut down our thread.
    fun pause() {
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    fun resume() {
        playing = true
        prepareLevel()
        gameThread.start()
    }

    //            THIS NEEDS TO BE CHANGED TO THE MOTION DETECTOR INSTEAD OF TOUCH
    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

        return true
    }

}