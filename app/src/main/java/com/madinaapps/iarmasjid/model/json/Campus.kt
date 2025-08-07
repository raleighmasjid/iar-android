package com.madinaapps.iarmasjid.model.json

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