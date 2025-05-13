package com.daily.news.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNewsAdapter()
        lifecycleScope.launchWhenStarted {
            viewModel.newsList.collectLatest { newsList ->
                adapter.updateList(newsList)
            }
        }
    }

    private fun loadNewsAdapter(){
        adapter = NewsAdapter(emptyList(), this)
        binding.apply {
           rvFeeds.layoutManager = LinearLayoutManager(requireActivity())
            rvFeeds.isNestedScrollingEnabled = true
            rvFeeds.adapter = adapter
        }

    }
    override fun onShareButtonClick(shareId: Int, newsData: NewsItem) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${newsData.news_id} \n demoapp://feeds?newsId=${newsData.title} \n -via Daily News App"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share this news via")
        startActivity(shareIntent)


    }

}