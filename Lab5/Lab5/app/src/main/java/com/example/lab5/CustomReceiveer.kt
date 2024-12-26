package com.example.lab5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message")
        Toast.makeText(context, message ?: "Nici un mesaj primit", Toast.LENGTH_SHORT).show()
    }
}