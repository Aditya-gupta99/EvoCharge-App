package com.sparklead.evocharge.ui.utils

class CarouselItem(private val drawable: String , private val contentDescRes: Int, private val url: String = "") {

    fun getDrawableRes(): String {
        return drawable
    }

    fun getContentDescRes(): Int {
        return contentDescRes
    }
}