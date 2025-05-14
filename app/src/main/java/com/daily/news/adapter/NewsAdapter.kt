package com.daily.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daily.news.databinding.ItemNewBinding
import com.daily.news.interfaces.NewsInterface
import com.daily.news.model.NewsItem

class NewsAdapter(private var newsList: List<NewsItem>, private var newsInterface: NewsInterface) :
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
            // Assuming Glide is being used for loading images (uncomment if needed)
            // Glide.with(holder.itemView.context)
            //     .load(newsItem.image)
            //     .into(imageViewNews)

            shareBtn.setOnClickListener {
                newsInterface.onShareButtonClick(newsItem.news_id ?: "", newsItem)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun updateList(newList: List<NewsItem>) {
        val diffCallback = NewsDiffCallback(newsList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        newsList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}

class NewsDiffCallback(
    private val oldList: List<NewsItem>,
    private val newList: List<NewsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].news_id == newList[newItemPosition].news_id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
