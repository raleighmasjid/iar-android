package com.madinaapps.iarmasjid.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.NewsRepository
import com.madinaapps.iarmasjid.model.json.Announcements
import com.madinaapps.iarmasjid.model.json.Event
import com.madinaapps.iarmasjid.model.json.News
import com.madinaapps.iarmasjid.utils.asLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    var announcements by mutableStateOf<Announcements?>(null)
        private set

    var events = mutableStateMapOf<LocalDate, List<Event>>()
        private set

    var error by mutableStateOf(false)

    var loading by mutableStateOf(false)

    var showBadge by mutableStateOf(false)

    private val repository = NewsRepository(dataStoreManager)

    fun fetchLatest() {
        loading = true
        viewModelScope.launch {

            val cached = repository.getCachedNews()
            if (cached != null) {
                updateNews(cached)
            }

            val scheduleResult = repository.fetchNews()
            scheduleResult.onSuccess {
                updateNews(it)
            }.onFailure {
                error = true
            }
            loading = false
        }
    }

    fun didViewAnnouncements() {
        viewModelScope.launch {
            val postIds = announcements?.postIds()
            if (postIds != null) {
                dataStoreManager.setViewedAnnouncments(postIds)
                updateBadge()
            }
        }
    }

    private fun updateBadge() {
        viewModelScope.launch {
            val viewedIds = dataStoreManager.getViewedAnnouncments().first()
            val currentIds = announcements?.postIds() ?: emptySet()
            val unviewedIds = currentIds.subtract(viewedIds)
            showBadge = unviewedIds.count() > 0
        }
    }

    private fun updateNews(news: News) {
        events.apply {
            clear()
            val groups = news.events.groupBy { it.start.asLocalDate() }
            putAll(groups)
        }
        announcements = news.announcements

        updateBadge()
    }
}