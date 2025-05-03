package com.daily.news.repo

import com.daily.news.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
class NewsRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getNewsList(): Flow<List<NewsItem>> = callbackFlow {
        val collection = firestore.collection("news")

        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val newsList = snapshot?.documents?.mapNotNull { document ->
                document.toObject(NewsItem::class.java)
            } ?: emptyList()

            trySend(newsList)
        }

        awaitClose { listener.remove() }
    }
}

