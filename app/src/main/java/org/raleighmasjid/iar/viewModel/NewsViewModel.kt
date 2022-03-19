package org.raleighmasjid.iar.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.data.NewsRepository
import org.raleighmasjid.iar.model.json.Announcement
import org.raleighmasjid.iar.model.json.Event
import org.raleighmasjid.iar.model.json.News
import org.raleighmasjid.iar.model.json.SpecialAnnouncement
import org.raleighmasjid.iar.utils.formatToDay
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    var special by mutableStateOf<SpecialAnnouncement?>(null)

    var featured by mutableStateOf<Announcement?>(null)

    var announcements = mutableStateListOf<Announcement>()
        private set

    var events = mutableStateMapOf<String, List<Event>>()
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

    fun setViewedSpecial(id: Int) {
        viewModelScope.launch {
            dataStoreManager.setViewedSpecial(id)
            updateBadge()
        }
    }

    private fun updateBadge() {
        viewModelScope.launch {
            showBadge = dataStoreManager.getViewedSpecial().first() != special?.id
        }
    }

    private fun updateNews(news: News) {
        announcements.apply {
            clear()
            addAll(news.announcements)
        }
        events.apply {
            clear()
            val groups = news.events.groupBy { it.start.formatToDay() }
            putAll(groups)
        }
        special = news.special
        featured = news.featured

        updateBadge()
    }
}