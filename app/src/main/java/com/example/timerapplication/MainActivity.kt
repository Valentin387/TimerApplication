package com.example.timerapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timerapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.FOREGROUND_SERVICE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                ),
                0
            )
        }

        setContentView(R.layout.activity_main)

        Intent(applicationContext, RunningService::class.java).also { intent ->
            intent.action = RunningService.Actions.START.toString()
            startService(intent)
        }


        val textView = findViewById<TextView>(R.id.textView01)
        val button = findViewById<TextView>(R.id.button01)


        timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the UI
                val text01 = "Seconds remaining: "
                textView.text = text01 + millisUntilFinished / 1000
            }

            override fun onFinish() {
                // Update the UI
                textView.text = "Done!"
                Intent(applicationContext, RunningService::class.java).also { intent ->
                    intent.action = RunningService.Actions.STOP.toString()
                    startService(intent)
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }
}