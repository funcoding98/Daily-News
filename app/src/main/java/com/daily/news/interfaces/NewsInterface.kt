package com.daily.news.interfaces

import com.daily.news.model.NewsItem

interface NewsInterface {

    fun onShareButtonClick(shareId:Int,newsData: NewsItem){}
}