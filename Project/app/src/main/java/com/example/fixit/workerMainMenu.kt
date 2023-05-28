package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class workerMainMenu : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var workerName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_main_menu)

        firebaseAuth = FirebaseAuth.getInstance()

        getUserName()

        val profileBtn = findViewById<CardView>(R.id.profileCardWorker)
        val jobBtn = findViewById<CardView>(R.id.jobsCardWorker)
        val onGoingBtn = findViewById<CardView>(R.id.onGoingCardWorker)
        val completedBtn = findViewById<CardView>(R.id.completedCardWorker)
//        val noteBtn = findViewById<CardView>(R.id.notesCardWorker)
//Main menu to job search page
        jobBtn.setOnClickListener {
            val jobPageIntent = Intent(this,searchJobs::class.java)
            startActivity(jobPageIntent)
        }

//        Main menu to on going job page
        onGoingBtn.setOnClickListener {
            val onGoingPageIntent = Intent(this,onGoingJobs::class.java)
            startActivity(onGoingPageIntent)
        }
        //Main menu to completed jobs
        completedBtn.setOnClickListener {
            val completedPageIntent = Intent(this, completedJobs::class.java)
            startActivity(completedPageIntent)
        }

//        noteBtn.setOnClickListener {
//            val notePageIntent = Intent(this,MainActivity::class.java)
//            startActivity(notePageIntent)
//        }

        profileBtn.setOnClickListener {
            val profilePageIntent = Intent(this,Worker::class.java)
            startActivity(profilePageIntent)
        }


//      Main Menu to completed job page
    }

    fun getUserName() {

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    workerName = findViewById(R.id.dashboardWorkerName)
                    var workerDisplayName = snapshot.child("name").value as String

                    workerName.text = workerDisplayName

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    //dashboardWorkerName
}