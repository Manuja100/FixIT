package com.example.fixit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PendingJobsAdapter(private val userList: ArrayList<PendingJobs>) :
    RecyclerView.Adapter<PendingJobsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item,parent,false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = userList[position]

        holder.status.text = currentitem.status ?: ""
        holder.title.text = currentitem.title ?: ""
        holder.jobDesc.text = currentitem.jobDesc ?: ""
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.recStatus)
        val title:TextView = itemView.findViewById(R.id.recTitle)
        var jobDesc: TextView = itemView.findViewById(R.id.recDec)
    }
}