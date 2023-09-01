package com.example.demotest.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.demotest.R

object BindingAdapters {
    @BindingAdapter("app:imageThumb")
    @JvmStatic
    fun loadImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(imageView)

    }
}