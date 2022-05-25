package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.runisenapp.databinding.ActivitySettingsPageBinding
import com.example.runisenapp.databinding.ActivitySettingsPageDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsPageData : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsPageDataBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page_data)
        binding = ActivitySettingsPageDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFind.setOnClickListener{
            val lastName : String = binding.rechercherLastname.text.toString()
            if (lastName.isNotEmpty()){
                readData(lastName)
            }else{
                Toast.makeText(this,"Veuillez entrer un nom de famille",Toast.LENGTH_SHORT).show()
            }
        }

        val imageClick17 = findViewById<ImageView>(R.id.backButtonn)
        imageClick17.setOnClickListener {
            val intent = Intent(this, SettingsPage::class.java)
            startActivity(intent)
        }

    }

    private fun readData(lastname: String) {
        database = FirebaseDatabase.getInstance().getReference("User")
        database.child(lastname).get().addOnSuccessListener {
            if(it.exists()){
                val genre = it.child("genre").value
                val lastname = it.child("lastName").value
                val firstname = it.child("firstname").value
                val age = it.child("age").value
                val taille = it.child("taille").value
                val poids = it.child("poid").value
                val pointure = it.child("pointure").value
                val niveau = it.child("niveau").value
                Toast.makeText(this,"Lecture réussie",Toast.LENGTH_SHORT).show()
                binding.rechercherLastname.text.clear()
                binding.genre.text = genre.toString()
                binding.textLastname.text = lastname.toString()
                binding.textFirstName.text = firstname.toString()
                binding.age.text = age.toString()
                binding.taille.text = taille.toString()
                binding.poids.text = poids.toString()
                binding.pointure.text = pointure.toString()
                binding.niveau.text = niveau.toString()

            }else{
                Toast.makeText(this,"Le profil lié à ce nom de famille n'existe pas",Toast.LENGTH_SHORT).show()

            }
        }.addOnFailureListener{
            Toast.makeText(this,"Erreur",Toast.LENGTH_SHORT).show()

        }
    }
}