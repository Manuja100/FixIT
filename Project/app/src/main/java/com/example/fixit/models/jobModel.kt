package com.example.fixit.models

data class jobModel (
    var jobid:String? = null,
    var cusName:String? = null,
    var number:String? = null,
    var location:String? = null,
    var jobDesc:String? = null,
    var latitude:Double? = null,
    var longitude:Double? = null,
    var status:String? = null,
    var title:String? = null,
    var workerId:String?=null,
    var acceptedDate:String? = null
)
