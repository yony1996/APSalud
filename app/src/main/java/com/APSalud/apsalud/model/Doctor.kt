package com.APSalud.apsalud.model

data class Doctor(val id:Int, val last_name:String){
    override fun toString(): String {
        return last_name
    }
}