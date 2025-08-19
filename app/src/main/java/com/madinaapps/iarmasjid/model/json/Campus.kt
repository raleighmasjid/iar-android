package com.madinaapps.iarmasjid.model.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class Campus {
    atwater,
    page;

    fun campusName(): String {
        return when (this) {
            atwater -> "Atwater St"
            page -> "Page Rd"
        }
    }
}