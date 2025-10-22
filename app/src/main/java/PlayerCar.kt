package com.example.myapplication

import android.content.Context
import android.graphics.Point
import android.graphics.RectF
import android.widget.FrameLayout
import android.widget.ImageView
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class CarPoster(private val imageView: ImageView) : Runnable {
    companion object {
        var positionX = 0f
        var moving = stopped

        const val stopped = 0
        const val left = 1
        const val right = 2
    }

    override fun run() {
        // --- Move left or right, stop at edge
        if (moving == left && positionX > -375) {
            positionX -= 8f
        } else if (moving == right && positionX < 335) {
            positionX += 8f
        }

        imageView.translationX = positionX
    }
}

class PlayerCar(
    context: Context,
    private val layout: FrameLayout,
    private val screenSize: Point
) {
    val imageView: ImageView = ImageView(context)
    private val carPoster: CarPoster

    init {
        imageView.setImageResource(R.drawable.car1)

        // --- Set size & position
        val carWidth = 600
        val carHeight = 1200
        val params = FrameLayout.LayoutParams(carWidth, carHeight)
        params.leftMargin = 275
        params.topMargin = 800
        imageView?.layoutParams = params

        // --- Start in the middle
        params.leftMargin = (screenSize.x / 2) - (carWidth / 2)
        params.topMargin = screenSize.y - carHeight - 150 // Positioned near bottom
        imageView.layoutParams = params

        // --- Initial translation
        imageView.translationX = 0f
        imageView.translationY = 0f

        layout.addView(imageView)

        carPoster = CarPoster(imageView)
        CarPoster.positionX = 0f
    }

    fun update() {
        imageView.post(carPoster)
    }

    fun getPosition(): RectF {
        val width = imageView.width.toFloat()
        val height = imageView.height.toFloat()
        val x = imageView.left + imageView.translationX + (3f * (width / 8f))
        val y = imageView.top + imageView.translationY + (3f * (height / 8f))

        return RectF(x, y, x + (width / 4f), y + (height / 4f))
    }

    fun resetPosition() {
        CarPoster.positionX = 0f
        imageView.translationX = 0f
    }
}
