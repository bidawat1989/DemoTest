package com.example.demotest.network

import com.example.demotest.models.Images
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("list")
    fun getImagesList(@Query("page") currentPage: Int,
                      @Query("limit") pageSize: Int) : Call<List<Images>>
}