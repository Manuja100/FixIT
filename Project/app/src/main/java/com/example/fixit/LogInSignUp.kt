package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LogInSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup)

        var btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            var intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

        var btnSignUp = findViewById<Button>(R.id.btnSignup)
        btnSignUp.setOnClickListener{
            var intent = Intent(this,Signup::class.java)
            startActivity(intent)
            finish()
        }

    }
}