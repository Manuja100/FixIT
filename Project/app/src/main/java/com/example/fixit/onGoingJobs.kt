package com.example.fixit

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixit.adapters.jobOnGoingAdapter
import com.example.fixit.models.jobModel
import com.example.fixit.models.jobModelWorkerCompleted
import com.example.fixit.models.paymentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.FieldPosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class onGoingJobs : AppCompatActivity() {

    private lateinit var onGoingJobRecyclerView: RecyclerView

    private lateinit var jobList: ArrayList<jobModel>

    private lateinit var dbRef : DatabaseReference
    private lateinit var dbRefPayment : DatabaseReference
    private lateinit var dbRefCompleteWorker :DatabaseReference
    private lateinit var dbRefChangeJobStatus : DatabaseReference

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var amount : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_going_jobs)


        firebaseAuth = FirebaseAuth.getInstance()


//back button
        val backBtn = findViewById<ImageView>(R.id.onGoingBackBtn)

        backBtn.setOnClickListener {
            val backBtnIntent = Intent(this, workerMainMenu::class.java)
            startActivity(backBtnIntent)
        }


        //data retrival
        onGoingJobRecyclerView = findViewById(R.id.onGoingRecyclerView)
        onGoingJobRecyclerView.layoutManager = LinearLayoutManager(this)
        onGoingJobRecyclerView.setHasFixedSize(true)

        jobList = arrayListOf<jobModel>()

        getJobData()
    }
    private fun getJobData(){

        val firebaseUser = firebaseAuth.currentUser!!
        //database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Jobs")

        val query = dbRef.orderByChild("workerId").equalTo("${firebaseUser.uid}")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()
                if(snapshot.exists()){
                    for(jobSnap in snapshot.children){
                        val jobData = jobSnap.getValue(jobModel::class.java)
                        if(jobData?.status=="ongoing"){
                            jobList.add(jobData!!)
                        }
                    }
                    val mAdapter = jobOnGoingAdapter(jobList)
                    onGoingJobRecyclerView.adapter = mAdapter

                    //onclick listners for the buttons
                    mAdapter.setOnItemClickListner(object : jobOnGoingAdapter.onItemClickListener {
                        override fun onItemClickCompleteJob(position: Int) {
                                showDeleteBox(position)
                        }

                        override fun onItemClickViewMaps(position: Int) {

                            val destLat = jobList[position].latitude
                            val destLng = jobList[position].longitude


                            val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", destLat,destLng)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            startActivity(intent)

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseError", error.message) // Log the error message
            }
        })
    }

    private fun showDeleteBox(position: Int){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_payment)

        val completePaymentBtn = dialog.findViewById<Button>(R.id.paymentBtn)
        val cancelPaymentBtn = dialog.findViewById<Button>(R.id.paymentBtnCancel)
        val amount = dialog.findViewById<EditText>(R.id.paymentInput)

        completePaymentBtn.setOnClickListener {

            val inputAmount = amount.text.toString()

            if (inputAmount.isEmpty()) {
                Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show()
            } else if (!inputAmount.isDigitsOnly()) {
                Toast.makeText(this, "Please Enter Numbers Only", Toast.LENGTH_SHORT).show()
            } else {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                dbRefPayment = FirebaseDatabase.getInstance().getReference("payments")
                val paymentId = dbRefPayment.push().key!!
                val payment = paymentModel(
                    paymentId, jobList[position].jobid, inputAmount,
                    LocalDate.now().format(formatter)
                )
                dbRefPayment.child(paymentId).setValue(payment)


                //Adding to completed jobs of the worker
                dbRefCompleteWorker =
                    FirebaseDatabase.getInstance().getReference("job_completed_worker")
                val jobid = jobList[position].jobid.toString()
                val cusName = jobList[position].cusName.toString()
                val location = jobList[position].location.toString()
                val title = jobList[position].title.toString()
                val workerId = jobList[position].workerId.toString()
                val jobCompletedWorker = jobModelWorkerCompleted(
                    jobid,
                    cusName,
                    location,
                    "completed",
                    title,
                    workerId, paymentId,
                    LocalDate.now().format(formatter)
                )

                dbRefCompleteWorker.child(jobid).setValue(jobCompletedWorker)

                //Changing the status to pending

                val jobId = jobList[position].jobid.toString()
                val cusNumber = jobList[position].number.toString()
                val jobDesc = jobList[position].jobDesc.toString()
                val latitude = jobList[position].latitude
                val longitude = jobList[position].longitude
                val acceptedDate = jobList[position].acceptedDate.toString()
                val workerIde = jobList[position].workerId.toString()


                dbRefChangeJobStatus =
                    FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
                val jobInfo = jobModel(
                    jobId,
                    cusName,
                    cusNumber,
                    location,
                    jobDesc,
                    latitude,
                    longitude,
                    "complete",
                    title,
                    workerIde,
                    acceptedDate
                )
                dbRefChangeJobStatus.setValue(jobInfo)

            }
        }

        cancelPaymentBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

    }

}