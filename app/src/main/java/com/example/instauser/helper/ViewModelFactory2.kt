package com.example.instauser.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instauser.DetailItems.DetailViewModel
import com.example.instauser.Viewmodel.MainViewModel2
import java.lang.IllegalArgumentException

class ViewModelFactory2 private constructor(private val mAplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory2? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory2 {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory2::class.java) {
                    INSTANCE = ViewModelFactory2(application)
                }
            }
            return INSTANCE as ViewModelFactory2
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel2::class.java)) {
            return MainViewModel2(mAplication) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mAplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
    }
}