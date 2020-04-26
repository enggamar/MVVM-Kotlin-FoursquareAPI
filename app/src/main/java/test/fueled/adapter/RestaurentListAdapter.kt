package com.taghawk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import test.fueled.databinding.AdpaterRestaurentListViewBinding
import test.fueled.model.Item
import test.fueled.viewmodel.HomeViewModel
import java.util.*

class RestaurentListAdapter(viewModel: HomeViewModel, mList: ArrayList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var mList: ArrayList<Item>
    private var mViewModel: HomeViewModel? = null


    init {
        this.mList = mList
        mViewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var mBinding = AdpaterRestaurentListViewBinding.inflate(LayoutInflater.from(parent.context))
        return RestaurentListViewHolder(mBinding, mViewModel)
    }

    class RestaurentListViewHolder(
        mBinding: AdpaterRestaurentListViewBinding?,
        mViewModel: HomeViewModel?
    ) :
        RecyclerView.ViewHolder(mBinding!!.root) {
        var binding: AdpaterRestaurentListViewBinding? = null
        private var mViewModel: HomeViewModel? = null

        init {
            binding = mBinding
            this.mViewModel = mViewModel
        }

        fun bind(bean: Item) {
            binding!!.item = bean
            binding!!.viewModel = mViewModel
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RestaurentListViewHolder
        viewHolder.bind(mList.get(position))
    }
}