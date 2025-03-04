package com.madinaapps.iarmasjid.model.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class News (
    val announcements: Announcements,
    val events: List<Event>,
    @Json(name = "cache_timestamp")
    var cacheTimestamp: Long?
) {
    fun isValidCache(): Boolean {
        val cacheTime = cacheTimestamp ?: return false
        return Date().time - cacheTime < (5 * 60 * 1000)
    }
}