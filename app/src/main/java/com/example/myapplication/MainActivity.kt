package com.example.myapplication

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var updater: Updater
    private var tilter: Tilter? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val layout = findViewById<FrameLayout>(R.id.myFrameLayout)
        val scoreTextView = findViewById<TextView>(R.id.scoreText)

        val bounds = windowManager.currentWindowMetrics.bounds
        val size = Point(bounds.width(), bounds.height())

        updater = Updater(this, layout, size, scoreTextView)

        tilter = Tilter(this)

        lifecycleScope.launch {
            while (isActive) {
                updater.update()
                delay(16L) // ~60fps
            }
        }
    }
}
