package com.metalgreystudio.pigeonpost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_auth.setOnClickListener() {
            val email = email_auth.text.toString()
            val password = password_auth.text.toString()
            Log.d("MainActivity", "Authentification email : $email")
            Log.d("MainActivity", "Authentification password : $password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        }
        back_to_reg_auth.setOnClickListener() {
            Log.d("MainActivity", "Back to reg")
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}