package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.Toast
import com.example.fixit.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        var btnCancel = findViewById<ImageButton>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            var intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.restPwButton.setOnClickListener{
            validateData()
        }

    }

    private var email:String = ""

    private fun validateData() {

        email = binding.forgetPwEmail.text.toString()

        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            Toast.makeText(this, "Empty Email field or Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        } else  {
            resetPassword()
        }

    }

    private fun resetPassword() {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                        Toast.makeText(this, "Check email for password reset!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }
            .addOnFailureListener{e->
                    Toast.makeText(this, "Failed to Reset password due to ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

}