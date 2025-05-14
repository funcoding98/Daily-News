package com.daily.news.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily.news.adapter.NewsAdapter
import com.daily.news.databinding.FragmentFeedsBinding
import com.daily.news.interfaces.NewsInterface
import com.daily.news.model.NewsItem
import com.daily.news.viewModel.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

class FeedsFragment : Fragment(), NewsInterface {
    private lateinit var binding: FragmentFeedsBinding
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNewsAdapter()

        // Collect news list data
        lifecycleScope.launchWhenStarted {
            viewModel.newsList.collectLatest { newsList ->
                adapter.updateList(newsList)
            }
        }

        // Add a custom scroll listener
        binding.rvFeeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // Check if scrolling is stopped
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    val centerPosition =
                        if (firstVisibleItemPosition + lastVisibleItemPosition > recyclerView.childCount) {
                            firstVisibleItemPosition
                        } else {
                            lastVisibleItemPosition
                        }

                    // Scroll to the closest item
                    recyclerView.smoothScrollToPosition(centerPosition)
                }
            }
        })
    }

    private fun loadNewsAdapter() {
        adapter = NewsAdapter(emptyList(), this)
        binding.rvFeeds.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFeeds.isNestedScrollingEnabled = true
        binding.rvFeeds.adapter = adapter
    }

    private fun shareNews(newsItem: NewsItem) {
        // Create an Intent to share the news content
        val deepLinkUrl = "demoapp://news?newsId=${newsItem.news_id}"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this news!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this news: \n\n${newsItem.title}\n\n$deepLinkUrl"
            )
//            putExtra(Intent.EXTRA_TEXT, "${newsItem.title}\n\n${newsItem.content}")
        }

        // Start the share activity (This opens the system share dialog)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun getCurrentNewsItem(): NewsItem? {
        // Return the current news item to share, you can fetch it from the ViewModel or elsewhere
        return viewModel.newsList.value[0]//viewModel.newsItem.value  // Assuming you are using the live data or state flow
    }

    override fun onShareButtonClick(shareId: String?, newsData: NewsItem) {
        val newsItem = getCurrentNewsItem()  // Implement this based on your logic

        if (newsItem != null) {
            shareNews(newsItem)
        } else {
            Toast.makeText(requireContext(), "No news item to share", Toast.LENGTH_SHORT).show()
        }

    }
}
