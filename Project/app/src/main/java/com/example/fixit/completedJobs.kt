package com.example.fixit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixit.adapters.jobCompletedAdapter
import com.example.fixit.models.jobModelWorkerCompleted
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class completedJobs : AppCompatActivity() {

    private lateinit var completedJobRecyclerView: RecyclerView

    private lateinit var completedJobList: ArrayList<jobModelWorkerCompleted>

    private lateinit var dbRef: DatabaseReference

    private lateinit var dbRefDelete :DatabaseReference

    private lateinit var firebaseAuth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_jobs)

        firebaseAuth = FirebaseAuth.getInstance()

        //back button
        val backBtn = findViewById<ImageView>(R.id.onGoingBackBtn)

        backBtn.setOnClickListener {
            val backBtnIntent = Intent(this, workerMainMenu::class.java)
            startActivity(backBtnIntent)
        }

        completedJobRecyclerView = findViewById(R.id.completedJobsRecyclerView)
        completedJobRecyclerView.layoutManager = LinearLayoutManager(this)
        completedJobRecyclerView.setHasFixedSize(true)

        completedJobList = arrayListOf<jobModelWorkerCompleted>()

        getJobCompletedData()

    }

    private fun getJobCompletedData() {

        val firebaseUser = firebaseAuth.currentUser!!
        //database reference
        dbRef = FirebaseDatabase.getInstance().getReference("job_completed_worker")
        val query = dbRef.orderByChild("workerId").equalTo("${firebaseUser.uid}")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completedJobList.clear()
                if (snapshot.exists()) {
                    for (jobSnap in snapshot.children) {
                        val jobData = jobSnap.getValue(jobModelWorkerCompleted::class.java)
                        completedJobList.add(jobData!!)
//                        if(jobData?.title == "Electrician"){
//                            jobList.add(jobData!!)
//                        }
                    }
                    val mAdapter = jobCompletedAdapter(completedJobList)
                    completedJobRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListner(object :jobCompletedAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val dbRefDelete = FirebaseDatabase.getInstance().getReference("job_completed_worker").child(completedJobList[position].jobid.toString())
                            dbRefDelete.removeValue()

                        }

                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseError", error.message) // Log the error message
            }

        })


    }


}
