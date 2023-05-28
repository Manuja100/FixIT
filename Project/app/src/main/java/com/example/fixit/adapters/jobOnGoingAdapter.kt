package com.example.fixit.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixit.R
import com.example.fixit.models.jobModel

class jobOnGoingAdapter (private val jobList: ArrayList<jobModel>) : RecyclerView.Adapter<jobOnGoingAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClickCompleteJob(position: Int)
        fun onItemClickViewMaps(position: Int)
    }

    fun setOnItemClickListner(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewJobSearch = LayoutInflater.from(parent.context).inflate(R.layout.jobongoinglistitem,parent,false)
        return ViewHolder(itemViewJobSearch,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.tvOnGoingJobAcceptedDate.text = currentJob.acceptedDate
        holder.tvOnGoingJobCusName.text = currentJob.cusName
        holder.tvOnGoingJobNumber.text = currentJob.number
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvOnGoingJobAcceptedDate : TextView = itemView.findViewById(R.id.tvOnGoingJobAcceptedDate)
        val tvOnGoingJobCusName : TextView = itemView.findViewById(R.id.tvOnGoingJobCusName)
        val tvOnGoingJobNumber : TextView = itemView.findViewById(R.id.tvOnGoingJobNumber)

        val comepleteBtn : Button = itemView.findViewById(R.id.onGoingCompleteBtn)
        val openMapBtn : Button = itemView.findViewById(R.id.onGoingMapBtn)

        init{
            comepleteBtn.setOnClickListener {
                clickListener.onItemClickCompleteJob(adapterPosition)
            }
            openMapBtn.setOnClickListener {
                clickListener.onItemClickViewMaps(adapterPosition)
            }
        }

    }

}