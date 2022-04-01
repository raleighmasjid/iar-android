package com.madinaapps.iarmasjid.model.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Event (
    val id: Int,
    val title: String,
    val url: String,
    val description: String,
    val start: Date,
    val end: Date,
    @Json(name = "all_day")
    val allDay: Boolean,
    val repeating: Boolean
)