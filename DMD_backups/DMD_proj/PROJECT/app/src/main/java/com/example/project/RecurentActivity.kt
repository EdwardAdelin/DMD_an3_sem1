package com.example.project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecurentActivity : ComponentActivity() {

    private var totalValue: Double = 0.0
    private lateinit var totalValueTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecurrentExpenseAdapter
    private val expenses = mutableListOf<RecurrentExpense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recurent)

        val paymentNameEditText: EditText = findViewById(R.id.payment_name)
        val paymentValueEditText: EditText = findViewById(R.id.payment_value)
        val addPaymentButton: Button = findViewById(R.id.add_payment_button)
        totalValueTextView = findViewById(R.id.total_value)
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecurrentExpenseAdapter(expenses) { expense ->
            totalValue -= expense.value
            totalValueTextView.text = "Total Value: $totalValue"
            saveData()
        }
        recyclerView.adapter = adapter

        loadData()

        addPaymentButton.setOnClickListener {
            val paymentName = paymentNameEditText.text.toString()
            val paymentValue = paymentValueEditText.text.toString().toDoubleOrNull()

            if (paymentName.isNotEmpty() && paymentValue != null) {
                val expense = RecurrentExpense(paymentName, paymentValue)
                expenses.add(expense)
                adapter.notifyItemInserted(expenses.size - 1)
                totalValue += paymentValue
                totalValueTextView.text = "Total Value: $totalValue"
                paymentNameEditText.text.clear()
                paymentValueEditText.text.clear()
                saveData()
                if (totalValue > 1000) {
                    sendNotification()
                }
            }
        }
    }

    private fun sendNotification() {
        val channelId = "expenses_channel"
        val channelName = "Expenses Notifications"
        val notificationId = 1

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Expenses Alert")
            .setContentText("Total expenses have exceeded 1000!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(this).notify(notificationId, notification)
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        } else {
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        }
    }

    override  fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                sendNotification()
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("recurent_expenses", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(expenses)
        editor.putString("expenses_list", json)
        editor.putFloat("total_value", totalValue.toFloat())
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("recurent_expenses", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("expenses_list", null)
        val type = object : TypeToken<MutableList<RecurrentExpense>>() {}.type
        if (json != null) {
            val loadedExpenses: MutableList<RecurrentExpense> = gson.fromJson(json, type)
            expenses.addAll(loadedExpenses)
            adapter.notifyDataSetChanged()
        }
        totalValue = sharedPreferences.getFloat("total_value", 0.0f).toDouble()
        totalValueTextView.text = "Total Value: $totalValue"
    }
}