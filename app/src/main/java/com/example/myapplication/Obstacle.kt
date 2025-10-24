package com.example.myapplication

import android.content.Context
import android.graphics.RectF
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.ui.util.lerp
import kotlin.random.Random

class Obstacle(private val context: Context, private val layout: FrameLayout, private val screenWidth: Int) {
    var imageView: ImageView = ImageView(context)
    var image1: Int = 0;
    var image2: Int = 0;
    var image3: Int = 0;
    var targetX: Int = 0;

    init {
        // --- Set random horizontal start position within screen width minus obstacle width
        val obstacleWidth = 800
        val obstacleHeight = 800

        var num = Random.nextInt(0, 3);

        val targets = listOf(
            -600,
            0,
            600,
        )
        targetX = targets[num];

        imageView.translationX = 0f
        // --- Start just above the screen
        imageView.translationY = -obstacleHeight.toFloat()

        num = Random.nextInt(0, 3);

        // --- Random obstacle
        val obstacleImages = listOf(
            R.drawable.semi1,
            R.drawable.lambo1,
            R.drawable.sign1
        )
        val obstacleImages1 = listOf(
            R.drawable.semi2,
            R.drawable.lambo2,
            R.drawable.sign2
        )
        val obstacleImages2 = listOf(
            R.drawable.semi3,
            R.drawable.lambo3,
            R.drawable.sign3
        )

        image1 = obstacleImages[num]
        image2 = obstacleImages1[num]
        image3 = obstacleImages2[num]

        imageView.setImageResource(image1);

        // --- Set size
        imageView.layoutParams = FrameLayout.LayoutParams(obstacleWidth, obstacleHeight)

        // --- Add to layout
        layout.addView(imageView)
    }

    fun update(speedMultiplier: Float) {
        imageView.translationY += 5f * speedMultiplier
        if (imageView.translationY < -100){
            imageView.translationY = -100f
        }
        imageView.translationX = lerp(0f, targetX.toFloat(), (imageView.translationY+100) / 1200f)
        if (imageView.translationY > 300) {
            imageView.setImageResource(image2);
        }
        if (imageView.translationY > 600){
            imageView.setImageResource(image3);
        }
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
