package com.madinaapps.iarmasjid.model

import android.location.Location

sealed class LocationState {
    data class Valid(
        val location: Location,
        val cityName: String?
    ) : LocationState()

    data object Error : LocationState()
    data object Pending : LocationState()

    fun cityName(): String? {
        return when (this) {
            is Valid -> {
                cityName
            }
            else -> null
        }
    }
}