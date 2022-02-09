package org.raleighmasjid.iar.model

import com.squareup.moshi.Json

data class Hijri(
    val day: Int,
    @Json(name = "month_numeric")
    val month: Int,
    val year: Int,
    @Json(name = "month")
    val monthName: String
) {
    fun fomatted(): String {
        return "$monthName $day, $year h"
    }
}