package com.daily.news.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.daily.news.repo.NewsRepository
import com.daily.news.model.NewsItem
import androidx.lifecycle.asLiveData

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    // StateFlow to manage the list of news items
    private val _newsList = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsList: StateFlow<List<NewsItem>> = _newsList

    // StateFlow for a single news item
    private val _newsItem = MutableStateFlow<NewsItem?>(null)
    val newsItem: StateFlow<NewsItem?> = _newsItem

    // Converting the StateFlow to LiveData for observation in the UI
    val newsItemLiveData: LiveData<NewsItem?> = newsItem.asLiveData()

    init {
        // Load the list of news items when the ViewModel is created
        viewModelScope.launch {
            repository.getNewsList().collect {
                _newsList.value = it
            }
        }
    }

    // Function to load a single news item by its ID
    fun loadNewsById(newsId: String) {
        viewModelScope.launch {
            repository.getNewsById(newsId).collect { news ->
                _newsItem.value = news
            }
        }
    }
}
