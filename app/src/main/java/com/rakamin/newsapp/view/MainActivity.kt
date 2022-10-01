package com.rakamin.newsapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakamin.newsapp.R
import com.rakamin.newsapp.data.Fetch
import com.rakamin.newsapp.data.local.entity.NewsEntity
import com.rakamin.newsapp.databinding.ActivityMainBinding
import com.rakamin.newsapp.view.viewmodel.NewsViewModel
import com.rakamin.newsapp.view.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: NewsViewModel  by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, category)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        getList(null)
        getNewsData()

    }

    private fun getList(item: String?){
        viewModel.getNews(item).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Fetch.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvNews.visibility = View.GONE
                    }
                    is Fetch.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvNews.visibility = View.VISIBLE
                        val data = result.data
                        setNewsList(data)
                    }
                    is Fetch.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun getNewsData(){

        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            binding.rvNews.visibility = View.VISIBLE
            val item = parent.getItemAtPosition(position).toString()
            getList(item)
        }
    }

    private fun setNewsList(news: List<NewsEntity>){

        val newsAdapter = NewsAdapter { news ->
            if (news.bookmark){
                viewModel.deleteNews(news)
            } else {
                viewModel.saveNews(news)
            }
        }

        newsAdapter.submitList(news)

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_bookmarks -> {
                val moveToSettingActivity = Intent(this, NewsBookmarkActivity::class.java)
                startActivity(moveToSettingActivity)
                true
            }
            R.id.menu_profile -> {
                val moveToStarredActivity = Intent(this, ProfileActivity::class.java)
                startActivity(moveToStarredActivity)
                true
            }
            else -> true
        }
    }
}