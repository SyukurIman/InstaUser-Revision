package com.example.instauser.Viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.instauser.Database.UserFav
import com.example.instauser.repository.Repository

class MainViewModel2(application: Application) : ViewModel() {
    private val mRepository: Repository = Repository(application)

    fun getAllFav(): LiveData<List<UserFav>> = mRepository.getAllFav()
}