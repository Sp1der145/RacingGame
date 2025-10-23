package com.example.myapplication

import android.content.Context
import android.graphics.RectF
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.random.Random

class Obstacle(private val context: Context, private val layout: FrameLayout, private val screenWidth: Int) {
    var imageView: ImageView = ImageView(context)

    init {
        // --- Set random horizontal start position within screen width minus obstacle width
        val obstacleWidth = 300
        val obstacleHeight = 300

        val startX = Random.nextInt(0, screenWidth - obstacleWidth)
        imageView.translationX = startX.toFloat()
        // --- Start just above the screen
        imageView.translationY = -obstacleHeight.toFloat()

        // --- Random obstacle
        val obstacleImages = listOf(
            R.drawable.semi3,
            R.drawable.lambo3,
            R.drawable.sign3
        )
        val randomImage = obstacleImages.random()
        imageView.setImageResource(randomImage)

        // --- Set size
        imageView.layoutParams = FrameLayout.LayoutParams(obstacleWidth, obstacleHeight)

        // --- Add to layout
        layout.addView(imageView)
    }

    fun update(speedMultiplier: Float) {
        imageView.translationY += 5f * speedMultiplier
    }

    fun isOffScreen(screenHeight: Int): Boolean {
        return imageView.translationY > screenHeight
    }

    val position: RectF
        get() {
            val width = imageView.width.toFloat()
            val height = imageView.height.toFloat()
            val x = imageView.left + imageView.translationX + (3f * (width / 8f))
            val y = imageView.top + imageView.translationY + (3f * (height / 8f))


            return RectF(x, y, x + (width / 4f), y + (height / 4f))
        }

    fun removeFromLayout() {
        layout.removeView(imageView)
    }
}
