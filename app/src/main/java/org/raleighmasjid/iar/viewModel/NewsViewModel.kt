package org.raleighmasjid.iar.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.data.NewsRepository
import org.raleighmasjid.iar.model.json.News
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    var news by mutableStateOf<News?>(null)

    var error by mutableStateOf(false)

    var loading by mutableStateOf(false)

    private val repository = NewsRepository(dataStoreManager)

    fun fetchLatest() {
        loading = true
        viewModelScope.launch {

            val cached = repository.getCachedNews()
            if (cached != null) {
                news = cached
            }

            val scheduleResult = repository.fetchNews()
            scheduleResult.onSuccess {
                news = it
            }.onFailure {
                error = true
            }
            loading = false
        }
    }
}