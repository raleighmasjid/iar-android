package com.madinaapps.iarmasjid.navigation

import kotlinx.serialization.Serializable

sealed class AppDestination {
    @Serializable
    data object Prayer : AppDestination()

    @Serializable
    data object Qibla : AppDestination()

    @Serializable
    data object News : AppDestination()

    @Serializable
    data object NewsGraph : AppDestination()

    @Serializable
    data object Donate : AppDestination()

    @Serializable
    data object More : AppDestination()

    @Serializable
    data class Web(
        val url: String,
        val title: String
    ) : AppDestination()
}

