package com.example.runisenapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.runisenapp.databinding.ActivityHomeBinding
import com.google.android.gms.location.*
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val callback= object:LocationCallback(){
        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }


        override fun onLocationResult(result: LocationResult) {
            val lastLocation= result.lastLocation
            Log.d("TAG", "onLocationResult: ${lastLocation.longitude.toString()}")
            Log.d("TAG", "onLocationResult: ${lastLocation.latitude.toString()}")

            findViewById<TextView>(R.id.longitudeText).text="Longitude : "+lastLocation.longitude.toString()
            findViewById<TextView>(R.id.latitudeText).text="Latitude : "+lastLocation.latitude.toString()

            super.onLocationResult(result)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        onGPS()

        findViewById<Button>(R.id.bt_location).setOnClickListener {
            fetchLocation()
        }

        binding.buttonStartStopRace.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, TimeService::class.java)
        registerReceiver(updateTime, IntentFilter(TimeService.TIMER_UPDATED))

        val imageClick = findViewById<ImageView>(R.id.backButtonn)
        imageClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun onGPS() {

        Log.d("TAG", "onGPS: ${isLocationEnabled()}")

        if (!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    else
    {
        fetchLocation()
    }
}


    private fun fetchLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),200)
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),200)
            return
            }else
                {
                requestLocation()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        Log.d("TAG", "requestLocation: ")
        val requestLocation= LocationRequest()
        requestLocation.priority = LocationRequest.QUALITY_HIGH_ACCURACY
       requestLocation.interval = 0
        requestLocation.fastestInterval = 0
        requestLocation.numUpdates = 1
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                requestLocation,callback, it
            )
        }

    }


    fun isLocationEnabled(): Boolean {
        val locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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
        Toast.makeText(
            this,
            "L'utilisateur a activé la localisation",
            Toast.LENGTH_SHORT
        ).show()
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
            R.id.history -> startActivity(Intent(this, HistoryPage :: class.java))
            R.id.settingsMenu -> startActivity(Intent(this, SettingsPage ::class.java))
            }

        return super.onOptionsItemSelected(item)
    }
}
