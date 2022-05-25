package com.example.runisenapp

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.runisenapp.databinding.ActivitySettingsPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class SettingsPage : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsPageBinding
    private lateinit var database : DatabaseReference
  //  private lateinit var auth : FirebaseAuth
  //  private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // auth = FirebaseAuth.getInstance()
     //   val uid = auth.currentUser!!.uid
      // a conserver  database = Firebase.database.reference
        binding.doneButton.setOnClickListener{

        showProgressBar()

        val genre = binding.radioGroup.checkedRadioButtonId.toString()
        val lastname = binding.inputName.text.toString()
        val firstname = binding.inputfirstName.text.toString()
        val age = binding.inputage.text.toString()
        val taille = binding.inputTaille.text.toString()
        val poids = binding.inputPoid.text.toString()
        val pointure = binding.inputPied.text.toString()
        val niveau = binding.radioGroup2.checkedRadioButtonId.toString()


        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User(genre,lastname,firstname,age,taille,poids,pointure,niveau)
            //database.child("user")


        database.child(lastname).setValue(User).addOnSuccessListener (this) {

                Log.d("Profile","écriture des données")

                binding.inputName.text.clear()
                binding.inputfirstName.text.clear()
                binding.inputage.text.clear()
                binding.inputTaille.text.clear()
                binding.inputPoid.text.clear()
                binding.inputPied.text.clear()

                hideProgressBar()
                Log.d("Profile","Profil enregistré")
                Toast.makeText(this@SettingsPage, "Profil enregistré !", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                hideProgressBar()
                Log.d("Profile","Erreur d'enregistrement")
                Toast.makeText(this@SettingsPage, "Erreur d'enregistrement", Toast.LENGTH_SHORT).show()
            }

        }

        val imageClick2 = findViewById<ImageView>(R.id.backButtonn)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menusettings,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.personalpage -> startActivity(Intent(this, SettingsPageData::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showProgressBar(){
        dialog = Dialog(this@SettingsPage)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.dismiss()
    }

}