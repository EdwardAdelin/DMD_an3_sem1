package com.example.services03

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.services03.databinding.ActivityMainBinding
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isBound = false


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "Notification permission is required for foreground service", Toast.LENGTH_SHORT).show()
            }
        }

    // TO DO 4 #preparing-bound-service
    //  declare the quoteService
    private var quoteService: QuoteFetchService? = null

    // TO DO 4 #preparing-bound-service
    //  create serviceConnection and use the function defined in the QuoteFetchService's Binder inner class to retrieve the quoteService instance
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as QuoteFetchService.QuoteBinder
            quoteService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Foreground Service
        binding.startForegroundServiceButton.setOnClickListener {
            // TO DO 3 #starting-a-foreground-service
            // start the ForegroundCounterService
            val intent = Intent(this, ForegroundCounterService::class.java)
            startService(intent)
        }

        binding.stopForegroundServiceButton.setOnClickListener {
            // TO DO 3 #stoping-a-foreground-service
            // stop the ForegroundCounterService
            val intent = Intent(this, ForegroundCounterService::class.java)
            stopService(intent)
        }

        // Background Service
        binding.startBackgroundServiceButton.setOnClickListener {
            // TO DO 1 #starting-a-background-service
            // start the BackgroundCounterService
            val intent = Intent(this, BackgroundCounterService::class.java)
            startService(intent)
        }


        binding.stopBackgroundServiceButton.setOnClickListener {
            // TO DO 1 #stoping-a-background-service
            // stop the BackgroundCounterService
            val intent = Intent(this, BackgroundCounterService::class.java)
            stopService(intent)

        }

        // Bound Service
        binding.bindServiceButton.setOnClickListener {
            // TO DO 4 #binding-a-service
            //  bind QuoteFetchService
            val intent = Intent(this, QuoteFetchService::class.java)
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        }

        binding.unbindServiceButton.setOnClickListener {
            // TO DO 4 #unbiding-a-service
            // unbind serviceConnection
            if (isBound) {
                unbindService(serviceConnection)
                isBound = false
            }
        }

        binding.fetchQuoteButton.setOnClickListener {
            // TODO 4 #retrieving-data-from-the-bound-service
            // retrieve the (quote, author) pair from the quoteService using the getLatestQuote() function
            if (isBound) {
                val (quote, author) = quoteService?.getLatestQuote() ?: return@setOnClickListener
                binding.quoteTextView.text = "\"$quote\"\n\n- $author"
            }
            //uncomment this line
             //binding.quoteTextView.text = "\"$quote\"\n\n- $author"
        }
    }
}
