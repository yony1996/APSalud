package com.APSalud.apsalud.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.APSalud.apsalud.PreferenceHelper
import  com.APSalud.apsalud.PreferenceHelper.get
import  com.APSalud.apsalud.PreferenceHelper.set
import com.APSalud.apsalud.R
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private val snackBar by lazy {
        Snackbar.make(findViewById<LinearLayout>(R.id.mainLayout),getString(R.string.press_back_again),Snackbar.LENGTH_SHORT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val preferences= PreferenceHelper.defaultPrefs(this)
        if (preferences["session",false]) {
            goToMenuActivity()
        }
        findViewById<Button>(R.id.BtnIngresar).setOnClickListener {

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
        val preferences= PreferenceHelper.defaultPrefs(this)
        preferences["session"]=true

    }

    override fun onBackPressed() {
        if(snackBar.isShown)
            super.onBackPressed()
        else
            snackBar.show()

    }

}