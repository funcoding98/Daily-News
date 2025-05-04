package com.daily.news.view


import android.os.Bundle
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
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
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
viewpagerAdapter()

       // adapter = NewsAdapter(emptyList(), this)
     /*   binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.isNestedScrollingEnabled = true
        binding.recyclerview.adapter = adapter*/

  /*      lifecycleScope.launchWhenStarted {
            viewModel.newsList.collectLatest { newsList ->
                adapter.updateList(newsList)
            }
        }*/
    }

    fun viewpagerAdapter(){
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }



 /*   override fun onShareButtonClick(shareId: Int, newsData: NewsItem) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${newsData.title} \n --link-- \n -via ")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share this news via")
        startActivity(shareIntent)
    }*/


}
