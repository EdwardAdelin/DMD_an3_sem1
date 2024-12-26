package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class TextEditorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_editor)

        val editText = findViewById<EditText>(R.id.editText)
        val buttonShare = findViewById<Button>(R.id.buttonShare)

        buttonShare.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                shareText(text)
            }
        }
    }

    private fun shareText(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}