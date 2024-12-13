// app/src/main/java/com/example/ksp_exercise/MainActivity.kt
package com.example.ksp_exercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.ksp_exercise.data.AppDatabase
import com.example.ksp_exercise.data.Student
import com.example.ksp_exercise.ui.theme.Ksp_exerciseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ksp_exerciseTheme {
                val students = remember { mutableStateListOf<Student>() }
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "student-database"
                ).build()

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        db.studentDao().insert(Student(name = "John Doe", year = 2, meanGrade = 3.5f))
                        db.studentDao().insert(Student(name = "Jane Smith", year = 3, meanGrade = 3.8f))
                        db.studentDao().insert(Student(name = "Alice Johnson", year = 1, meanGrade = 3.9f))
                        students.addAll(db.studentDao().getAllStudents())
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        students.forEach { student ->
                            Text(text = "${student.name}, Year: ${student.year}, Mean Grade: ${student.meanGrade}")
                        }
                    }
                }
            }
        }
    }
}