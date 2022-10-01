package com.rakamin.newsapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rakamin.newsapp.data.local.entity.NewsEntity
import com.rakamin.newsapp.databinding.ActivityNewsDetailBinding
import com.rakamin.newsapp.utils.DateFormatter

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getNewsDetail()

    }

    private fun getNewsDetail(){
        val news = intent.getParcelableExtra<NewsEntity>(EXTRA_NEWS) as NewsEntity

        binding.tvNewsTitle.text = news.title
        binding.tvDate.text = DateFormatter.formatDate(news.date)
        binding.tvNewsDesc.text = news.desc
        Glide.with(binding.ivNewsImage)
            .load(news.image)
            .into(binding.ivNewsImage)
        binding.buttonNewsUrl.setOnClickListener {
            val browserDirect = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            startActivity(browserDirect)
        }
    }

    companion object{
        const val EXTRA_NEWS = "extra_news"
    }
}