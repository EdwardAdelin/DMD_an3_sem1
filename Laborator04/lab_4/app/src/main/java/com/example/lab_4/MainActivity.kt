package com.example.lab_4

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "theme_pref"
    private val THEME_KEY = "is_dark_theme"
//main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializare
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        //alegere
        if (readThemePreference()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //toggleButton
        val themeToggle = findViewById<ToggleButton>(R.id.themeToggle)
        themeToggle.isChecked = readThemePreference()

        themeToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemePreference(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemePreference(false)
            }
        }

        // Initialize Room db
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "student-database"
        ).build()

        val studentDao = db.studentDao()

        GlobalScope.launch(Dispatchers.IO) {
            studentDao.insertStudent(
                Student(name = "Alex Iordan", year = 2, meanGrade = 8.5f),
                Student(name = "Flavius Albu", year = 1, meanGrade = 4.6f),
                Student(name = "George Florea", year = 3, meanGrade = 9.8f)
            )
        }

        GlobalScope.launch(Dispatchers.Main) {
            val students = studentDao.getAllStudents()
            val text = StringBuilder()
            for (student in students) {
                text.append("Nume: ${student.name}, An: ${student.year}, Medie: ${student.meanGrade}\n")
            }
            findViewById<TextView>(R.id.textView).text = text.toString()
        }
    }

    private fun saveThemePreference(isDarkTheme: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(THEME_KEY, isDarkTheme)
            apply()
        }
    }

    private fun readThemePreference(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }
}
