package com.example.ex3_lab4

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ex3_lab4.ui.theme.Ex3_lab4Theme
import java.io.File
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        writeToInternalStorage(this, "example.txt", "Ada, Adelin, Alex, Ion, Jenny")
    }
}
//3
fun writeToInternalStorage(context: Context, filename: String, content: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(content.toByteArray())
    }
}
//4
fun writeTextFile(context: Context, fileName: String, fileContent: String) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/")  // Path for shared storage
    }

    val fileUri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    fileUri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            outputStream.write(fileContent.toByteArray())
            outputStream.flush()
        }
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
    Ex3_lab4Theme {
        Greeting("Android")
    }
}