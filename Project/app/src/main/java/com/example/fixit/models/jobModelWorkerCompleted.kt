package com.example.fixit.models

data class jobModelWorkerCompleted (
    var jobid:String? = null,
    var cusName:String? = null,
    var location:String? = null,
    var status:String? = null,
    var title:String? = null,
    var workerId:String?=null,
    var paymentId:String? = null,
    var completedDate:String? = null
)
