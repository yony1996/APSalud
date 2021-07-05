package com.APSalud.apsalud.model

import com.google.gson.annotations.SerializedName

data class Exam (
    val id:Int,
    val doctor:Doctor,
    @SerializedName("created_at") val createdAt:String
    )