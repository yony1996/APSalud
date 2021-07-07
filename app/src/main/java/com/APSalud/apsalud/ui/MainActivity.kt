package com.APSalud.apsalud.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.APSalud.apsalud.util.PreferenceHelper
import  com.APSalud.apsalud.util.PreferenceHelper.get
import  com.APSalud.apsalud.util.PreferenceHelper.set
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.io.response.LoginResponse
import com.APSalud.apsalud.util.toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val apiService:ApiService by lazy {
        ApiService.create()
    }
    private val snackBar by lazy {
        Snackbar.make(mainLayout,getString(R.string.press_back_again),Snackbar.LENGTH_SHORT)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val preferences= PreferenceHelper.defaultPrefs(this)
        if (preferences["passport",""].contains(".")) {
            goToMenuActivity()
        }

        BtnIngresar.setOnClickListener {
            performLogin()
        }

        gotoRegister.setOnClickListener {
            Toast.makeText(this, getString(R.string.fill_your_data), Toast.LENGTH_LONG).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun performLogin(){

        val email=EtEmail.text.toString()
        val password=EtPassword.text.toString()

        if (email.trim().isEmpty() || password.trim().isEmpty()){
            Snackbar.make(mainLayout,getString(R.string.Alert_empty_text_login),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.orange)).show()
            return
        }
       val call= apiService.postLogin(email,password)
        call.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse=response.body()
                    if (loginResponse== null){
                        toast(getString(R.string.Alert_login_response))
                        return
                    }
                    if (loginResponse.success){
                        createSessionPreference(loginResponse.passport)
                        Snackbar.make(mainLayout,getString(R.string.welcome_name,loginResponse.user.name),Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.green)).show()
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_credentials))
                    }
                }else{
                    toast(getString(R.string.Alert_login_response))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
              toast(t.localizedMessage)
            }
        })
    }

   private fun goToMenuActivity() {
        val intent = Intent(this@MainActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(passport:String) {

        val preferences= PreferenceHelper.defaultPrefs(this)
        preferences["passport"]=passport

    }

    override fun onBackPressed() {
        if(snackBar.isShown)
            super.onBackPressed()
        else
            snackBar.show()

    }

}