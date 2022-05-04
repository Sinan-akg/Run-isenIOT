package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.example.runisenapp.databinding.ActivitySettingsPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsPage : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsPageBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lastname = binding.inputName.text.toString()
        val firstname = binding.inputfirstName.text.toString()
        val age = binding.inputage.text.toString()
        val taille = binding.inputTaille.text.toString()
        val poid = binding.inputPoid.text.toString()
        val pointure = binding.inputPied.text.toString()

        binding.doneButton.setOnClickListener{

            database = FirebaseDatabase.getInstance().getReference("Users")
            val user = User(firstname,lastname,age,taille,poid,pointure)
            database.child(lastname).setValue(user).addOnSuccessListener {

                binding.inputName.text.clear()
                binding.inputfirstName.text.clear()
                binding.inputage.text.clear()
                binding.inputTaille.text.clear()
                binding.inputPoid.text.clear()
                binding.inputPied.text.clear()

                Toast.makeText(this, "Profil enregistré !", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{

                Toast.makeText(this, "Erreur d'enregistrement", Toast.LENGTH_SHORT).show()
            }

        }

        val imageClick2 = findViewById<ImageView>(R.id.backButton)
        imageClick2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val animimage1 = findViewById<RadioButton>(R.id.imageDebutant)
        animimage1.setOnClickListener{

            animimage1.animate().apply{
                duration = 200
                translationYBy(-30f)
             }.withEndAction {

                animimage1.animate().apply {
                    duration = 200
                    translationYBy(30f)
                }.start()
            }

            val toast = Toast.makeText(this, "Sportif débutant sélectionné", Toast.LENGTH_SHORT)
            toast.show()
        }

        val animimage2 = findViewById<RadioButton>(R.id.imageAmateur)
        animimage2.setOnClickListener{

            animimage2.animate().apply{
                duration = 200
                translationYBy(-30f)
            }.withEndAction {

                animimage2.animate().apply {
                    duration = 200
                    translationYBy(30f)
                }.start()
            }

            val toast = Toast.makeText(this, "Sportif amateur sélectionné", Toast.LENGTH_SHORT)
            toast.show()
        }

        val animimage3 = findViewById<RadioButton>(R.id.imageAvance )
        animimage3.setOnClickListener{

            animimage3.animate().apply{
                duration = 200
                translationYBy(-30f)
            }.withEndAction {

                animimage3.animate().apply {
                    duration = 200
                    translationYBy(30f)
                }.start()
            }

            val toast = Toast.makeText(this, "Sportif avancé sélectionné", Toast.LENGTH_SHORT)
            toast.show()
        }

        val animimage4 = findViewById<RadioButton>(R.id.imagePro)
        animimage4.setOnClickListener{

            animimage4.animate().apply{
                duration = 200
                translationYBy(-30f)
            }.withEndAction {

                animimage4.animate().apply {
                    duration = 200
                    translationYBy(30f)
                }.start()
            }

            val toast = Toast.makeText(this, "Sportif pro sélectionné", Toast.LENGTH_SHORT)
            toast.show()
        }




    }
}