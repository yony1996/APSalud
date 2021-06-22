package com.APSalud.apsalud.model

/*
    "user": {
        "id": 4,
        "name": "jonathan",
        "email": "yony@gmail.com",
        "status": 1,
        "rol": "medico"
    },
* */
data class User(
    val id:Int,
    val name:String,
    val email:String,
    val status:Boolean,
    val rol:String
)