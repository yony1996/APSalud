package com.APSalud.apsalud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import  com.APSalud.apsalud.PreferenceHelper.get
import  com.APSalud.apsalud.PreferenceHelper.set


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val preferences=getSharedPreferences("general", MODE_PRIVATE)
        val session=preferences.getBoolean("session",false)
        */
        val preferences= PreferenceHelper.defaultPrefs(this)
        if (preferences["session",false]) {
            goToMenuActivity()
        }
        findViewById<Button>(R.id.BtnIngresar).setOnClickListener {
            //validar login
            createSessionPreference()
            goToMenuActivity()
        }

        findViewById<TextView>(R.id.gotoRegister).setOnClickListener {
            Toast.makeText(this, getString(R.string.fill_your_data), Toast.LENGTH_LONG).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMenuActivity() {
        val intent = Intent(this@MainActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference() {
        /*
        val preferences = getSharedPreferences("general", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session", true)
        editor.apply()
        */
        val preferences=PreferenceHelper.defaultPrefs(this)
        preferences["session"]=true

    }

}