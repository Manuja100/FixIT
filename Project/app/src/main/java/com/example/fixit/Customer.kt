package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.fixit.databinding.ActivityCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Customer : AppCompatActivity() {

    private lateinit var binding : ActivityCustomerBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        var btnLogout = findViewById<ImageButton>(R.id.cusBtnLogout)
        btnLogout.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,LogInSignUp::class.java))
            finish()
        }

        var btnEdit = findViewById<ImageButton>(R.id.CustomerBtnEdit)
        btnEdit.setOnClickListener{
            startActivity(Intent(this,CustomerEdit::class.java))
            finish()
        }

        var btnDelete = findViewById<ImageButton>(R.id.CustomerBtnDelete)
        btnDelete.setOnClickListener{
            deleteRecode()
        }

    }


    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val name = snapshot.child("name").value
                    val email = snapshot.child("email").value
                    val age = snapshot.child("age").value
                    val number = snapshot.child("number").value

                    binding.cusName.text = name as CharSequence?
                    binding.cusEmail.text = email as CharSequence?
                    binding.cusAge.text = age as CharSequence?
                    binding.cusNumber.text = number as CharSequence?

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun deleteRecode() {

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(firebaseUser.uid).removeValue().addOnSuccessListener {
            Toast.makeText(this, "User Account Deleted", Toast.LENGTH_SHORT).show()
            deleteRecodeAuth()
        }
            .addOnFailureListener {e->
                Toast.makeText(this, "Failed deleted account from Realtime DB due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun deleteRecodeAuth() {

        val firebaseUser = firebaseAuth.currentUser!!

        firebaseUser?.delete()?.addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this, LogInSignUp::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e("Failed to delete user from Authentication", it.exception.toString())
            }
        }

    }

}