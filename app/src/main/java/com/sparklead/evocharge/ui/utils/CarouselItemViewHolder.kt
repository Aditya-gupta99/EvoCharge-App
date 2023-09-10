package com.sparklead.evocharge.ui.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sparklead.evocharge.R
import com.sparklead.evocharge.ui.adapters.CarouselAdapter

internal class CarouselItemViewHolder(itemView: View, listener: CarouselAdapter.CarouselItemListener) :
    RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView
    private val listener: CarouselAdapter.CarouselItemListener

    init {
        imageView = itemView.findViewById(R.id.ad_image_view)
        this.listener = listener
    }

    fun bind(item: CarouselItem) {
        Glide
            .with(imageView.context)
            .asBitmap()
            .load(item.getDrawableRes())
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Log.e("SET DRAWABLE BITMAP", resource.toString())
                    imageView.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
        imageView.contentDescription = imageView.resources.getString(item.getContentDescRes())
//        itemView.setOnClickListener {
//            listener.onItemClicked(
//                item,
//                bindingAdapterPosition
//            )
//        }
    }
}