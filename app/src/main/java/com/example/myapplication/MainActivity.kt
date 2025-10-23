package com.example.myapplication

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.FrameLayout
import kotlinx.coroutines.*
import android.widget.ImageView
import android.widget.TextView
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var updater: Updater
    private lateinit var car: ImageView;
    private var tilter: Tilter? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val layout = findViewById<FrameLayout>(R.id.myFrameLayout)
        val scoreTextView = findViewById<TextView>(R.id.scoreText)

        val bounds = windowManager.currentWindowMetrics.bounds
        val size = Point(bounds.width(), bounds.height())

        val background = findViewById<ImageView>(R.id.imageView2)
        background.scaleType = ImageView.ScaleType.CENTER_CROP
        val backgroundLooper = BackgroundLooper(background, this, 80) // change every 80ms


        updater = Updater(this, layout, size, scoreTextView)
        backgroundLooper.start()
        tilter = Tilter(this)

        lifecycleScope.launch {
            while (isActive) {
                updater.update()
                delay(16L) // ~60fps
            }
        }
    }
}

class BackgroundLooper(
    private val imageView: ImageView,
    private val context: Context,
    private val interval: Long = 500L // milliseconds
) {
    private val images = arrayOf(
        R.drawable.background1,
        R.drawable.background2,
        R.drawable.background3,
        R.drawable.background4
    )

    private var currentIndex = 0
    private var job: Job? = null

    fun start() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                imageView.setImageResource(images[currentIndex])
                currentIndex = (currentIndex + 1) % images.size
                delay(interval)
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}