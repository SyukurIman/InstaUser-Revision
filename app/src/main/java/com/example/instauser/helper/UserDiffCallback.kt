package com.example.instauser.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.instauser.Database.UserFav

class UserDiffCallback(
    private val mOldFavList: List<UserFav>,
    private val mNewFavList: List<UserFav>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.avatarName == newEmployee.avatarName && oldEmployee.type == newEmployee.type
    }

}