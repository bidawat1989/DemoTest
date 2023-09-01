package com.example.demotest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demotest.databinding.ImageListItemBinding
import com.example.demotest.models.Images

class ImagesListAdapter(var onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<ImagesListAdapter.MyViewHolder>() {

    private var mList: List<Images>? = listOf()

    fun setData(list: List<Images>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.image = mList!![position]
        holder.binding.tagIndex = position
        holder.binding.listener = onClickListener
    }

    override fun getItemCount(): Int = mList!!.size

    inner class MyViewHolder(var binding: ImageListItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
    }
}