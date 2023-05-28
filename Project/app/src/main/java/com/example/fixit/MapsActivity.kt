package com.example.fixit

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixit.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userdatabase: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btn:Button
    private lateinit var loctxt: TextView
    var latitude: Double? = null
    var longitude: Double? = null
    private lateinit var onGoingBackBtn: ImageView

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        //database reference
        database = FirebaseDatabase.getInstance().getReference("Jobs")
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        btn = findViewById<Button>(R.id.workerbtn)

        btn.setOnClickListener {
            showDialog()
        }

        onGoingBackBtn = findViewById(R.id.onGoingBackBtn)
        onGoingBackBtn.setOnClickListener(){
            // Create an Intent to start the new activity
            val intent = Intent(this, DashBoard::class.java)

            // Start the new activity
            startActivity(intent)
        }


    }

    //show popup
    private fun showDialog() {

        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude!!,
            longitude!!,
            1
        )


        val address: String =
            addresses!![0].getAddressLine(0)

        val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottompopup)


        loctxt = dialog.findViewById(R.id.locationtext)

        loctxt.text = "${city}, ${state}, ${country}"

        val titleEditText = dialog.findViewById<Spinner>(R.id.popup_title_spinner)
        val descriptionEditText = dialog.findViewById<EditText>(R.id.popup_description_input)
        val submitButton = dialog.findViewById<Button>(R.id.form_submit_button)

        val firebaseUser = firebaseAuth.currentUser!!

        val cusid = FirebaseDatabase.getInstance().getReference("Users")
        cusid.child(firebaseUser.uid)

        userdatabase = FirebaseDatabase.getInstance().getReference("Users")
        val userref = userdatabase.child(firebaseUser.uid)
        userref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val cusName = snapshot.child("name").getValue(String::class.java)
                val cusNumber = snapshot.child("number").getValue(String::class.java)

                submitButton.setOnClickListener {
                    val title = titleEditText.selectedItem.toString()
                    val name = cusName.toString()
                    val number = cusNumber.toString()
                    val description = descriptionEditText.text.toString().trim()
                    val location = city




                    if (title == "Choose Value" || description.isBlank()) {
                        Toast.makeText(this@MapsActivity, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (description.isEmpty()) {
                        descriptionEditText.error = "Description is required"
                        return@setOnClickListener
                    }



                    val jobid = database.push().key!!
                    val user1 = Client(firebaseUser.uid,number,jobid,title,name,description,location,latitude,longitude,address,"pending")
//            val newRef = database.child("jobs").push() // generate new unique key
                    database.child(jobid).setValue(user1) // add new item as child with unique key under "Users" parent node

                    Toast.makeText(this@MapsActivity, "Searching For Worker", Toast.LENGTH_SHORT).show()

//            Toast.makeText(this, "${city}, ${state}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
//            Toast.makeText(this@CustomerHomeActivity, "Looking for agent", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
                    )
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))

                    latitude = location.latitude
                    longitude = location.longitude

                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        mMap.setOnMyLocationButtonClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
                        )
                    }
                }
                true
            } else {
                false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap)
            }
        }
    }
}

