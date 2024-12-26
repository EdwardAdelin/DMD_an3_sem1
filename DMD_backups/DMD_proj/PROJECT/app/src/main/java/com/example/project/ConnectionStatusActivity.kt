// ConnectionStatusActivity.kt
package com.example.project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.activity.ComponentActivity

class ConnectionStatusActivity : ComponentActivity() {

    private lateinit var statusTextView: TextView

    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isAirplaneModeOn = intent?.getBooleanExtra("state", false) ?: return
            updateAirplaneModeStatus(isAirplaneModeOn)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection_status)

        statusTextView = findViewById(R.id.statusTextView)
        checkAirplaneModeStatus()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(airplaneModeReceiver)
    }

    private fun checkAirplaneModeStatus() {
        val isAirplaneModeOn = Settings.Global.getInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
        updateAirplaneModeStatus(isAirplaneModeOn)
    }

    private fun updateAirplaneModeStatus(isAirplaneModeOn: Boolean) {
        if (isAirplaneModeOn) {
            statusTextView.text = "Airplane Mode is ON"
        } else {
            statusTextView.text = "Airplane Mode is OFF"
        }
    }
}