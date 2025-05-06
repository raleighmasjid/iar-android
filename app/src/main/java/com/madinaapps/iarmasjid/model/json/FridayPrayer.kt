package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class FridayPrayer(
    val title: String,
    val shift: String,
    val time: String,
    val speaker: String,
    val description: String,
    @Json(name = "image_url")
    val imageUrl: String
)