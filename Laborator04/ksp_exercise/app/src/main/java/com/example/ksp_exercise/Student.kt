// app/src/main/java/com/example/ksp_exercise/data/Student.kt
package com.example.ksp_exercise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val year: Int,
    val meanGrade: Float
)