package com.dzulfikar.usersapp.utils

import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    fun checkInput(input: EditText?) : Boolean{
        return when(input?.text?.isEmpty()){
            true -> {
                input.error = "Must Be Filled"
                false
            }
            else -> true
        }
    }

    fun epochToDateConverter(epoch: Long) : String{
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(epoch)).toString()
    }

}