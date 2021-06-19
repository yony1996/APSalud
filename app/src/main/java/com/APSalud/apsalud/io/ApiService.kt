package com.APSalud.apsalud.io

import com.APSalud.apsalud.io.response.LoginResponse
import com.APSalud.apsalud.model.Doctor
import com.APSalud.apsalud.model.Schedule
import com.APSalud.apsalud.model.Specialty
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("specialties")
    fun getSpecialties():Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialtyId:Int):Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId:Int,@Query("date") date:String):Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email:String,@Query("password") password:String):Call<LoginResponse>

    companion object Factory{
        private  const val BASE_URL="http://vast-lowlands-02402.herokuapp.com/api/"

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