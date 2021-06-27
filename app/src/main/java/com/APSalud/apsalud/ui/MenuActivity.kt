package com.APSalud.apsalud.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.set
import com.APSalud.apsalud.util.PreferenceHelper.get
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.util.toast
import kotlinx.android.synthetic.main.activity_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        MakeAppoinment.setOnClickListener {
            val intent=Intent(this@MenuActivity, CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        MyAppoinment.setOnClickListener {
            val intent=Intent(this@MenuActivity, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        Logout.setOnClickListener {
            performLogout()


        }

        MyExams.setOnClickListener {
            val intent=Intent(this@MenuActivity, ExamsActivity::class.java)
            startActivity(intent)
        }

    }
    private fun performLogout(){
        val passport=preferences["passport",""]
        val call=apiService.postLogout("Bearer $passport")
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()

                val intent=Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }
    private fun clearSessionPreference(){

        preferences["passport"]=""
    }
}