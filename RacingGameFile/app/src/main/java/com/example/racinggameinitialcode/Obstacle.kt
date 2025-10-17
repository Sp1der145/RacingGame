package com.example.racinggameinitialcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import kotlin.random.Random

class Obstacle(context: Context, screenX: Int, screenY: Int, speedMultiplier: Float) {

    private val width = screenX / 10f
    private val height = screenY / 12f
    private var speed = 300f * speedMultiplier

    val position = RectF()
    val bitmap: Bitmap

    init {
        val random = Random(System.currentTimeMillis())

        // Random X position
        val x = random.nextInt(screenX - width.toInt())
        position.left = x.toFloat()
        position.top = -height
        position.right = position.left + width
        position.bottom = position.top + height

        // Random image from a set of obstacles
        val obstacleImages = listOf(
            R.drawable.semi1,
            R.drawable.lambo1,
            R.drawable.sign1
        )
        val randomImage = obstacleImages[random.nextInt(obstacleImages.size)]

        // Load and scale
        bitmap = BitmapFactory.decodeResource(context.resources, randomImage)
        val scaled = Bitmap.createScaledBitmap(bitmap, width.toInt(), height.toInt(), false)
        bitmap.recycle()  // free memory
    }

    fun update(fps: Long) {
        position.top += speed / fps
        position.bottom = position.top + height
    }
}
