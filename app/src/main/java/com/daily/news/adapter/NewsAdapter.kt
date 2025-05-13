package com.daily.news.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.daily.news.model.NewsItem
// Imports needed
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.daily.news.databinding.ItemNewBinding
import com.daily.news.interfaces.NewsInterface

class NewsAdapter(private var newsList: List<NewsItem>,private var newsInterface: NewsInterface) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemNewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.binding.apply {
            titleTextView.text = newsItem.title
            descriptionTextView.text = newsItem.content
       /*     Glide.with(holder.itemView.context)
                .load(newsItem.image)
                .into(imageViewNews)*/
            shareBtn.setOnClickListener {
                newsInterface.onShareButtonClick(shareId = position, newsData = newsItem)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun updateList(newList: List<NewsItem>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
