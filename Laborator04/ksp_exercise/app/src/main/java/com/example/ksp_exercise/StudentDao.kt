// app/src/main/java/com/example/ksp_exercise/data/StudentDao.kt
package com.example.ksp_exercise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ksp_exercise.MainActivity

@Dao
interface StudentDao {
    @Insert
    suspend fun insert(student: Student)

    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): List<Student>
}