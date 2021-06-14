package com.APSalud.apsalud

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import java.util.*

class CreateAppointmentActivity : AppCompatActivity() {


    private val selectedCalendar=Calendar.getInstance()
    private var selectedRadioButton:RadioButton?=null


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

        findViewById<Button>(R.id.ConfirmAppoimeintment).setOnClickListener {
            Toast.makeText(this,getString(R.string.toast_message),Toast.LENGTH_SHORT).show()
            finish()
        }
        val specialties= arrayListOf("specialty A","specialty B","specialty C")
        findViewById<Spinner>(R.id.specialtySpiner).adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,specialties)

        val doctors= arrayListOf("doctor A","doctor B","doctor C")
        findViewById<Spinner>(R.id.doctorSpiner).adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,doctors)



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

        selectedRadioButton=null
        findViewById<LinearLayout>(R.id.radioGroupLeft).removeAllViews()
        findViewById<LinearLayout>(R.id.radioGroupRight).removeAllViews()

        val hours= arrayOf("3:00 PM","3:30 PM","4:00 PM","4:30 PM","5:00 PM","5:30 PM")

        var goToLeft=true

        hours.forEach {

            val radioButton=RadioButton(this)
            radioButton.id=View.generateViewId()
            radioButton.text=it

            radioButton.setOnClickListener { view ->
                selectedRadioButton?.isChecked=false

                selectedRadioButton= view as RadioButton?
                selectedRadioButton?.isChecked=true
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
        if(findViewById<CardView>(R.id.step2).visibility==View.VISIBLE){
            findViewById<CardView>(R.id.step2).visibility=View.GONE
            findViewById<CardView>(R.id.step1).visibility=View.VISIBLE

        }else if(findViewById<CardView>(R.id.step1).visibility==View.VISIBLE){
            val builder=  AlertDialog.Builder(this)
            builder.setTitle(getString( R.string.dialog_title))
            builder.setMessage(getString(R.string.dialog_message))
            builder.setPositiveButton(getString(R.string.dialog_button_yes)){ _,_->
                finish()
            }
            builder.setNegativeButton(getString(R.string.dialog_button_continue)){dialog,_->
                dialog.dismiss()
            }
            val dialog=builder.create()
            dialog.show()
        }


    }

}