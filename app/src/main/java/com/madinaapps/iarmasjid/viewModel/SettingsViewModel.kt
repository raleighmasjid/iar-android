package com.madinaapps.iarmasjid.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {
    val notificationType: StateFlow<NotificationType> = dataStoreManager
        .getNotificationType()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationType.SILENT
        )

    fun setNotificationType(type: NotificationType) {
        viewModelScope.launch {
            dataStoreManager.setNotificationType(type)
        }
    }

    fun getNotificationEnabled(prayer: Prayer): StateFlow<Boolean> {
        return dataStoreManager
            .getNotificationEnabled(prayer)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
    }

    fun setNotification(enabled: Boolean, prayer: Prayer) {
        viewModelScope.launch {
            dataStoreManager.setNotification(
                enabled = enabled,
                prayer = prayer
            )
        }
    }
}