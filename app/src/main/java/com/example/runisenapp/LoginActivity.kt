package com.example.runisenapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.runisenapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //Les variables globales
    private var email: String? = null
    private var password: String? = null

    //Les composants de l'interface
    private var forgotPassword: TextView? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var Loginbutton: Button? = null
    private var accountButton: Button? = null
    private var progressBar: ProgressDialog? = null

    //La reference Firebase
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)

        val imageClick9 = findViewById<ImageView>(R.id.backButton)
        imageClick9.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        initialisation()

    }

    private fun initialisation() {
        forgotPassword = findViewById<View>(R.id.forgotPassword) as TextView
        editTextEmail = findViewById<View>(R.id.editTextEmail) as EditText
        editTextPassword = findViewById<View>(R.id.editTextPassword) as EditText
        Loginbutton = findViewById<View>(R.id.Loginbutton) as Button
        accountButton = findViewById<View>(R.id.accountButton) as Button
        progressBar = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()

        forgotPassword!!
            .setOnClickListener { startActivity(
                Intent(this@LoginActivity,
                    ForgotPasswordActivity::class.java)
            ) }
        accountButton!!
            .setOnClickListener { startActivity(Intent(this@LoginActivity,
                CreateAccountActivity::class.java)) }
        Loginbutton!!.setOnClickListener { connexion() }
    }
    private fun connexion() {
        email = editTextEmail?.text.toString()
        password = editTextPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressBar!!.setMessage("Vérification de l'utilisateur...")
            progressBar!!.show()
            Log.d(TAG, "connexion de l'utilisateur.")
            auth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    progressBar!!.hide()
                    if (task.isSuccessful) {
                        // Si l'authentification a réussi, on met à jour les informations de l'utilisateur
                        Log.d(TAG, "Connexion de l'utilisateur avec son email : Succès")
                        MAJInfoUser()
                    } else {
                        // Si l'authentification a échouée, on affiche un message à l'utilisateur
                        Log.e(TAG, "Connexion de l'utilisateur avec son email : Echec", task.exception)
                        Toast.makeText(this@LoginActivity, "Echec de l'authentication.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Entrer les détails", Toast.LENGTH_SHORT).show()
        }
    }

    private fun MAJInfoUser() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}