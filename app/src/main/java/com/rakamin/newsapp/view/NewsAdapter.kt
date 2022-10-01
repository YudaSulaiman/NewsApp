package com.rakamin.newsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rakamin.newsapp.R
import com.rakamin.newsapp.data.local.entity.NewsEntity
import com.rakamin.newsapp.databinding.ItemNewsBinding
import com.rakamin.newsapp.utils.DateFormatter

class NewsAdapter(private val onBookmarkClick: (NewsEntity) -> Unit) : ListAdapter<NewsEntity, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

        val ivBookmark = holder.binding.ivBookmark
        if (news.bookmark) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarked_white))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark_white))
        }
        ivBookmark.setOnClickListener {
            onBookmarkClick(news)
        }

    }

    class MyViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(news: NewsEntity) {
            binding.tvItemTitle.text = news.title
            binding.tvItemPublishedDate.text = DateFormatter.formatDate(news.date)
            Glide.with(itemView.context)
                .load(news.image)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(binding.imgPoster)
            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, NewsDetailActivity::class.java)
                val newsAttribute = NewsEntity(news.title, news.date, news.image, news.desc, news.url, news.bookmark)
                moveToDetail.putExtra(NewsDetailActivity.EXTRA_NEWS, newsAttribute)
                itemView.context.startActivity(moveToDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<NewsEntity> =
            object : DiffUtil.ItemCallback<NewsEntity>() {
                override fun areItemsTheSame(oldUser: NewsEntity, newUser: NewsEntity): Boolean {
                    return oldUser.title == newUser.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: NewsEntity, newUser: NewsEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}