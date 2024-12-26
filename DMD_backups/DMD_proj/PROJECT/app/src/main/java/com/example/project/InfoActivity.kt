package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val serviceIntent = Intent(this, MyBackgroundService::class.java)

        // Start the background service
        val startServiceButton = findViewById<Button>(R.id.start_service_button)
        startServiceButton.setOnClickListener {
            startService(serviceIntent)
        }

        // Stop the background service
        val stopServiceButton = findViewById<Button>(R.id.stop_service_button)
        stopServiceButton.setOnClickListener {
            stopService(serviceIntent)
        }
    }
}