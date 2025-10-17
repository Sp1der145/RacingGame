package com.example.myapplication

import android.graphics.Point
import android.graphics.RectF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.coroutines.*
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.text.font.Typeface
import kotlin.collections.plusAssign
import kotlin.compareTo

class MainActivity : ComponentActivity() {
    private lateinit var car: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val layout = findViewById<FrameLayout>(R.id.myFrameLayout)

        // make tilter and updater

        //val display = windowManager.defaultDisplay
        val size = Point(1080, 2424)
        val tilter = Tilter(this, layout)
        val updater = Updater(this, layout, size)

        val background = findViewById<ImageView>(R.id.imageView2)
        val ob = Obstacle(tilter, this, layout, 1f)

        val params = FrameLayout.LayoutParams(1080, 2424) // width x height in pixels
        tilter.layoutParams = params

        layout.addView(tilter)
        layout.bringChildToFront(background)
        layout.bringChildToFront(ob.imageView)
        layout.bringChildToFront(tilter.textView)
        layout.addView(updater.playerCar!!.imageView)
        layout.bringChildToFront(updater.playerCar!!.imageView)


        //car.translationX = 50f
        //car.translationY = 350f

        Thread(updater).start()
    }
}

