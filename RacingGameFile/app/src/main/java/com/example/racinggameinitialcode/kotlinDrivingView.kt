package com.example.racinggameinitialcode

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.random.Random

class KotlinDrivingView(context: Context, private val size: Point) :
    SurfaceView(context), Runnable, SensorEventListener {

    private var gameThread: Thread? = null
    private var playing = false
    private var paused = true

    private val paint = Paint()
    private var canvas: Canvas = Canvas()

    private val playerCar = PlayerCar(context, size.x, size.y)
    private val obstacles = ArrayList<Obstacle>()

    private var score = 0
    private var lives = 3
    private var lastObstacleSpawnTime = 0L
    private var startTime = System.currentTimeMillis()
    private var scrollY = 0f

    // Lane marker setup
    private val laneMarkers = ArrayList<RectF>()
    private val lanePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val laneCount = 3   // 3 lanes
    private val dashHeight = 80f
    private val dashWidth = 10f
    private val dashGap = 120f

    // Road background
    private var roadBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.road_background)

    // Scale the road image to fit the screen width
    init {
        roadBitmap = Bitmap.createScaledBitmap(roadBitmap, size.x, size.y, false)

        // Initialize lane markers (3 lanes centered)
        val laneSpacing = size.x / (laneCount + 1)
        for (i in 1..laneCount) {
            val x = i * laneSpacing.toFloat()
            var y = 0f
            while (y < size.y) {
                laneMarkers.add(RectF(x - dashWidth / 2, y, x + dashWidth / 2, y + dashHeight))
                y += dashHeight + dashGap
            }
        }
    }

    // Accelerometer for tilt control
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun run() {
        var fps: Long

        while (playing) {
            val startFrameTime = System.currentTimeMillis()
            val timeThisFrame = startFrameTime - startTime
            fps = if (timeThisFrame >= 1) 1000 / timeThisFrame else 60
            startTime = startFrameTime

            if (!paused) update(fps)
            draw()
        }
    }

    private fun update(fps: Long) {
        // Increase score
        score += 1

        // Move player
        playerCar.update(fps)

        // Scroll the road downward
        scrollY += (300f / fps)
        if (scrollY >= size.y) scrollY = 0f

        // Dynamic difficulty: increase obstacle speed over time
        val speedMultiplier = 1f + (score / 2000f)

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
    }

    private fun draw() {
        if (!holder.surface.isValid) return
        canvas = holder.lockCanvas()

        // --- Scrolling Background ---     NEED TO CHANGE
        val bgY1 = scrollY
        val bgY2 = scrollY - size.y
        canvas.drawBitmap(roadBitmap, 0f, bgY1, paint)
        canvas.drawBitmap(roadBitmap, 0f, bgY2, paint)

        // --- Draw Car ---
        canvas.drawBitmap(playerCar.bitmap, playerCar.position.left, playerCar.position.top, paint)

        // --- Draw Obstacles ---
        for (obj in obstacles) {
            canvas.drawBitmap(obj.bitmap, obj.position.left, obj.position.top, paint)
        }

        // --- Draw HUD ---
        paint.color = Color.WHITE
        paint.textSize = 60f
        canvas.drawText("Score: $score", 30f, 80f, paint)
        canvas.drawText("Lives: $lives", size.x - 250f, 80f, paint)

        // --- Game Over Overlay ---
        if (!playing && paused) {
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 100f
            canvas.drawText("GAME OVER", size.x / 2f, size.y / 2f, paint)
            paint.textSize = 70f
            canvas.drawText("Final Score: $score", size.x / 2f, size.y / 2f + 120f, paint)
            paint.textAlign = Paint.Align.LEFT
        }

        holder.unlockCanvasAndPost(canvas)
    }

    fun pause() {
        playing = false
        sensorManager.unregisterListener(this)
        try { gameThread?.join() } catch (_: InterruptedException) {}
    }

    fun resume() {
        playing = true
        paused = false
        lives = 3
        score = 0
        scrollY = 0f
        obstacles.clear()
        lastObstacleSpawnTime = System.currentTimeMillis()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        gameThread = Thread(this)
        gameThread?.start()
    }

    //Tilt sensors
    /*
    override fun onSensorChanged(event: SensorEvent) {
        val tilt = event.values[0]
        playerCar.setTilt(tilt)
    }
     */

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Touch input for testing (emulator/desktop)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (event.x > size.x / 2) playerCar.moving = PlayerCar.right
                else playerCar.moving = PlayerCar.left
            }
            MotionEvent.ACTION_UP -> playerCar.moving = PlayerCar.stopped
        }
        return true
    }

    private fun showGameOverDialog() {
        post {
            AlertDialog.Builder(context)
                .setTitle("Game Over")
                .setMessage("Final Score: $score\n\nPlay again?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> resume() }
                .setNegativeButton("Exit") { _, _ -> (context as MainActivity).finish() }
                .show()
        }
    }
}
