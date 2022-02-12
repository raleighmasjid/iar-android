package org.raleighmasjid.iar.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FridaySchedule(
    val title: String,
    val shift: String,
    val time: String,
    val speaker: String,
    val description: String,
    @Json(name = "image_url")
    val imageUrl: String
)