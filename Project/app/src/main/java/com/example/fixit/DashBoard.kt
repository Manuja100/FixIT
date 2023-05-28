package com.example.fixit

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoard : AppCompatActivity() {

    lateinit var menu: LinearLayout
    lateinit var bgapp: ImageView
    lateinit var cloverimg: ImageView
    lateinit var profile: ImageView
    lateinit var splacshtxt:LinearLayout
    lateinit var hometxt:LinearLayout
    lateinit var frombottom:Animation

    lateinit var find:LinearLayout
    lateinit var pending:LinearLayout
    lateinit var ongoing:LinearLayout
    lateinit var complete:LinearLayout
    lateinit var rating:LinearLayout
    lateinit var userN:TextView
    var userName = ""

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        firebaseAuth = FirebaseAuth.getInstance()

        getUserName()


        menu = findViewById(R.id.menu)
        bgapp = findViewById(R.id.bgapp)
        cloverimg = findViewById(R.id.cloverimg)
        splacshtxt = findViewById(R.id.splashscreen)
        hometxt = findViewById(R.id.hometext)
        frombottom = AnimationUtils.loadAnimation(this,R.anim.bganim)

        profile = findViewById(R.id.profile)
        find = findViewById(R.id.findworker)
        pending = findViewById(R.id.pending)
        ongoing = findViewById(R.id.inprogress)
        complete = findViewById(R.id.complete)
        rating = findViewById(R.id.rating)




        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        bgapp.postDelayed({
            bgapp.animate()
                .translationY(-screenHeight.toFloat() / 1.7f) // slide up halfway
                .setDuration(1000)
                .withEndAction {
                    // animation finished
                }
                .start()
        }, 700)


        cloverimg.alpha = 1.0f

// animate the ImageView to fade in
        cloverimg.animate()
            .alpha(0.0f) // fade in
            .setDuration(1000)
            .start()
        bgapp.postDelayed({
            splacshtxt.animate()
                .alpha(0.0f)
                .translationY(140.0f)
                .setDuration(800)
                .start()}, 700)

        hometxt.startAnimation(frombottom)
        menu.startAnimation(frombottom)

        find.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, MapsActivity::class.java)

            // Start the new activity
            startActivity(intent)
        }

        pending.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, Pending::class.java)

            // Start the new activity
            startActivity(intent)
        }

        ongoing.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, OnGoing::class.java)

            // Start the new activity
            startActivity(intent)
        }

        complete.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, Complete::class.java)

            // Start the new activity
            startActivity(intent)
        }

        profile.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, Customer::class.java)

            // Start the new activity
            startActivity(intent)
        }

        rating.setOnClickListener {
            Toast.makeText(this, "rating clicked", Toast.LENGTH_SHORT).show()
        }

    }



    fun getUserName() {

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    userN = findViewById(R.id.userName)
                    userName = snapshot.child("name").value as String

                    userN.text = userName

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }


}
