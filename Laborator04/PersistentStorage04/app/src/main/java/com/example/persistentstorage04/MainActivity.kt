package com.example.persistentstorage04

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.persistentstorage04.ui.theme.PersistentStorage04Theme

private lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("color-theme", true) // Default is dark mode
        enableEdgeToEdge()

        setContent {
            MyApp(isDarkMode)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(initialIsDarkMode: Boolean) {
    var isDarkMode by remember { mutableStateOf(initialIsDarkMode) }

    PersistentStorage04Theme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Persistent Storage")
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isDarkMode) "Dark Mode" else "Light Mode",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = {
                        isDarkMode = it
                        saveToSharedPreferences("color-theme", isDarkMode)
                    }
                )
            }
        }
    }
}

// Save to Shared Preferences
fun saveToSharedPreferences(key: String, value: Boolean) {
    with(sharedPreferences.edit()) {
        putBoolean(key, value)
        apply()
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PersistentStorage04Theme {
        MyApp(initialIsDarkMode = true)
    }
}
