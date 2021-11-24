package com.example.instauser.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFav: UserFav)

    @Delete
    fun delete(userFav: UserFav)

    @Query("SELECT * from userfav ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<UserFav>>
}