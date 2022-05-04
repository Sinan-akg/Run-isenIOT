package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val imageClick = findViewById<ImageView>(R.id.backButton)
        imageClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

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
