package com.daily.news.model

import java.security.Timestamp

data class NewsItem(
    val author: String? = null,
    val category: String? = null,
    val content: String? = null,
    val image: String? = null,
    val likes: Int? = null,
  //  val timestamp: Long? = null,
    val title: String? = null,
    val news_id: String? = null
)

/*
data class NewsItem(
    val title: String = "",
    val description: String = "",
 */
/*   val imageUrl: String = "",
    val category: String = "",*//*

   // val timestamp: Long = 0L
)
*/
