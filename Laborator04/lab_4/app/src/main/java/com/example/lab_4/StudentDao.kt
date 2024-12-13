package com.example.lab_4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(vararg students: Student)

    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<Student>
}
