package com.example.fixit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixit.R
import com.example.fixit.models.jobModel

class jobSearchAdapter (private val jobList: ArrayList<jobModel>) : RecyclerView.Adapter<jobSearchAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener



    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewJobSearch = LayoutInflater.from(parent.context).inflate(R.layout.joblistitem,parent,false)
        return ViewHolder(itemViewJobSearch,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.tvJobDisplayItemJobType.text = currentJob.title
        holder.tvJobDisplayItemLocation.text = currentJob.location
        holder.tvJobDisplayItemCustomerName.text = currentJob.cusName
        holder.tvJobDisplayItemDesc.text = currentJob.jobDesc
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvJobDisplayItemJobType : TextView = itemView.findViewById(R.id.tvJobDisplayItemJobType)
        val tvJobDisplayItemLocation : TextView = itemView.findViewById(R.id.tvJobDisplayItemLocation)
        val tvJobDisplayItemCustomerName : TextView = itemView.findViewById(R.id.tvJobDisplayItemCustomerName)
        val tvJobDisplayItemDesc : TextView = itemView.findViewById(R.id.tvJobDisplayItemDesc)

        val acceptBtn : Button = itemView.findViewById(R.id.jobSearchAcceptBtn)

        init{
            acceptBtn.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}