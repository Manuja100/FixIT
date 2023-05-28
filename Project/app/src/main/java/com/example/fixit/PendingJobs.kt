package com.example.fixit

data class PendingJobs(val cusid:String = "",val jobid: String = "",val title:String = "",var jobDesc: String = "",val status: String = "pending") {

}
