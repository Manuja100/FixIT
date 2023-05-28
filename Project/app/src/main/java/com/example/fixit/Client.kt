package com.example.fixit

import android.location.Address

data class Client(
    val cusid:String = "",
    val number:String ="",
    val jobid:String = "",
    val title:String = "",
    var cusName:String = "", var jobDesc: String = "", val location: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String = "",
    val status: String = ""
)
