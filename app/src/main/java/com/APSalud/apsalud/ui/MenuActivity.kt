package com.APSalud.apsalud.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.APSalud.apsalud.PreferenceHelper
import com.APSalud.apsalud.PreferenceHelper.set
import com.APSalud.apsalud.R


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViewById<View>(R.id.MakeAppoinment).setOnClickListener {
            val intent=Intent(this@MenuActivity, CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.MyAppoinment).setOnClickListener {
            val intent=Intent(this@MenuActivity, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.Logout).setOnClickListener {
            clearSessionPreference()
            val intent=Intent(this@MenuActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun clearSessionPreference(){
        val preferences= PreferenceHelper.defaultPrefs(this)
        preferences["session"]=false
    }
}