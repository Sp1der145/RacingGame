package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory
import android.widget.FrameLayout
import android.widget.ImageView

class CarPoster(i: ImageView?, layout: FrameLayout) : Runnable {
    private var tilter: Tilter? = null
    private var imageView: ImageView? = null
    private val frameLayout: FrameLayout = layout;

    // This keeps track of where the ship is
    var positionX = 0f;
    var positionY = 0f;

    // This data is accessible using ClassName.propertyName
    companion object {
        // Which ways can the car move
        const val stopped = 0
        const val left = 1
        const val right = 2
    }

    // Is the ship moving and in which direction
    // Start off stopped
    var moving = stopped


    init {
        imageView = i
    }

    override fun run() {
        //val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        moving = right
        // Move as long as it doesn't try and leave the Road
        if (moving == left && positionX > -375) {
            positionX -= 8f
        }
        else if (moving == right && positionX < 335) {
            positionX += 8f
        }

        imageView?.translationX = positionX
    }
}

class PlayerCar(context: Context, frameLayout: FrameLayout) {
    public var imageView: ImageView? = null
    private var p: CarPoster? = null

    // This will hold the pixels per second speed that the ship will move
    private val speed  = 450f

    init{
        imageView = ImageView(context)
        imageView?.setImageResource(R.drawable.car1)

        // Scale the car size
        val carWidth = 600
        val carHeight = 1200
        val params = FrameLayout.LayoutParams(carWidth, carHeight)
        params.leftMargin = 275
        params.topMargin = 800
        imageView?.layoutParams = params

        p = CarPoster(imageView, frameLayout)
    }

    // This update method will be called from update in KotlinDrivingView
    // It determines if the player's ship needs to move and changes the coordinates
    fun update(fps: Long) {
        imageView?.post(p)

    }

}