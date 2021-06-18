package com.APSalud.apsalud.ui

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.model.Doctor
import com.APSalud.apsalud.model.Specialty
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    private val selectedCalendar=Calendar.getInstance()
    private var selectedTimeRadioButton:RadioButton?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if(findViewById<EditText>(R.id.EtxtDescription).text.toString().length<3){
                findViewById<EditText>(R.id.EtxtDescription).error=getString(R.string.validate_descrption_appointment)
            }else{
                findViewById<CardView>(R.id.step1).visibility= View.GONE
                findViewById<CardView>(R.id.step2).visibility= View.VISIBLE
            }

        }

        findViewById<Button>(R.id.btnNext2).setOnClickListener {

            when {
                findViewById<EditText>(R.id.EtxtScheduledDate).text.toString().isEmpty() -> {
                    findViewById<EditText>(R.id.EtxtScheduledDate).error=getString(R.string.validate_date)
                    Snackbar.make(findViewById<ConstraintLayout>(R.id.createAppointment),getString(R.string.validate_date),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(
                        R.color.red
                    )).show()
                }
                selectedTimeRadioButton==null -> {
                    Snackbar.make(findViewById<ConstraintLayout>(R.id.createAppointment),getString(R.string.validate_time),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(
                        R.color.red
                    )).show()
                }
                else -> {
                    showAppointmentDataConfirm()
                    findViewById<CardView>(R.id.step2).visibility= View.GONE
                    findViewById<CardView>(R.id.step3).visibility= View.VISIBLE
                }
            }




        }

        findViewById<Button>(R.id.ConfirmAppoimeintment).setOnClickListener {
            Toast.makeText(this,getString(R.string.toast_message),Toast.LENGTH_SHORT).show()
            finish()
        }

        loadSpecialties()
        listenSpecialtyChanges()




    }
    private fun loadSpecialties(){
        val call=apiService.getSpecialties()
        call.enqueue(object : Callback<ArrayList<Specialty>> {
            override fun onResponse(call: Call<ArrayList<Specialty>>, response: Response<ArrayList<Specialty>>) {
                 if (response.isSuccessful){ //200...300
                     response.body()?.let {
                         val specialties=it.toMutableList()
                         findViewById<Spinner>(R.id.specialtySpiner).adapter=ArrayAdapter<Specialty>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,specialties)
                     }



                 }
            }

            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,getString(R.string.Alert_specialty),Toast.LENGTH_LONG).show()
                finish()
            }

        })

    }
    private fun listenSpecialtyChanges(){
        findViewById<Spinner>(R.id.specialtySpiner).onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val specialty= adapter?.getItemAtPosition(position) as Specialty
                loadDoctors(specialty.id)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun loadDoctors(specialtyId:Int){
        val call=apiService.getDoctors(specialtyId)
        call.enqueue(object: Callback<ArrayList<Doctor>>{
            override fun onResponse(call: Call<ArrayList<Doctor>>,response: Response<ArrayList<Doctor>>) {
                if (response.isSuccessful){ //200...300
                    response.body()?.let {
                        val doctors=it.toMutableList()
                        findViewById<Spinner>(R.id.doctorSpiner).adapter=ArrayAdapter<Doctor>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,doctors)
                    }



                }
            }

            override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,getString(R.string.Alert_doctor),Toast.LENGTH_LONG).show()

            }
        } )
    }

    private fun showAppointmentDataConfirm(){
        findViewById<TextView>(R.id.tvConfirm_description).text= findViewById<EditText>(R.id.EtxtDescription).text.toString()
        findViewById<TextView>(R.id.tvConfirm_type).text=findViewById<Spinner>(R.id.specialtySpiner).selectedItem.toString()

        val radiogroup =findViewById<RadioGroup>(R.id.radioType).checkedRadioButtonId
        val selectedType=findViewById<RadioGroup>(R.id.radioType).findViewById<RadioButton>(radiogroup)

        findViewById<TextView>(R.id.tvConfirm_specialty).text=selectedType.text.toString()
        findViewById<TextView>(R.id.tvConfirm_doctor).text=findViewById<Spinner>(R.id.doctorSpiner).selectedItem.toString()
        findViewById<TextView>(R.id.tvConfirm_scheduled_date).text=findViewById<EditText>(R.id.EtxtScheduledDate).text.toString()


        findViewById<TextView>(R.id.tvConfirm_scheduled_time).text=selectedTimeRadioButton?.text.toString()


    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun onClickScheduleDate(v:View?){

        val year= selectedCalendar.get(Calendar.YEAR)
        val month= selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth=selectedCalendar.get(Calendar.DAY_OF_MONTH)


        val listener=DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            //Toast.makeText(this,"$y-$m-$d",Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y,m,d)
            findViewById<EditText>(R.id.EtxtScheduledDate).setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    m.twoDigits(),
                    d.twoDigits()
                )
            )
            findViewById<EditText>(R.id.EtxtScheduledDate).error=null
            displayRadioButtons()
        }


        //new Dialog
        val datePickerDialog=DatePickerDialog(this,listener,year,month,dayOfMonth)

        //set datepicker
        val datePicker=datePickerDialog.datePicker
        val calendar=Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,1)
        //min date
        datePicker.minDate= calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH,29)
        datePicker.maxDate=calendar.timeInMillis
        //show dialog
        datePickerDialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun  displayRadioButtons(){

        selectedTimeRadioButton=null
        findViewById<LinearLayout>(R.id.radioGroupLeft).removeAllViews()
        findViewById<LinearLayout>(R.id.radioGroupRight).removeAllViews()

        val hours= arrayOf("3:00 PM","3:30 PM","4:00 PM","4:30 PM","5:00 PM","5:30 PM")

        var goToLeft=true

        hours.forEach {

            val radioButton=RadioButton(this)
            radioButton.id=View.generateViewId()
            radioButton.text=it

            radioButton.setOnClickListener { view ->
                selectedTimeRadioButton?.isChecked=false

                selectedTimeRadioButton= view as RadioButton?
                selectedTimeRadioButton?.isChecked=true
            }

            if (goToLeft)
                findViewById<LinearLayout>(R.id.radioGroupLeft).addView(radioButton)
            else
                findViewById<LinearLayout>(R.id.radioGroupRight).addView(radioButton)
            goToLeft= !goToLeft

        }


    }

    private fun Int.twoDigits()=if(this>=10) this.toString() else "0$this"

    override fun onBackPressed() {
        when {
            findViewById<CardView>(R.id.step3).visibility==View.VISIBLE -> {
                findViewById<CardView>(R.id.step3).visibility=View.GONE
                findViewById<CardView>(R.id.step2).visibility=View.VISIBLE

            }

            findViewById<CardView>(R.id.step2).visibility==View.VISIBLE -> {
                findViewById<CardView>(R.id.step2).visibility=View.GONE
                findViewById<CardView>(R.id.step1).visibility=View.VISIBLE

            }

            findViewById<CardView>(R.id.step1).visibility==View.VISIBLE -> {
                val builder=  AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_title))
                builder.setMessage(getString(R.string.dialog_message))
                builder.setPositiveButton(getString(R.string.dialog_button_yes)){ _, _->
                    finish()
                }
                builder.setNegativeButton(getString(R.string.dialog_button_continue)){ dialog, _->
                    dialog.dismiss()
                }
                val dialog=builder.create()
                dialog.show()
            }
        }


    }


}