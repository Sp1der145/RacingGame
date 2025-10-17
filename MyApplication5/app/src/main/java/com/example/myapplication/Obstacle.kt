package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.random.Random
import androidx.core.graphics.scale

class PostData(i: ImageView?, layout: FrameLayout, t: Tilter) : Runnable {
    private var tilter: Tilter? = null
    private var imageView: ImageView? = null
    private val frameLayout: FrameLayout = layout;

    init {
        tilter = t
        imageView = i
    }

    override fun run() {
        imageView?.translationY += 2f
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        imageView?.translationX = (tilter!!.posX - imageView!!.width / 2f).toFloat()


    }
}

class Obstacle(t: Tilter, context: Context, frameLayout: FrameLayout, speedMultiplier: Float) : Runnable {
    public var imageView: ImageView? = null
    private var p: PostData? = null

    init {
        imageView = ImageView(context)

        imageView?.translationX = 0f; // overwritten by tilter
        imageView?.translationY = 400f;

        // get random obstacle image

        val random = Random(System.currentTimeMillis())

        val obstacleImages = listOf(
            R.drawable.semi1,
            R.drawable.lambo1,
            R.drawable.sign1
        )

        val randomImage = obstacleImages[random.nextInt(obstacleImages.size)]

        // apply the image

        imageView?.setImageResource(randomImage)

        // add object to layout

        frameLayout.addView(imageView)

        // post data for updating UI on main thread

        p = PostData(imageView, frameLayout, t)
    }

    override fun run() {
        while (true) {
            imageView?.post(p)
            Thread.sleep(16) // ~60 FPS

            if ((imageView!!.translationY - imageView!!.height / 2f) > 500){
                //frameLayout.removeView(imageView);
                break
            }
        }
    }
}
