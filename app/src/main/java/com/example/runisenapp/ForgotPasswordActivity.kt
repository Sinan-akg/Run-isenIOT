package com.example.runisenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {


    private val TAG = "ForgotPasswordActivity"

    // Les composants de l'interfaces
    private var et_email: EditText? = null
    private var btn_envoie: Button? = null

    //Les références Firebase
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val imageClick7 = findViewById<ImageView>(R.id.backButtonn)
        imageClick7.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        initialisation()

    }

    private fun initialisation() {
        et_email = findViewById<View>(R.id.emailText) as EditText
        btn_envoie = findViewById<View>(R.id.ForgotPasswordSend) as Button
        auth = FirebaseAuth.getInstance()
        btn_envoie!!.setOnClickListener { reInitialisationPassword() }
    }

    private fun reInitialisationPassword() {
        val email = et_email?.text.toString()
        if (!TextUtils.isEmpty(email)) {
            auth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val message = "Email envoyé."
                        Log.d(TAG, message)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        MAJInfoUser()
                    } else {
                        task.exception!!.message?.let { Log.w(TAG, it) }
                        Toast.makeText(this, "Aucun utilisateur trouvé avec cette adresse email.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Entrez votre Email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun MAJInfoUser() {
        val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}