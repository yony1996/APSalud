package com.APSalud.apsalud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<TextView>(R.id.gotoLogin).setOnClickListener {
            Toast.makeText(this,getString(R.string.fill_your_data), Toast.LENGTH_LONG).show()

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}