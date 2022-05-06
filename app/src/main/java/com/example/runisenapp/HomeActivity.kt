package com.example.runisenapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.example.runisenapp.databinding.ActivityHomeBinding
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHomeBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartStopRace.setOnClickListener{ startStopTimer() }
        binding.resetButton.setOnClickListener{ resetTimer()}

        serviceIntent = Intent(applicationContext, TimeService::class.java)
        registerReceiver(updateTime, IntentFilter(TimeService.TIMER_UPDATED))

        val imageClick = findViewById<ImageView>(R.id.backButton)
        imageClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun resetTimer()
    {
        stopTimer()
        time = 0.0
        binding.clock.text = getTimeStringFromDouble(time)

    }

    private fun startStopTimer()
    {
        if(timeStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimeService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.buttonStartStopRace.text = "Stop"
        binding.buttonStartStopRace.icon = getDrawable(R.drawable.ic_pause)
        timeStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.buttonStartStopRace.text = "Start"
        binding.buttonStartStopRace.icon = getDrawable(R.drawable.ic_play)
        timeStarted = false
    }

    private val updateTime : BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimeService.TIME_EXTRA, 0.0)
            binding.clock.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.loginMenu -> startActivity(Intent(this, LoginActivity ::class.java))
            R.id.profileMenu -> startActivity(Intent(this, ProfilePage ::class.java))
            R.id.settingsMenu -> startActivity(Intent(this, SettingsPage ::class.java))
            }

        return super.onOptionsItemSelected(item)
    }
}
