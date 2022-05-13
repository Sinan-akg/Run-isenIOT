package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.runisenapp.databinding.ActivitySettingsPageBinding
import com.example.runisenapp.databinding.ActivitySettingsPageDataBinding

class SettingsPageData : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsPageDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page_data)
        binding = ActivitySettingsPageDataBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imageClick17 = findViewById<ImageView>(R.id.backButtonn)
        imageClick17.setOnClickListener {
            val intent = Intent(this, SettingsPage::class.java)
            startActivity(intent)
        }

    }
}