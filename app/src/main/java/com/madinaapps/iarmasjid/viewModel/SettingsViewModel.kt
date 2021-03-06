package com.madinaapps.iarmasjid.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.NotificationType
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {
    fun currentNotificationType(): NotificationType {
        return runBlocking { dataStoreManager.getNotificationType().first() }
    }
}