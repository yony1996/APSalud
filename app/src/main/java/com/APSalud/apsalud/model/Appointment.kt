package com.APSalud.apsalud.model

import com.google.gson.annotations.SerializedName

/*
* "id": 1,
        "description": "dolor de cabeza",
        "scheduled_date": "2021-05-11",
        "type": "Consulta",
        "created_at": "2021-05-10T03:29:42.000000Z",
        "status": "Cancelada",
        "scheduled_time_12": "8:00 AM",
        "specialty": {
            "id": 2,
            "name": "cardiologia"
        },
        "doctor": {
            "id": 2,
            "last_name": "venegas"
        }
    },
*  */
data class Appointment(
    val id:Int,
    val description:String,
    val type:String,
    val status:String,

    @SerializedName("scheduled_date") val scheduledDate:String,
    @SerializedName("scheduled_time_12") val scheduledTime:String,
    @SerializedName("created_at") val createdAt:String,

    val specialty:Specialty,
    val doctor:Doctor
    )