package com.madinaapps.iarmasjid.model.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class News (
    val announcements: Announcements,
    val events: List<Event>
)