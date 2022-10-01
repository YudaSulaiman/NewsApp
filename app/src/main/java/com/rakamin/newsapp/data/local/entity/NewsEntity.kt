package com.rakamin.newsapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news")
@Parcelize
data class NewsEntity(

    @field:ColumnInfo(name = "title")
    @field:PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "date")
    val date: String,

    @field:ColumnInfo(name = "image")
    val image: String? = null,

    @field:ColumnInfo(name = "description")
    val desc: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null,

    @field:ColumnInfo(name = "bookmark")
    var bookmark: Boolean

    ) : Parcelable