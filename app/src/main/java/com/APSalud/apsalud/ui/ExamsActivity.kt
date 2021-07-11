package com.APSalud.apsalud.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.model.Appointment
import com.APSalud.apsalud.model.Exam
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.get
import com.APSalud.apsalud.util.toast
import kotlinx.android.synthetic.main.activity_appointments.*
import kotlinx.android.synthetic.main.activity_exams.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExamsActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val examAdapter=ExamAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)

        loadExams()


        rvExams.layoutManager= LinearLayoutManager(this)
        rvExams.adapter= examAdapter
    }

    private fun loadExams(){
        val passport=preferences["passport",""]
        val call=apiService.getExams("Bearer $passport")
        call.enqueue(object : Callback<ArrayList<Exam>> {
            override fun onResponse(
                call: Call<ArrayList<Exam>>,
                response: Response<ArrayList<Exam>>
            ) {
                if(response.body()?.isEmpty() == true){
                    alertExam() 
                }
                if (response.isSuccessful){

                    response.body()?.let {
                        examAdapter.exams=it
                        examAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Exam>>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })


    }

    private fun alertExam(){
        val builder=  AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage("Aun no hay examenes registrados")
        builder.setIcon(android.R.drawable.ic_dialog_info)
        builder.setPositiveButton("OK"){ _, _->
            finish()
        }

        val dialog=builder.create()
        dialog.show()
    }
}