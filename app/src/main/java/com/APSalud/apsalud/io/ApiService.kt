package com.APSalud.apsalud.io

import com.APSalud.apsalud.io.response.LoginResponse
import com.APSalud.apsalud.io.response.SimpleResponse
import com.APSalud.apsalud.model.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("specialties")
    fun getSpecialties():Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialtyId:Int):Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId:Int,@Query("date") date:String):Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email:String,@Query("password") password:String):Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String):Call<Void>

    @POST("register")
    @Headers("Accept:application/json")
    fun postRegister(
        @Query("name") name:String,
        @Query("email") email:String,
        @Query("password") password:String,
    ):Call<LoginResponse>

    @GET("appointments")
    fun getAppointments(@Header("Authorization") authHeader: String):Call<ArrayList<Appointment>>

    @POST("appointments")
    @Headers("Accept:application/json")
    fun storeAppointment(@Header("Authorization") authHeader: String,
                         @Query("description") description:String,
                         @Query("specialty_id") specialtyId:Int,
                         @Query("doctor_id") doctorId:Int,
                         @Query( "scheduled_date")scheduledDate:String,
                         @Query("scheduled_time") scheduledTime:String,
                         @Query("type") type:String

                         ):Call<SimpleResponse>

    @GET("exams")
    fun getExams(@Header("Authorization") authHeader: String):Call<ArrayList<Exam>>



    companion object Factory{
        private  const val BASE_URL="https://apsalud.life/api/"

        fun create():ApiService{
            val interceptor=HttpLoggingInterceptor()
            interceptor.level=HttpLoggingInterceptor.Level.BODY
            val client=OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit= Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


            return retrofit.create(ApiService::class.java)
        }
    }
}