package com.daily.news.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.daily.news.repo.NewsRepository
import com.daily.news.model.NewsItem
import kotlinx.coroutines.flow.StateFlow



class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _newsList = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsList: StateFlow<List<NewsItem>> = _newsList

    init {
        viewModelScope.launch {
            repository.getNewsList().collect {
                _newsList.value = it
            }
        }
    }
}

