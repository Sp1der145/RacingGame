package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.random.Random

class Tilter(context: Context, layout: FrameLayout) : SensorEventListener, SurfaceView(context) {
    public var posX: Float = 1f
    public var posY: Float = 1f
    public var textView: TextView? = null

    // Accelerometer for tilt control
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    init {
        textView = TextView(context)
        //textView?.text = "asdf"

        textView?.textSize = 20f;
        textView?.translationX = 100f;
        textView?.translationY = 100f;

        layout.addView(textView)

        isClickable = true
        isFocusable = true
        setBackgroundColor(Color.argb(50, 255, 0, 0))
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val tilt = event.values[0]
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                posX = event.rawX
                textView?.text = posX.toString()
                return true
            }
            MotionEvent.ACTION_UP -> {

                performClick()
                return true
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

}