package com.daily.news.repo

import com.daily.news.model.NewsItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
class NewsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    //private val database = FirebaseDatabase.getInstance().getReference("news_articles")
/*
    fun getNewsList(): Flow<List<NewsItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newsList = mutableListOf<NewsItem>()
                for (newsSnapshot in snapshot.children) {
                    val newsItem = newsSnapshot.getValue(NewsItem::class.java)
                    newsItem?.let { newsList.add(it) }
                }
                trySend(newsList).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }*/
/*  fun getNewsList(): Flow<List<NewsItem>> = callbackFlow {
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
    }*/
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

}

