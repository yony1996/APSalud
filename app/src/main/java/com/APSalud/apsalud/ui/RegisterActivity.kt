package com.APSalud.apsalud.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.io.response.LoginResponse
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.set
import com.APSalud.apsalud.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        gotoLogin.setOnClickListener {
            Toast.makeText(this,getString(R.string.fill_your_data), Toast.LENGTH_LONG).show()

            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener {
            performRegister()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun performRegister(){
        val name=EtRegisterUser.text.toString().trim()
        val email=EtRegisterEmail.text.toString().trim()
        val password=EtRegisterPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            Snackbar.make(registerLayout,getString(R.string.Alert_register),
                Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.red)).show()
            return
        }

        val call=apiService.postRegister(name,email,password)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse=response.body()
                    if (loginResponse== null){
                        toast(getString(R.string.Alert_login_response))
                        return
                    }
                    if (loginResponse.success){
                        createSessionPreference(loginResponse.passport)
                        Snackbar.make(findViewById<LinearLayout>(R.id.registerLayout),getString(R.string.welcome_name),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.green)).show()
                        //Snackbar.make(registerLayout,getString(R.string.welcome_name,loginResponse.user.name),Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.green)).show()
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_credentials))
                    }
                }else{
                    toast(getString(R.string.Alert_error_register))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
               toast(t.localizedMessage)
            }

        })

    }

    private fun goToMenuActivity() {
        val intent = Intent(this@RegisterActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(passport:String) {

        val preferences= PreferenceHelper.defaultPrefs(this@RegisterActivity)
        preferences["passport"]=passport

    }
}