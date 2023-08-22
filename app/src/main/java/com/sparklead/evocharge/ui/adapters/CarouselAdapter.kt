package com.sparklead.evocharge.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sparklead.evocharge.ui.utils.CarouselItem
import com.sparklead.evocharge.ui.utils.CarouselItemViewHolder

internal class CarouselAdapter(private val listener: CarouselItemListener, private val itemLayoutRes: Int) :
    ListAdapter<CarouselItem, CarouselItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): CarouselItemViewHolder {
        return CarouselItemViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(itemLayoutRes, viewGroup, false), listener
        )
    }

    override fun onBindViewHolder( carouselItemViewHolder: CarouselItemViewHolder, pos: Int) {
        carouselItemViewHolder.bind(getItem(pos))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CarouselItem> =
            object : DiffUtil.ItemCallback<CarouselItem>() {
                override fun areItemsTheSame(
                    oldItem: CarouselItem, newItem: CarouselItem
                ): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(
                    oldItem: CarouselItem,  newItem: CarouselItem
                ): Boolean {
                    return false
                }
            }
    }

    internal interface CarouselItemListener {
        fun onItemClicked(item: CarouselItem, position: Int)
    }
}