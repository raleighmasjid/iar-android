package org.raleighmasjid.iar.model

import com.google.gson.annotations.SerializedName

data class Hijri(
    val day: Int,
    @SerializedName("month_numeric")
    val month: Int,
    val year: Int,
    @SerializedName("month")
    val monthName: String
) {
    fun fomatted(): String {
        return "$monthName $day, $year h"
    }
}