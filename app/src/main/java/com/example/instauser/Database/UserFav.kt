package com.example.instauser.Database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserFav(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "avatarName")
    var avatarName: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "Location")
    var location: String? = null,

    @ColumnInfo(name = "fullName")
    var fullName: String? = null,

    @ColumnInfo(name = "websiteName")
    var websiteName: String? = null,
) : Parcelable
