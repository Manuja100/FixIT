package com.example.fixit

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.Toast
import com.example.fixit.databinding.ActivityCustomerEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CustomerEdit : AppCompatActivity() {

    private lateinit var binding : ActivityCustomerEditBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var selectedImg : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnEditCancel = findViewById<ImageButton>(R.id.btnCancel)
        btnEditCancel.setOnClickListener{
            startActivity(Intent(this,Customer::class.java))
            finish()
        }

        binding.updateButton.setOnClickListener {

            validateDate()

        }

    }

    private fun validateDate(){

        val editName = binding.cusEtName.text.toString()
        val editEmail = binding.cusEtEmail.text.toString()
        val editAge = binding.cusEtAge.text.toString()
        val editNumber = binding.cusEtNumber.text.toString()

        if (editName.isEmpty()) {
            Toast.makeText(this, "Empty Name field", Toast.LENGTH_SHORT).show()
        } else if ( !Patterns.EMAIL_ADDRESS.matcher(editEmail).matches() ) {
            Toast.makeText(this, "Empty Email field or Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        } else if ( !validateAge(editAge)) {
            Toast.makeText(this, "Enter your Age", Toast.LENGTH_SHORT).show()
        } else if ( !isValidPhoneNumber(editNumber)) {
            Toast.makeText(this, "Empty Number field or Invalid Number", Toast.LENGTH_SHORT).show()
        }  else
            updateDate(editEmail, editName, editAge, editNumber, selectedImg)
    }

    fun isValidPhoneNumber(editNumber: String): Boolean {
        val regexPattern = """^\d{10}$""".toRegex()
        return regexPattern.matches(editNumber)
    }

    fun validateAge(editAge: String): Boolean {
        // Check if the age string contains only digits
        if (!editAge.matches(Regex("\\d+"))) {
            Toast.makeText(this, "Invalid Age", Toast.LENGTH_SHORT).show()
            return false
        }

        // Convert the age string to an Int value and validate the age
        val age = editAge.toInt()
        if (age < 18 || age > 65) {
            Toast.makeText(this, "Your are under age (18) or over age (65)", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun updateDate(editEmail: String, editName: String, editAge: String, editNumber: String, selectedImg: Any) {

        database = FirebaseDatabase.getInstance().getReference("Users")

        val uid = firebaseAuth.uid

        val hashMap: HashMap<String,Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = editEmail
        hashMap["name"] = editName
        hashMap["age"] = editAge
        hashMap["number"] = editNumber

        database.child(uid.toString()).updateChildren(hashMap).addOnSuccessListener {

            binding.cusEtEmail.text.clear()
            binding.cusEtName.text.clear()
            binding.cusEtAge.text.clear()
            binding.cusEtNumber.text.clear()
            Toast.makeText(this,"$editName, Your Account has been Updated", Toast.LENGTH_LONG).show()

            startActivity(Intent(this,Login::class.java))
            finish()

        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Update", Toast.LENGTH_LONG).show()
        }

    }

    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        //if (data != null){
            //if (data.data != null){
                //selectedImg = data.data!!

                //binding.userImage.setImageURI(selectedImg)
            //}
        //}
    //}

}
