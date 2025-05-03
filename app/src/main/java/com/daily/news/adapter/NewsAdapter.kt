package com.daily.news.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.daily.news.model.NewsItem
// Imports needed
import android.view.ViewGroup
import com.daily.news.databinding.ItemNewBinding

class NewsAdapter(private var newsList: List<NewsItem>) :
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
            descriptionTextView.text = newsItem.description
        }
    }

    override fun getItemCount(): Int = newsList.size

    // ✅ Add this to allow updating data
    fun updateList(newList: List<NewsItem>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
