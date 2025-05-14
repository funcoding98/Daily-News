package com.daily.news.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.daily.news.adapter.ViewPagerAdapter
import com.daily.news.databinding.ActivityDashboardBinding
import com.daily.news.viewModel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.lifecycle.observe  // This extension function


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val tabTitles = listOf("MyFeed", "News", "DailyNews")
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        handleDeepLink(intent)
        viewpagerAdapter()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun viewpagerAdapter() {
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun handleDeepLink(intent: Intent?) {
        val data = intent?.data
        if (data != null && data.scheme == "demoapp" && data.host == "news") {
            val newsId = data.getQueryParameter("newsId")
            if (newsId != null) {
                // Use the newsId to load the specific news item
                Log.d("DashboardActivity", "Deep Link News ID: $newsId")
                loadNewsById(newsId) // Call the method to load the news by ID
            }
        }
//        val data = intent?.data ?: return
//        if (data.scheme == "demoapp" && data.host == "feeds") {
//            val newsId = data.getQueryParameter("newsId")
//            if (newsId != null) {
//                Log.d("DashboardActivity", "Deep Link News ID: $newsId")
//                binding.viewPager.setCurrentItem(0, false)
//                loadNewsById(newsId)
//            }
//        }
    }

    private fun loadNewsById(newsId: String) {
        newsViewModel.loadNewsById(newsId)

        newsViewModel.newsItemLiveData.observe(this, Observer { newsItem ->
            if (newsItem != null) {
                Log.d("DashboardActivity", "Loaded News: $newsItem")
                Toast.makeText(this, "Loaded News: ${newsItem.title}", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("DashboardActivity", "News not found.")
                Toast.makeText(this, "News not found", Toast.LENGTH_SHORT).show()
            }
        })

//        newsViewModel.newsItem.observe(this, Observer { newsItem ->
//            if (newsItem != null) {
//                Log.d("DashboardActivity", "Loaded News: $newsItem")
//                Toast.makeText(this, "Loaded News: ${newsItem.title}", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "News not found", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}
