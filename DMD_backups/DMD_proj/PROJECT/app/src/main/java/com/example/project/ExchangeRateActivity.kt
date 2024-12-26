package com.example.project

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.project.api.ExchangeRateApi
import com.example.project.api.ExchangeRateResponse
import com.example.project.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeRateActivity : ComponentActivity() {

    private lateinit var textViewRates: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate)

        textViewRates = findViewById(R.id.textViewRates)

        val apiKey = "1a632328c3e5190ed172813e"
        val baseCurrency = "USD"

        val api = RetrofitInstance.api
        api.getExchangeRates(apiKey, baseCurrency).enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("ExchangeRateResponse", "Response: $responseBody")
                    val rates = responseBody?.conversion_rates
                    val euroRate = rates?.get("EUR")
                    val usdRate = rates?.get("USD")
                    val ronRate = rates?.get("RON")
                    val euroToRonRate = if (euroRate != null && ronRate != null) ronRate / euroRate else null
                    textViewRates.text = "USD-EUR: $euroRate\nUSD-USD: $usdRate\nUSD-RON: $ronRate\nEUR-RON: $euroToRonRate"
                } else {
                    Toast.makeText(this@ExchangeRateActivity, "Failed to get rates", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                Toast.makeText(this@ExchangeRateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}