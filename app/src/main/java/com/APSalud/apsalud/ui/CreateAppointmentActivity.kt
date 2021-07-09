package com.APSalud.apsalud.ui

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.APSalud.apsalud.R
import com.APSalud.apsalud.io.ApiService
import com.APSalud.apsalud.io.response.SimpleResponse
import com.APSalud.apsalud.model.Doctor
import com.APSalud.apsalud.model.Schedule
import com.APSalud.apsalud.model.Specialty
import com.APSalud.apsalud.util.PreferenceHelper
import com.APSalud.apsalud.util.PreferenceHelper.get
import com.APSalud.apsalud.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_three.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val selectedCalendar=Calendar.getInstance()
    private var selectedTimeRadioButton:RadioButton?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener {
            if(EtxtDescription.text.toString().length<3){
                EtxtDescription.error=getString(R.string.validate_descrption_appointment)
            }else{
                step1.visibility= View.GONE
                step2.visibility= View.VISIBLE
            }

        }

        btnNext2.setOnClickListener {

            when {
                EtxtScheduledDate.text.toString().isEmpty() -> {
                    EtxtScheduledDate.error=getString(R.string.validate_date)
                    Snackbar.make(createAppointment,getString(R.string.validate_date),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(
                        R.color.red
                    )).show()
                }
                selectedTimeRadioButton==null -> {
                    Snackbar.make(createAppointment,getString(R.string.validate_time),Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(
                        R.color.red
                    )).show()
                }
                else -> {
                    showAppointmentDataConfirm()
                    step2.visibility= View.GONE
                    step3.visibility= View.VISIBLE
                }
            }




        }

        ConfirmAppoimeintment.setOnClickListener {
            performStoreAppointment()
        }

        loadSpecialties()
        listenSpecialtyChanges()
        listenDoctorAndChanges()

    }
    private fun performStoreAppointment(){
        ConfirmAppoimeintment.isClickable=false
        val passport=preferences["passport",""]
        val authHeader="Bearer $passport"
        val description=tvConfirm_description.text.toString()
        val specialty=specialtySpiner.selectedItem as Specialty
        val doctor =doctorSpiner.selectedItem as Doctor
        val scheduledDate=tvConfirm_scheduled_date.text.toString()
        val scheduledTime=tvConfirm_scheduled_time.text.toString()
        val type=tvConfirm_type.text.toString()

        val call =apiService.storeAppointment(
            authHeader,description,
            specialty.id,doctor.id,
            scheduledDate,scheduledTime,
            type
        )
        call.enqueue(object : Callback<SimpleResponse>{
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful){
                    toast(getString(R.string.toast_message))
                    finish()
                }else{
                    toast(getString(R.string.Alert_error_stroreAppointment))
                    ConfirmAppoimeintment.isClickable=true
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                toast(t.localizedMessage)
                ConfirmAppoimeintment.isClickable=true
            }
        })

    }
    private fun listenDoctorAndChanges(){
        doctorSpiner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val doctor =adapter?.getItemAtPosition(position) as Doctor
                loadHours(doctor.id,EtxtScheduledDate.text.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        EtxtScheduledDate.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               val doctor=doctorSpiner.selectedItem as Doctor
                loadHours(doctor.id,EtxtScheduledDate.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }
    private fun loadHours(doctorId:Int,date:String){
        //Toast.makeText(this,"doctor:$doctorId ,date:$date",Toast.LENGTH_LONG).show()
        if( date.isEmpty()){
            return
        }
        val call=apiService.getHours(doctorId,date)
        call.enqueue(object :Callback<Schedule>{
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if(response.isSuccessful){
                    val schedule=response.body()

                    schedule?.let {
                        AlertInfo.visibility=View.GONE
                        val intervals=it.morning + it.afternoon
                        val hours=ArrayList<String>()
                        intervals.forEach { interval->
                            hours.add(interval.start)
                        }

                        displayIntervalRadio(hours)
                    }


                }
            }

            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                toast(getString(R.string.Fail_hours))
            }
        })



    }
    private fun loadSpecialties(){
        val call=apiService.getSpecialties()
        call.enqueue(object : Callback<ArrayList<Specialty>> {
            override fun onResponse(call: Call<ArrayList<Specialty>>, response: Response<ArrayList<Specialty>>) {
                if (response.isSuccessful){ //200...300
                    response.body()?.let {
                        val specialties=it.toMutableList()
                        specialtySpiner.adapter=ArrayAdapter<Specialty>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,specialties)
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
        specialtySpiner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val specialty= adapter?.getItemAtPosition(position) as Specialty
                loadDoctors(specialty.id)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

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
                        doctorSpiner.adapter=ArrayAdapter<Doctor>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,doctors)
                    }




                }
            }

            override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,getString(R.string.Alert_doctor),Toast.LENGTH_LONG).show()

            }
        } )


    }
    private fun showAppointmentDataConfirm(){



        val radiogroup =radioType.checkedRadioButtonId
        val selectedType=radioType.findViewById<RadioButton>(radiogroup)

        tvConfirm_description.text= EtxtDescription.text.toString()
        tvConfirm_type.text=selectedType.text.toString()
        tvConfirm_specialty.text=specialtySpiner.selectedItem.toString()
        tvConfirm_doctor.text=doctorSpiner.selectedItem.toString()
        tvConfirm_scheduled_date.text=EtxtScheduledDate.text.toString()
        tvConfirm_scheduled_time.text=selectedTimeRadioButton?.text.toString()


    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun onClickScheduleDate(v:View?){

        val year= selectedCalendar.get(Calendar.YEAR)
        val month= selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth=selectedCalendar.get(Calendar.DAY_OF_MONTH)


        val listener=DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            //Toast.makeText(this,"$y-$m-$d",Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y,m,d)
            EtxtScheduledDate.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            EtxtScheduledDate.error=null

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
    private fun  displayIntervalRadio( hours:ArrayList<String>){

        selectedTimeRadioButton=null
        radioGroupLeft.removeAllViews()
        radioGroupRight.removeAllViews()

        if(hours.isEmpty()){
            AlertDanger.visibility=View.VISIBLE

            return
        }
        AlertDanger.visibility=View.GONE

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
                radioGroupLeft.addView(radioButton)
            else
                radioGroupRight.addView(radioButton)
            goToLeft= !goToLeft

        }


    }
    private fun Int.twoDigits()=if(this>=10) this.toString() else "0$this"
    override fun onBackPressed() {
        when {
            step3.visibility==View.VISIBLE -> {
                step3.visibility=View.GONE
                step2.visibility=View.VISIBLE

            }

            step2.visibility==View.VISIBLE -> {
                step2.visibility=View.GONE
                step1.visibility=View.VISIBLE

            }

            step1.visibility==View.VISIBLE -> {
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