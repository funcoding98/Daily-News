package com.daily.news.view


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.R
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.daily.news.adapter.NewsAdapter
import com.daily.news.adapter.ViewPagerAdapter
import com.daily.news.databinding.ActivityDashboardBinding
import com.daily.news.interfaces.NewsInterface
import com.daily.news.viewModel.NewsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : AppCompatActivity(), NewsInterface {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val tabTitles = listOf("MyFeed", "News", "DailyNews")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        handleDeepLink(intent)
        viewpagerAdapter()


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }


    fun viewpagerAdapter() {
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun handleDeepLink(intent: Intent?) {
        val data = intent?.data ?: return
        if (data.scheme == "demoapp" && data.host == "feeds") {
            val newsId = data.getQueryParameter("newsId")
            // Feeds tab is at position 0
            val viewPager = binding.viewPager
            viewPager.setCurrentItem(0, false)

        }

    }
}
