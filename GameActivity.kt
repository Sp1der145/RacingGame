package com.example.racinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var carPositionView: TextView

    // Lanes: 0 = left, 1 = center, 2 = right
    private var currentLane = 1

    // Thresholds to detect tilt, tune if needed
    private val TILT_THRESHOLD = 2.5f

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        carPositionView = findViewById(R.id.carPositionView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        updateCarPosition()
    }

     override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

     override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]  // X-axis acceleration (tilt left/right)

            if (x > TILT_THRESHOLD) {
                // Tilted left: move left if possible
                if (currentLane > 0) {
                    currentLane--
                    updateCarPosition()
                }
            } else if (x < -TILT_THRESHOLD) {
                // Tilted right: move right if possible
                if (currentLane < 2) {
                    currentLane++
                    updateCarPosition()
                }
            }
            // If between thresholds, do nothing (no movement)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed for now
    }

    private fun updateCarPosition() {
        // Update the text view to show current lane
        val laneText = when (currentLane) {
            0 -> "Car is in: Left Lane"
            1 -> "Car is in: Center Lane"
            2 -> "Car is in: Right Lane"
            else -> "Car position unknown"
        }
        carPositionView.text = laneText
    }
}