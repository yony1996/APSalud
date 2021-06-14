package com.APSalud.apsalud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.APSalud.apsalud.model.Appointment
import java.util.ArrayList

class AppointmentAdapter(private val appointments:ArrayList<Appointment>)
    : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {


    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        fun bind(appointment: Appointment)=with(itemView){
                findViewById<TextView>(R.id.tvAppointmentId).text=
                    context.getString(R.string.item_appointment_id,appointment.id)
                findViewById<TextView>(R.id.tvDoctorName).text=appointment.doctorName
                findViewById<TextView>(R.id.tvScheduledDate).text=
                    context.getString(R.string.item_appointment_date,appointment.scheduledDate)
                findViewById<TextView>(R.id.tvScheduledTime).text=
                    context.getString(R.string.item_appointment_time,appointment.scheduledTime)
        }

    }
    //Inflates XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment,parent,false)
        )
    }
    //Bind Data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val appointment=appointments[position]
        holder.bind(appointment)

    }

    //Number of elements
    override fun getItemCount()=appointments.size

}