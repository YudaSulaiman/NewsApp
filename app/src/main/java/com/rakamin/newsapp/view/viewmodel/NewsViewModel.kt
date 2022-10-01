package com.rakamin.newsapp.view.viewmodel

import androidx.lifecycle.*
import com.rakamin.newsapp.data.NewsRepository
import com.rakamin.newsapp.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    fun getNews(category: String?) = newsRepository.getNews(category)

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()

    fun saveNews(news: NewsEntity) {
        viewModelScope.launch {
            newsRepository.setNewsBookmark(news, true)
        }
    }

    fun deleteNews(news: NewsEntity) {
        viewModelScope.launch {
            newsRepository.setNewsBookmark(news, false)
        }
    }

}