package com.madinaapps.iarmasjid.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

sealed class AppDestination {
    @Keep
    @Serializable
    data object Prayer : AppDestination()

    @Keep
    @Serializable
    data object Qibla : AppDestination()

    @Keep
    @Serializable
    data object NewsTab : AppDestination()

    @Keep
    @Serializable
    data object News : AppDestination()

    @Keep
    @Serializable
    data object Donate : AppDestination()

    @Keep
    @Serializable
    data object More : AppDestination()

    @Keep
    @Serializable
    data class Web(
        val url: String,
        val title: String
    ) : AppDestination()
}

