package com.example.instauser.Favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instauser.Database.UserFav
import com.example.instauser.DetailItems.DetailActivity
import com.example.instauser.DetailItems.FollowerFragment
import com.example.instauser.DetailItems.FollowingFragment
import com.example.instauser.databinding.ItemListBinding
import com.example.instauser.helper.UserDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFav = ArrayList<UserFav>()

    inner class FavoriteViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userFav: UserFav) {
            with(binding) {
                textName.text = userFav.name
                Glide.with(binding.root)
                    .load(userFav.avatarName)
                    .into(imgPhoto)
                typeAccount.text = userFav.type
                tvList.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_NAME, userFav.name)
                        putExtra(DetailActivity.EXTRA_AVATAR, userFav.avatarName)
                        putExtra(FollowerFragment.EXTRA_NAME, userFav.name)
                        putExtra(FollowingFragment.EXTRA_NAME, userFav.name)
                        putExtra(DetailActivity.EXTRA_FAV, userFav)
                    }

                    it.context.startActivity(intent)
                }
            }
        }
    }

    fun setListFav(listFav: List<UserFav>) {
        val diffCallback = UserDiffCallback(this.listFav, listFav)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFav.clear()
        this.listFav.addAll(listFav)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int {
        return listFav.size
    }
}