package com.daily.news.repo

import com.daily.news.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NewsRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Fetch the list of news items
    fun getNewsList(): Flow<List<NewsItem>> = callbackFlow {
        val docRef = firestore.collection("dailynews").document("adailynews")

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val newsData = snapshot?.get("news") as? List<Map<String, Any>>
            val newsList = newsData?.map {
                NewsItem(
                    title = it["title"] as? String ?: "",
                    content = it["content"] as? String ?: "",
                    news_id = it["news_id"] as? String ?: ""
                )
            } ?: emptyList()

            trySend(newsList).isSuccess
        }

        awaitClose {
            listener.remove()
        }
    }

    // Fetch a single news item by its news_id
    fun getNewsById(newsId: String): Flow<NewsItem?> = callbackFlow {
        val docRef = firestore.collection("dailynews").document("adailynews")

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val newsData = snapshot?.get("news") as? List<Map<String, Any>>
            val newsItem = newsData?.firstOrNull { it["news_id"] == newsId }

            val news = newsItem?.let {
                NewsItem(
                    title = it["title"] as? String ?: "",
                    content = it["content"] as? String ?: "",
                    news_id = it["news_id"] as? String ?: ""
                )
            }

            trySend(news).isSuccess
        }

        awaitClose {
            listener.remove()
        }
    }
}
