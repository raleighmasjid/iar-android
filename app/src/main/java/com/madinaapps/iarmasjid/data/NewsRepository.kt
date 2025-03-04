package com.madinaapps.iarmasjid.data

import com.madinaapps.iarmasjid.api.ApiClient
import com.madinaapps.iarmasjid.model.json.News
import java.util.Date

class NewsRepository(private val dataStoreManager: DataStoreManager) {

    suspend fun getCachedNews(): News? {
        val jsonString = dataStoreManager.getCachedNewsData() ?: return null
        return ApiClient.moshi.adapter(News::class.java).fromJson(jsonString)
    }

    suspend fun fetchNews(forceRefresh: Boolean): Result<News> {
        val currentCache = getCachedNews()
        if (currentCache != null && currentCache.isValidCache() && !forceRefresh) {
            return Result.success(currentCache)
        }

        try {
            val response = ApiClient.getNews()
            val news = response.body()
            if (!response.isSuccessful || news == null) {
                return Result.failure(Exception("Network Error"))
            }
            news.cacheTimestamp = Date().time
            val jsonString = ApiClient.moshi.adapter(News::class.java).toJson(news)
            dataStoreManager.cacheNewsData(jsonString)
            return Result.success(news)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}