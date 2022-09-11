package com.campusmap.android.wanted_preonboarding_android.news

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campusmap.android.wanted_preonboarding_android.MainActivity
import com.campusmap.android.wanted_preonboarding_android.R
import com.campusmap.android.wanted_preonboarding_android.viewmodel.TopNewsViewModel
import com.campusmap.android.wanted_preonboarding_android.adapter.TopNewsAdapter
import com.campusmap.android.wanted_preonboarding_android.databinding.TopnewsBinding
import kotlinx.coroutines.*


class TopNews : Fragment() {

    private lateinit var binding: TopnewsBinding
    private lateinit var topNewsRecyclerView: RecyclerView

    private val topNewsAdapter by lazy {
        TopNewsAdapter()
    }


/*    private val topNewsViewModel: TopNewsViewModel by lazy {
        ViewModelProvider(this).get(TopNewsViewModel::class.java)
    }*/

    // 요기 requireActivity로 바꿔서 Activity 주기에 맞췄음. by activityViewModels() 쓴거랑 같다고 생각함.
    private val topNewsViewModel: TopNewsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TopNewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d("topNewsArray1", "topNewsArray1")


        Log.d("TopNewsLifecycle", "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.topnews, container, false)
        Log.d("TopNewsLifecycle", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TopNewsLifecycle", "onViewCreated")
        topNewsRecyclerView = view.findViewById(R.id.topNews_recycler_view)
        topNewsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        topNewsRecyclerView.adapter = topNewsAdapter

/*        binding.topNewsRecyclerView.apply {
            findViewById<View>(R.id.topNews_recycler_view)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = topNewsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }*/

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                topNewsViewModel.getTopNewsItemData()
            }
        }


        Log.d("TopNews", topNewsViewModel.getTopNewsAllResponseLiveData().value.toString())
        topNewsViewModel.getTopNewsAllResponseLiveData().observe(
            viewLifecycleOwner,
            {
                topNews -> updateUI(topNews)
            })

    }

    override fun onResume() {
        super.onResume()

        Log.d("TopNewsLifecycle", "onResume")
        val activity : Activity? = activity

        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "TopNews"
        }

        topNewsAdapter.setOnItemClickListener(object : TopNewsAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, pos: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    topNewsViewModel.setTopNewsItemPosition(pos) // 잘못된 구조의 느낌. viewHolder의 position을 넘김
                    createFragment(TopNewsDetail.newInstance())
                }
            }

        })

    }

    companion object { // 근데 왜 이렇게 하는거지?
        fun newInstance() : TopNews {
            return TopNews()
        }
    }


    private fun updateUI(topNews: List<Article?>) {
        topNewsAdapter.submitList(topNews)
        //topNewsRecyclerView.adapter = topNewsAdapter
    }

    private fun createFragment(view: Fragment) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.topNews_container, view)
            .commit()
    }



}