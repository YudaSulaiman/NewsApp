package com.rakamin.newsapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakamin.newsapp.databinding.ActivityNewsBookmarkBinding
import com.rakamin.newsapp.view.viewmodel.NewsViewModel
import com.rakamin.newsapp.view.viewmodel.ViewModelFactory

class NewsBookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBookmarkBinding

    val viewModel: NewsViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.rvNews.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvNews.addItemDecoration(itemDecoration)

        getData()
    }

    private fun getData(){

        val newsAdapter = NewsAdapter { news ->
            if (news.bookmark){
                viewModel.deleteNews(news)
            } else {
                viewModel.saveNews(news)
            }
        }

        viewModel.getBookmarkedNews().observe(this){ result ->
            newsAdapter.submitList(result)
        }

        binding.rvNews.adapter = newsAdapter
    }
}