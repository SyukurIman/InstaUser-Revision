package com.example.instauser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.instauser.Database.UserDao
import com.example.instauser.Database.UserFav
import com.example.instauser.Database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllFav(): LiveData<List<UserFav>> = mUserDao.getAllFavorite()

    fun insert(userFav: UserFav) {
        executorService.execute { mUserDao.insert(userFav) }
    }

    fun delete(userFav: UserFav) {
        executorService.execute { mUserDao.delete(userFav) }
    }

}