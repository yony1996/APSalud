package com.APSalud.apsalud.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.model.Appointment
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.get
import com.APSalud.apsalud.util.toast
import kotlinx.android.synthetic.main.activity_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsActivity : AppCompatActivity() {

    private val apiService:ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val appointmentAdapter=AppointmentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        loadAppointments()
        //aqui swipe refresh
        rvAppointment.layoutManager=LinearLayoutManager(this)
        rvAppointment.adapter= appointmentAdapter
    }

    private fun loadAppointments(){
        val passport=preferences["passport",""]
        val call=apiService.getAppointments("Bearer $passport")

        call.enqueue(object : Callback<ArrayList<Appointment>>{
            override fun onResponse(
                call: Call<ArrayList<Appointment>>,
                response: Response<ArrayList<Appointment>>
            ) {
                if(response.body()?.isEmpty() == true){
                    alertAppoinment()
                }
               if (response.isSuccessful){
                   response.body()?.let {
                       appointmentAdapter.appointments=it
                       appointmentAdapter.notifyDataSetChanged()
                   }

               }
            }

            override fun onFailure(call: Call<ArrayList<Appointment>>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }

    private fun alertAppoinment(){
        val builder=  AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage("Aun no hay citas reservadas")
        builder.setIcon(android.R.drawable.ic_dialog_info)
        builder.setPositiveButton("OK"){ _, _->
            finish()
        }

        val dialog=builder.create()
        dialog.show()
    }
}