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
        // Which ways can the ship move
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

        moving = left
        // Move as long as it doesn't try and leave the Road
        if (moving == left) {          //CAN CHANGE FROM SMOOTH TO FAST/AUTOMATIC
            positionX -= 1f                   //So change 'Speed' to a specific pixel jump for position?
        }
        else if (moving == right && positionX < 1080) {
            positionX += 450f / 60f
        }

        imageView?.translationX = positionX
    }
}

class PlayerCar(context: Context, frameLayout: FrameLayout) {

    // The player ship will be represented by a Bitmap
    public var imageView: ImageView? = null                //PUT IMAGE IN RES FILE

    // How wide and high our ship will be
    //val width = screenX / 20f
    //private val height = screenY / 40f


    private var p: CarPoster? = null

    // This will hold the pixels per second speed that the ship will move
    private val speed  = 450f

    init{
        imageView = ImageView(context)
        imageView?.setImageResource(R.drawable.car1)
        imageView?.translationX = 100f;
        imageView?.translationY = 100f;
        //frameLayout.addView(imageView)
        // stretch the bitmap to a size
        // appropriate for the screen resolution
        p = CarPoster(imageView, frameLayout)
    }


    //Tilting movement
    /*
    fun setTilt(tilt: Float) {
        moving = when {
            tilt > 1.5f -> left
            tilt < -1.5f -> right
            else -> stopped
        }
    }
    */

    // This update method will be called from update in KotlinDrivingView
    // It determines if the player's ship needs to move and changes the coordinates
    fun update(fps: Long) {
        imageView?.post(p)

    }

}