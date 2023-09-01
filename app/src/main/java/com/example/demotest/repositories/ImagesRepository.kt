package com.example.demotest.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.demotest.database.DataBaseClient
import com.example.demotest.interfaces.NetworkResponseCallback
import com.example.demotest.models.Images
import com.example.demotest.network.RestClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback
    private var mCountryList: MutableLiveData<List<Images>> =
        MutableLiveData<List<Images>>().apply { value = emptyList() }

    companion object {
        private var mInstance: ImagesRepository? = null
        fun getInstance(): ImagesRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = ImagesRepository()
                }
            }
            return mInstance!!
        }
    }


    private lateinit var mCountryCall: Call<List<Images>>

    fun getImagesList(mContext: Context, currentPage: Int, pageSize: Int, callback: NetworkResponseCallback): MutableLiveData<List<Images>> {
        mCallback = callback

        mCountryCall = RestClient.getInstance().getApiService().getImagesList(currentPage, pageSize)
        mCountryCall.enqueue(object : Callback<List<Images>> {

            override fun onResponse(call: Call<List<Images>>, response: Response<List<Images>>) {
                mCountryList.value = response.body()

                GlobalScope.launch {
                    for (item in mCountryList.value!!) {
                        DataBaseClient.getDatabase(mContext).imageDao().insert(item)
                    }
                }

                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<Images>>, t: Throwable) {
                mCountryList.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return mCountryList
    }
}