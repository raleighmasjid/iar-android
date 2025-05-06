package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.util.Date

@Keep
@JsonClass(generateAdapter = true)
data class Post(
    val id: Int,
    val title: String,
    val date: Date,
    val url: String,
    val text: String,
    val image: String?
)