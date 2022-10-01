package com.rakamin.newsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rakamin.newsapp.data.local.entity.NewsEntity
import com.rakamin.newsapp.data.local.room.NewsDao
import com.rakamin.newsapp.data.remote.retrofit.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
){
    fun getNews(category: String?): LiveData<Fetch<List<NewsEntity>>> = liveData {
        emit(Fetch.Loading)
        try {
            val response = apiService.getNews(category, "db874e166f4c473e9132d19a45135274")
            val articles = response.articles
            val newsList = articles.map { article ->
                val isBookmarked = newsDao.isNewsBookmarked(article.title)
                NewsEntity(
                    article.title,
                    article.publishedAt,
                    article.urlToImage,
                    article.description,
                    article.url,
                    isBookmarked
                )
            }
            newsDao.deleteAll()
            newsDao.insertNews(newsList)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Fetch.Error(e.message.toString()))
        }
        val localData: LiveData<Fetch<List<NewsEntity>>> = newsDao.getNews().map { Fetch.Success(it) }
        emitSource(localData)
    }

    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }

    suspend fun setNewsBookmark(news: NewsEntity, bookmarkState: Boolean) {
        news.bookmark = bookmarkState
        newsDao.updateNews(news)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao,
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}