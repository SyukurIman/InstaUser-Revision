package com.example.instauser.ImageSlider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BannerMainView : ViewModel() {
    private val listImage = MutableLiveData<List<BannerModel>>()

    fun bannerSet() {
        val listBanner = ArrayList<BannerModel>()
        val bannerModel = bannerList

        listBanner.addAll(bannerModel)
        listImage.postValue(listBanner)
    }

    fun getList(): LiveData<List<BannerModel>> {
        return listImage
    }
}