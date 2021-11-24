package com.example.instauser.DetailItems

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.instauser.Database.UserFav
import com.example.instauser.repository.Repository

class DetailViewModel(application: Application) : ViewModel() {
    private val mRepository: Repository = Repository(application)

    fun checkFav(): LiveData<List<UserFav>> = mRepository.getAllFav()

    fun insert(userFav: UserFav) {
        mRepository.insert(userFav)
    }

    fun delete(userFav: UserFav) {
        mRepository.delete(userFav)
    }
}