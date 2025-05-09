package com.madinaapps.iarmasjid.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.madinaapps.iarmasjid.model.LocationState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//private const val SIMULATE_LOCATION = true

@SuppressLint("MissingPermission")
@HiltViewModel
class QiblaViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var cacheTimestamp: Long? = null
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Pending)
    val locationState = _locationState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    fun forceRefresh() {
        _locationState.value = LocationState.Pending
        getCurrentLocation()
    }

    fun getCurrentLocation() {
        Log.d("IARDebug", "getCurrentLocation")
        if (isValidCache()) {
            Log.d("IARDebug", "using cached location")
            return
        }

//        if (SIMULATE_LOCATION) {
//            viewModelScope.launch {
//                withContext(Dispatchers.IO) {
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(5))
////                    _locationState.value = LocationState.Error
//                    val fakeLocation = Location("")
//                    fakeLocation.latitude = 40.77740854406346
//                    fakeLocation.longitude = -73.96895210200351
//                    updateLocation(fakeLocation)
//                }
//            }
//            return
//        }

        val cancellationToken = CancellationTokenSource().token
        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setMaxUpdateAgeMillis(TimeUnit.MINUTES.toMillis(15))
        fusedLocationClient.getCurrentLocation(locationRequest.build(), cancellationToken)
            .addOnSuccessListener {
                Log.d("IARDebug", "got location: $it")
                if (it != null) {
                    updateLocation(it)
                } else {
                    Log.d("IARDebug", "Error: location was null")
                    _locationState.value = LocationState.Error
                }
            }
            .addOnFailureListener {
                Log.d("IARDebug", "Error getting location")
                _locationState.value = LocationState.Error
            }
    }

    private fun isValidCache(): Boolean {
        if (_locationState.value !is LocationState.Valid) return false
        val cacheTime = cacheTimestamp ?: return false
        return Date().time - cacheTime < TimeUnit.MINUTES.toMillis(5)
    }

    private fun updateLocation(location: Location) {
        _locationState.value = LocationState.Valid(location, null)
        cacheTimestamp = Date().time
        fetchCityName(location)
    }

    private fun fetchCityName(location: Location) {
        geocode(location) {
            val cityName = it?.locality ?: it?.adminArea
            _locationState.value = LocationState.Valid(location, cityName)
        }
    }

    private fun geocode(location: Location, onResult: (Address?) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            onResult(addresses.firstOrNull())
                        }

                        override fun onError(errorMessage: String?) {
                            Log.e("IARDebug", "onError getting city name: $errorMessage")
                            onResult(null)
                        }
                    }
                )
            } catch (e: Exception) {
                onResult(null)
                Log.e("IARDebug", "Caught Error getting city name", e)
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        @Suppress("DEPRECATION")
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        onResult(addresses?.firstOrNull())
                    } catch (e: Exception) {
                        Log.e("IARDebug", "Error getting city name", e)
                        onResult(null)
                    }
                }
            }
        }
    }
}