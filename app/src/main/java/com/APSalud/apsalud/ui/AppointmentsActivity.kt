package com.APSalud.apsalud.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.APSalud.apsalud.R
import com.APSalud.apsalud.model.Appointment
import kotlinx.android.synthetic.main.activity_appointments.*

class AppointmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val appointments=ArrayList<Appointment>()
        appointments.add(
            Appointment(1,"Medico 1","2021/06/03","3:00 PM")

        )
        appointments.add(
            Appointment(2,"Medico 2","2021/06/06","5:00 PM")

        )
        appointments.add(
            Appointment(3,"Medico 3","2021/06/10","4:00 PM")

        )
        appointments.add(
            Appointment(4,"Medico 4","2021/06/13","2:00 PM")

        )
        rvAppointment.layoutManager=LinearLayoutManager(this)
        rvAppointment.adapter= AppointmentAdapter(appointments)
    }
}