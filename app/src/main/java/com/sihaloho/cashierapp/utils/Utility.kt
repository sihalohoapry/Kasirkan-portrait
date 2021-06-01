package com.sihaloho.cashierapp.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

fun idrFormat(number: Int): String{
    val decimalFormat: NumberFormat = DecimalFormat("#,###")
    return decimalFormat.format(number)
}
fun convertTanggal(tgl: String, formatBaru: String, formatLama: String): String{
    val dateFormat = SimpleDateFormat(formatLama)
    val convert = dateFormat.parse(tgl)
    dateFormat.applyPattern(formatBaru)
    return dateFormat.format(convert)
}