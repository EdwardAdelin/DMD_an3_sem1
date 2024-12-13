// app/src/main/java/com/example/ksp_exercise/data/AppDatabase.kt
package com.example.ksp_exercise.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}