package com.sihaloho.cashierapp.utils

import java.util.*

class CalendarUtils {
    companion object{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
    }
}