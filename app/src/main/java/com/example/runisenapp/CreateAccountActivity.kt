package com.example.runisenapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.runisenapp.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    // Les composants de l’interfaces
    private var et_prenom: EditText? = null
    private var et_nom: EditText? = null
    private var et_email: EditText? = null
    private var et_password: EditText? = null
    private var btnCreateAccount: Button? = null
    private var progressBar: ProgressDialog? = null
    // Les références Firebase
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null

    private val TAG = "CreateAccountActivity"

    // Les variables globales
    private var prenom: String? = null
    private var nom: String? = null
    private var email: String? = null
    private var password: String? = null

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageClick8 = findViewById<ImageView>(R.id.backButton)
        imageClick8.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        initialisation()
    }

    private fun initialisation() {

        et_prenom = findViewById(R.id.editPrenom)
        et_nom = findViewById(R.id.editNom)
        et_email = findViewById(R.id.editEmail)
        et_password = findViewById(R.id.editPassword)
        btnCreateAccount = findViewById(R.id.ForgotPasswordSend)
        progressBar = ProgressDialog(this)
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.reference!!.child("Users")
        auth = FirebaseAuth.getInstance()
        btnCreateAccount!!.setOnClickListener { creationDeCompte() }
    }

    private fun creationDeCompte() {
        prenom = et_prenom?.text.toString()
        nom = et_nom?.text.toString()
        email = et_email?.text.toString()
        password = et_password?.text.toString()
        if (!TextUtils.isEmpty(prenom) && !TextUtils.isEmpty(nom)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressBar!!.setMessage("Enregistrement de l’utilisateur…")
            progressBar!!.show()
            auth!!
                .createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    progressBar!!.hide()
                    if (task.isSuccessful) {
                    // Si l’authentification a réussi, on met à jour les informations de l’utilisateur
                        Log.d(TAG, "Création de l’utilisateur avec son email : Succès")
                        val userId = auth!!.currentUser!!.uid

                    // On vérifie l’email
                        verificationEmail()

                    // On inscrit les informations de l’utilisateur dans firebase
                        val currentUserDb = databaseReference!!.child(userId)
                        currentUserDb.child("firstName").setValue(prenom)
                        currentUserDb.child("lastName").setValue(nom)
                        MAJInfoUser()
                    } else {

                    // Si l’authentification a échouée, on affiche un message à l’utilisateur
                        Log.w(TAG, "Création de l’utilisateur avec son email : Echec", task.exception)
                        Toast.makeText(this@CreateAccountActivity, "Echec de l’authentification",
                        Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Entrer les détails", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificationEmail() {
        val utilisateur = auth!!.currentUser
        utilisateur!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@CreateAccountActivity,
                        "Envoie de la vérification par email à " + utilisateur.getEmail(),
                    Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "Vérification par Email", task.exception)
                    Toast.makeText(this@CreateAccountActivity,
                        "Echec de la vérification par email.",
                    Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun MAJInfoUser() {
        //start next activity
        val intent = Intent(this@CreateAccountActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}