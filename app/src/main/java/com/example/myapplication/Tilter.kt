package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView



class Tilter(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var currentLane = 0

    private val TILT_THRESHOLD = 2.5f

    init {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            if (x > TILT_THRESHOLD) {
                currentLane = 1
            } else if (x < -TILT_THRESHOLD) {
                currentLane = 2
            } else {
                currentLane = 0
            }
            updateCarPosition()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun updateCarPosition() {
        CarPoster.moving = when (currentLane) {
            0 -> CarPoster.stopped
            1 -> CarPoster.left
            2 -> CarPoster.right
            else -> CarPoster.stopped
        }
    }

    fun unregister() {
        sensorManager.unregisterListener(this)
    }
}
