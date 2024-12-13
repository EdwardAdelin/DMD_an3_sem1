package com.example.lab1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab1.ui.theme.Lab1Theme

class MainActivity : AppCompatActivity() {
    private lateinit var firstButton: Button
    private lateinit var secondButton: Button
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // First method of adding a click listener
        firstButton = findViewById(R.id.button)
        firstButton.setOnClickListener {
            BUTTON_PRESS_COUNTER += 1;
            Log.d(TAG, "Pressed this button $BUTTON_PRESS_COUNTER times");
        }
    }

    companion object {
        var BUTTON_PRESS_COUNTER = 0;
        var TAG = "MainActivity"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab1Theme {
        Greeting("Android")
    }
}