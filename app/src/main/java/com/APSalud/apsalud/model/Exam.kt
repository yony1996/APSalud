package com.APSalud.apsalud.model

import com.google.gson.annotations.SerializedName

data class Exam (
    val id:Int,
    val doctorName:String,
    @SerializedName("created_at") val createdAt:String
    )