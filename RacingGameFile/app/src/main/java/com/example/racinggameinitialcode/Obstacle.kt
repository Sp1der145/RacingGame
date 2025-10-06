package com.example.racinggameinitialcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import java.util.*
import android.graphics.BitmapFactory

class Obstacle(context: Context, row: Int, column: Int, screenX: Int, screenY: Int) {
    // How wide, high and spaced out are the invader will be
    var width = screenX / 35f
    private var height = screenY / 35f
    private val padding = screenX / 45

    var position = RectF(
        column * (width + padding),
        100 + row * (width + padding / 4),
        column * (width + padding) + width,
        100 + row * (width + padding / 4) + height
    )

    // This will hold the pixels per second speed that the invader will move
    private var speed = 40f

    private val left = 1
    private val right = 2

    // Is the car moving and in which direction
    private var carMoving = right

    var isVisible = true

    companion object {
        // The Obstacles will be represented by a Bitmap
        var bitmap1: Bitmap? = null
        var bitmap2: Bitmap? = null

        // keep track of the number of instances that are active (MAX IS 2)
        var numberOfObstacles = 0
    }

    init {
        // Initialize the bitmaps
        bitmap1 = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.obstacles1) //ADD FILE TO RES

        bitmap2 = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.obstacles2) //ADD FILE TO RES

        // stretch the first bitmap to a size
        // appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(
            bitmap1,
            (width.toInt()),
            (height.toInt()),
            false)

        // stretch the second bitmap as well
        bitmap2 = Bitmap.createScaledBitmap(
            bitmap2,
            (width.toInt()),
            (height.toInt()),
            false)

        numberOfObstacles ++
    }

    fun update(fps: Long) {
        if (carMoving == left) {
            position.left -= speed / fps
        }

        if (carMoving == right) {
            position.left += speed / fps
        }

        position.right = position.left + width
    }

    fun dropDownAndReverse(waveNumber: Int) {
        carMoving = if (carMoving == left) {
            right
        } else {
            left
        }

        position.top += height
        position.bottom += height

        // The later the wave, the more the Obstacles speeds up
        speed *=  (1.1f + (waveNumber.toFloat() / 20))
    }

    fun takeAim(playerCarX: Float, playerCarLength: Float, waves: Int): Boolean {
        val generator = Random()
        var randomNumber: Int

            // The higher the wave the more the more Obstacles Show up
            randomNumber = generator.nextInt(100 * numberOfObstacles) / waves
            if (randomNumber == 0) {    //THIS CODE NEEDS TO BE ALTERED FOR CORRECT GENERATION
                return true
            }


    }
}