package org.raleighmasjid.iar.model.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class News (
    val special: SpecialAnnouncement?,
    val announcements: List<Announcement>,
    val events: List<Event>
)