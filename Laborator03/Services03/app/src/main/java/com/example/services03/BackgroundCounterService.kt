package com.example.services03

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BackgroundCounterService : Service() {

    private val TAG = "BackgroundCounterService"
    private var isCounting = true
    private var count = 0

    // TO DO 1 #starting-a-background-service
    //override the onStartCommand function and call the startCounting function inside
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //start task...
        startCounting()
        return START_STICKY
    }

    // TO DO 1 #stoping-a-background-service
    //override the onDestroy function
    override fun onDestroy() {
        super.onDestroy()
        //cleanup ...
        isCounting = false
        Log.d(TAG, "Service stopped")
    }

    private fun startCounting() {
        // TODO 2
        // Fix the issue from task 1
            Thread {
                while (isCounting) {
                    count++
                    Log.d(TAG, "Count: $count")
                    Thread.sleep(1000)
                }
            }.start()
    }

//    private fun keepCounting(){
//        while (isCounting) {
//            count++
//            Log.d(TAG, "Count: $count")
//            Thread.sleep(1000)
//        }
//    }

    override fun onBind(intent: Intent?): IBinder? = null
}
