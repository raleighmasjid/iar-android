package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
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