package com.example.demotest.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.demotest.database.DataBaseClient
import com.example.demotest.interfaces.NetworkResponseCallback
import com.example.demotest.models.Images
import com.example.demotest.repositories.ImagesRepository
import com.example.demotest.utils.NetworkHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesListViewModel(private val app: Application) : AndroidViewModel(app) {
    private var mList: MutableLiveData<List<Images>> =
        MutableLiveData<List<Images>>().apply { value = emptyList() }
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var mRepository = ImagesRepository.getInstance()
    var currentPage = 1
    val pageSize = 15
    var needToScroll = true

    fun fetchImagesFromServer(currentPage: Int) {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            needToScroll = false
            mList = mRepository.getImagesList(app.baseContext, currentPage, pageSize, object : NetworkResponseCallback {
                    override fun onNetworkFailure(th: Throwable) {
                        mShowApiError.value = th.message
                        needToScroll = false
                    }

                    override fun onNetworkSuccess() {
                        mShowProgressBar.value = false

                        if (mList.value!!.count() >= pageSize)
                            needToScroll = true
                        else
                            needToScroll = false
                    }
                })
        } else
            mShowNetworkError.value = true
    }

    fun onRefreshClicked(view: View) {
        GlobalScope.launch {
            DataBaseClient.getDatabase(app.baseContext).imageDao().deleteAll()
        }

        currentPage = 1
        fetchImagesFromServer(currentPage)
    }
}