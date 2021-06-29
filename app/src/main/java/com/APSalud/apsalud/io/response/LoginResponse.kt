package com.APSalud.apsalud.io.response

import com.APSalud.apsalud.model.User

data class LoginResponse(
    val success:Boolean,
    val user: User,
    val passport:String)