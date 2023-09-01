package com.example.demotest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demotest.R
import com.example.demotest.adapters.ImagesListAdapter
import com.example.demotest.database.DataBaseClient
import com.example.demotest.databinding.ActivityMainBinding
import com.example.demotest.models.Images
import com.example.demotest.utils.NetworkHelper
import com.example.demotest.viewModels.ImagesListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAdapter: ImagesListAdapter
    private lateinit var mViewModel: ImagesListViewModel
    private lateinit var mBinding: ActivityMainBinding
    private var imagesList: MutableList<Images> = mutableListOf()
    lateinit var mLayoutManager: GridLayoutManager
    private var pastVisiblesItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolbar)

        mViewModel = ViewModelProvider(this)[ImagesListViewModel::class.java]

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        initializeRecyclerView()
        initializeObservers()
        observeDataUpdate()
        getImagesList()
    }

    private fun observeDataUpdate() {
        lifecycleScope.launch {

            DataBaseClient.getDatabase(baseContext).imageDao().getList()
                .collect { dataList ->
                    if (dataList != null) {
                        imagesList = dataList as MutableList<Images>
                        mAdapter.setData(imagesList)
                    }
                }
        }
    }

    private fun initializeRecyclerView() {
        val verticalDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val horizontalDecorator = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        verticalDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.seprator)!!)
        horizontalDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.seprator)!!)

        mLayoutManager = GridLayoutManager(this, 2)
        mAdapter = ImagesListAdapter(this)
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapter
            addItemDecoration(verticalDecorator)
            addItemDecoration(horizontalDecorator)
        }

        mBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy >= 0) {
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()

                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && mViewModel.needToScroll) {
                        if (NetworkHelper.isOnline(baseContext)) {
                            mViewModel.currentPage++
                            getImagesList()
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getImagesList() {
        mViewModel.fetchImagesFromServer(mViewModel.currentPage)
    }

    private fun initializeObservers() {
        mViewModel.mShowApiError.observe(this, Observer {
            AlertDialog.Builder(this).setMessage(it).show()
        })
        mViewModel.mShowProgressBar.observe(this, Observer { bt ->
            if (bt) {
                mBinding.progressBar.visibility = View.VISIBLE
                mBinding.floatingActionButton.hide()
            } else {
                mBinding.progressBar.visibility = View.GONE
                mBinding.floatingActionButton.show()
            }
        })
        mViewModel.mShowNetworkError.observe(this, Observer {
            mBinding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
        })
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imageRootView -> {
                    startActivity(
                        Intent(this, Detail::class.java)
                            .putExtra("imageUrl", imagesList!![view.tag as Int].download_url)
                    )
                }
            }
        }
    }
}