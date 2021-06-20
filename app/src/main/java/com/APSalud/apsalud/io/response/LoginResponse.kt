package com.APSalud.apsalud.io.response

import com.APSalud.apsalud.model.User
import com.APSalud.apsalud.model.UserInformation

data class LoginResponse(
    val success:Boolean,
    val user: User,
    val info:UserInformation,
    val passport:String)