package com.daily.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daily.news.databinding.FragmentNewsBinding
import com.daily.news.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postBtn.setOnClickListener {
            uploadNewsData()
        }
    }
    private fun uploadNewsData() {
        val title = binding.titleEditText.text.toString()
        val content = binding.contentEditText.text.toString()

        if (title.isBlank() || content.isBlank()) {
            Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
            return
        }

        val newsItem = mapOf(
            "title" to title,
            "content" to content,
            "news_id" to System.currentTimeMillis().toString() // simple unique ID
        )

        val adailyNewsRef = firestore.collection("dailynews").document("adailynews")

        // Step 1: Try to get the current news array (if document doesn't exist, create it)
        adailyNewsRef.get()
            .addOnSuccessListener { document ->
                val currentNewsList = if (document.exists()) {
                    document.get("news") as? MutableList<Map<String, Any>> ?: mutableListOf()
                } else {
                    mutableListOf()
                }

                // Step 2: Append new news item
                currentNewsList.add(newsItem)

                // Step 3: Set (or create) the document with updated news list
                adailyNewsRef.set(mapOf("news" to currentNewsList))
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "News posted", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to post news", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to read from Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    /*
        private fun uploadNewsData() {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return
            }

            val newsItem = mapOf(
                "title" to title,
                "content" to content
            )

            val adailyNewsRef = firestore.collection("dailynews").document("adailynews")

            // Step 1: Get current news list (if exists)
            adailyNewsRef.get()
                .addOnSuccessListener { document ->
                    val currentNewsList = document.get("news") as? MutableList<Map<String, String>> ?: mutableListOf()

                    // Step 2: Add new item
                    currentNewsList.add(newsItem)

                    // Step 3: Update the document with new list
                    adailyNewsRef.set(mapOf("news" to currentNewsList))
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "News posted", Toast.LENGTH_SHORT).show()
                            clearForm()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to post news", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load document", Toast.LENGTH_SHORT).show()
                }
        }
    */


    /*
        private fun uploadNewsData() {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return
            }

            // Create a new NewsItem object
            val news = NewsItem(
                title = title,
                content = content
            )

            // Reference to 'dailynews' collection
            val dailyNewsRef = firestore.collection("dailynews")

            // Reference to 'adailynews' document inside 'dailynews' collection
            val adailyNewsRef = dailyNewsRef.document("adailynews")

            // Use 'add()' to add a new document inside the 'posts' sub-collection of 'adailynews' document
            adailyNewsRef.collection("posts") // This is the 'posts' sub-collection
                .add(news) // Adds the news data to the 'posts' sub-collection
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "News posted", Toast.LENGTH_SHORT).show()
                    clearForm()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error posting news", Toast.LENGTH_SHORT).show()
                }
        }
    */

    private fun clearForm() {
        binding.titleEditText.text?.clear()
        binding.contentEditText.text?.clear()
    }
}
