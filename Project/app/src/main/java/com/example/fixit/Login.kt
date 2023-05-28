package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.Toast
import com.example.fixit.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        var btnCancel = findViewById<ImageButton>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            var intent = Intent(this,LogInSignUp::class.java)
            startActivity(intent)
            finish()
        }

        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        binding.signupRedirectText.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            validateData()
        }


    }

        private var email = ""
        private var password = ""

        private fun validateData() {
            email = binding.loginEmail.text.toString()
            password = binding.loginPassword.text.toString()

            if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
                Toast.makeText(this,"Empty Email field or Invalid Email Pattern", Toast.LENGTH_SHORT).show()
            }else if (email.any { it.isUpperCase() } ){
                Toast.makeText(this, "You have a Uppercase letter in your Email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()){
                Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show()
            }
            else{
                loginUser()
            }
        }

        private fun loginUser() {
            //error here handle later
            try {
                firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        checkUser()
                    }
                    .addOnFailureListener { e->
                        Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this,"Enter your Email & Password again", Toast.LENGTH_SHORT).show()
            }

        }

        private fun checkUser() {

            val firebaseUser = firebaseAuth.currentUser!!

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userType = snapshot.child("userType").value
                        if (userType == "Worker"){
                            startActivity(Intent(this@Login,workerMainMenu::class.java))
                            finish()
                        }
                        else if (userType == "Customer"){
                            startActivity(Intent(this@Login,DashBoard::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

        }





}
