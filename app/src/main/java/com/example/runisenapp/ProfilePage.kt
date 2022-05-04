package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.runisenapp.ble.BluetoothActivity
import com.example.runisenapp.databinding.ActivityProfilePageBinding

class ProfilePage : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imageClick2 = findViewById<ImageView>(R.id.backButton)
        imageClick2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.semelle.setOnClickListener {
            startActivity(Intent(this, SemelleActivity ::class.java))
        }

        binding.bluetooth.setOnClickListener {
            startActivity(Intent(this, BluetoothActivity ::class.java))
        }

        binding.about.setOnClickListener {
            startActivity(Intent(this, AboutActivity ::class.java))
        }
    }
}