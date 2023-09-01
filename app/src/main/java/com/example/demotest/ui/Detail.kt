package com.example.demotest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demotest.R
import com.example.demotest.databinding.ActivityDetailBinding

class Detail : AppCompatActivity() {

    private lateinit var mBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val imageUrl = intent.getStringExtra("imageUrl")

        mBinding.imageUrl = imageUrl
    }
}