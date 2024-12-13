package com.example.services03

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class QuoteFetchService : Service() {

    //private val binder = QuoteBinder()
    private var latestQuote: String = "Fetching quote..."
    private var latestAuthor: String = ""
    private var isFetching = false
    private var fetchThread: Thread? = null

    // TO DO 4 #binding-a-service
    // override the onBind function and call the startFetching() function inside
    override fun onBind(intent: Intent?): IBinder? {
        startFetching()
        return QuoteBinder()
    }

    // TO DO 4 #unbiding-a-service
    // override the onUnbind function and call the stopFetching() function inside
    override fun onUnbind(intent: Intent?): Boolean {
    stopFetching()
    return super.onUnbind(intent)
}

    // TO DO 4 #unbiding-a-service
    // override the onDestroy function and call the stopFetching() function inside
    override fun onDestroy() {
        super.onDestroy()
        stopFetching()
    }

    private fun startFetching() {
        if (isFetching) return // Avoid starting multiple threads

        isFetching = true
        fetchThread = Thread {
            while (isFetching) {
                try {
                    fetchQuote()
                    Thread.sleep(1000) // Fetch every second
                } catch (e: InterruptedException) {
                    Log.e("QuoteFetchService", "Fetching thread interrupted: ${e.message}")
                    isFetching = false
                }
            }
        }
        fetchThread?.start()
    }

    private fun stopFetching() {
        isFetching = false
        fetchThread?.interrupt() // Stop the thread gracefully
        fetchThread = null
    }

    private fun fetchQuote() {
        try {
            val url = URL("https://quotes-api-self.vercel.app/quote")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                latestQuote = json.getString("quote")
                latestAuthor = json.getString("author")
            } else {
                Log.e("QuoteFetchService", "Error fetching quote: ${connection.responseCode}")
            }
        } catch (e: Exception) {
            Log.e("QuoteFetchService", "Exception: ${e.message}")
        }
    }


    fun getLatestQuote(): Pair<String, String> {
        return Pair(latestQuote, latestAuthor)
    }


    inner class QuoteBinder : Binder() {
        // TO DO 4 #preparing-bound-service
        // create a function that returns the this@QuoteFetchService
        fun getService(): QuoteFetchService = this@QuoteFetchService
    }

    //override fun onBind(intent: Intent?): IBinder? = null
}
