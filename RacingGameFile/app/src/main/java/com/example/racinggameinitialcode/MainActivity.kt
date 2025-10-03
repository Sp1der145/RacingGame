package com.example.racinggameinitialcode

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Point

class MainActivity : AppCompatActivity() {
    private var kotlinDrivingView: KotlinDrivingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            // Get a Display object to access screen details
            val display = windowManager.defaultDisplay
            // Load the resolution into a Point object
            val size = Point()
            display.getSize(size)

            // Initialize gameView and set it as the view
            kotlinDrivingView = KotlinDrivingView(this, size)   //NEED TO SET UP THE VIEW
            setContentView(kotlinDrivingView)
    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        kotlinDrivingView?.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        kotlinDrivingView?.pause()
        }
}