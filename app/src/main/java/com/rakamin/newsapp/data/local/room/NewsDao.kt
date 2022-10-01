package com.rakamin.newsapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rakamin.newsapp.data.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY date DESC")
    fun getNews(): LiveData<List<NewsEntity>>

    @Query("SELECT * FROM news where bookmark = 1")
    fun getBookmarkedNews(): LiveData<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Update
    suspend fun updateNews(news: NewsEntity)

    @Query("DELETE FROM news WHERE bookmark = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title AND bookmark = 1)")
    suspend fun isNewsBookmarked(title: String): Boolean
}