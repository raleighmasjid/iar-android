package org.raleighmasjid.iar.data

import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.model.json.News

class NewsRepository(private val dataStoreManager: DataStoreManager) {

    suspend fun getCachedNews(): News? {
        val jsonString = dataStoreManager.getCachedNewsData() ?: return null
        return ApiClient.moshi.adapter(News::class.java).fromJson(jsonString)
    }

    suspend fun fetchNews(): Result<News> {
        try {
            val response = ApiClient.getNews()
            val news = response.body()
            if (!response.isSuccessful || news == null) {
                return Result.failure(Exception("Network Error"))
            }
            val jsonString = ApiClient.moshi.adapter(News::class.java).toJson(news)
            dataStoreManager.cacheNewsData(jsonString)
            return Result.success(news)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}