package com.example.lab5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button as ComposeButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab5.ui.theme.Lab5Theme
import java.security.MessageDigest
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Lab5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        AirplaneModeListener()
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomBroadcastButton()
                        Spacer(modifier = Modifier.height(16.dp))
                        MessageHashingSection()
                    }
                }
            }
        }

        // dynamic broadcast receiver pt hashed messages
        val intentFilter = IntentFilter("com.example.lab5.HASHED_MESSAGE")
        registerReceiver(hashReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(hashReceiver)
    }

    //exercitiul 3 reciver
    private val hashReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val hashedMessage = intent?.getStringExtra("hashed_message")
            Toast.makeText(context, "Mesaj hash-uit: $hashedMessage", Toast.LENGTH_LONG).show()
        }
    }

    // trimite custom broadcast
    private fun sendCustomBroadcast(context: Context) {
        val intent = Intent("com.example.lab5.CUSTOM_BROADCAST")
        intent.setPackage(context.packageName)
        intent.putExtra("message", "Hello from explicit broadcast!")
        context.sendBroadcast(intent)
        Toast.makeText(context, "custom broadcast trimit", Toast.LENGTH_SHORT).show()
    }

    // Function to hash the message and send a broadcast
    private fun hashAndSendBroadcast(message: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val hashedMessage = hashMessage(message)
            val intent = Intent("com.example.lab5.HASHED_MESSAGE")
            intent.setPackage(packageName)
            intent.putExtra("hashed_message", hashedMessage)
            sendBroadcast(intent)
        }
    }

    // Hashing function - SHA-256
    private fun hashMessage(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Composable function for sending a custom broadcast
    @Composable
    fun CustomBroadcastButton() {
        val context = LocalContext.current
        ComposeButton(onClick = { sendCustomBroadcast(context) },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Red,  // Fundal buton
                contentColor = androidx.compose.ui.graphics.Color.White  // Culoare text
            )
            ) {
            Text("Trimite Custom Broadcast")
        }
    }

    // gestionare input hashuire mesaj buton
    @Composable
    fun MessageHashingSection() {
        val context = LocalContext.current
        val inputMessage = remember { mutableStateOf("") }

        Column {
            EditTextField(inputMessage.value) { inputMessage.value = it }
            Spacer(modifier = Modifier.height(15.dp))
            ComposeButton(onClick = {
                if (inputMessage.value.isNotEmpty()) {
                    hashAndSendBroadcast(inputMessage.value)
                } else {
                    Toast.makeText(context, "Te rog introdu un mesaj", Toast.LENGTH_SHORT).show()
                }
            },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Red,  // Fundal buton
                    contentColor = androidx.compose.ui.graphics.Color.White  // Culoare text
                )
            ) {
                Text("Hash & Send")
            }
        }
    }

    // functie care face un TextField pentru a introduce un mesaj de hash-uit
    @Composable
    fun EditTextField(value: String, onValueChange: (String) -> Unit) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Mesaj pentru hash-uire") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    //exercitiul 1
    @Composable
    fun AirplaneModeListener() {
        val context = LocalContext.current
        val airplaneModeReceiver = remember {
            //reciver pentru modul avion
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                        val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                        Log.d("EX1:AirplaneModeReceiver", "Modul avion este ${if (isAirplaneModeOn) "PORNIT" else "OPRIT"}")
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            context.registerReceiver(airplaneModeReceiver, intentFilter)

            onDispose {
                context.unregisterReceiver(airplaneModeReceiver)
            }
        }

        Text("ex1:Se urmareste starea modului avion...")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Lab5Theme {
            Column {
                AirplaneModeListener()
                CustomBroadcastButton()
                MessageHashingSection()
            }
        }
    }
}