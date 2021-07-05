package com.APSalud.apsalud.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.APSalud.apsalud.R
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.get

class SplashScreen : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            verification()
        }, 3000)
    }


    private fun verification(){
        val preferences= PreferenceHelper.defaultPrefs(this)
        if (preferences["passport",""].contains(".")) {
            goToMenuActivity()
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}