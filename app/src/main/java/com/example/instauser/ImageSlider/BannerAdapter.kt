package com.example.instauser.ImageSlider


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instauser.R
import com.example.instauser.databinding.BannerItemLayoutBinding
import com.example.instauser.helper.UserDiffCallback

class BannerAdapter : RecyclerView.Adapter<BannerViewHolder>() {

    private val bannerListItem = ArrayList<BannerModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view =
            BannerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BannerViewHolder(view)
    }

    fun setData(items: List<BannerModel>) {
        bannerListItem.clear()
        bannerListItem.addAll(items)
    }

    override fun getItemCount(): Int {
        return bannerListItem.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val bannerItem = bannerListItem[position]
        holder.bind(bannerItem)
    }
}

class BannerViewHolder(private val view: BannerItemLayoutBinding) :
    RecyclerView.ViewHolder(view.root) {
    fun bind(bannerItem: BannerModel) {
        Glide.with(view.root)
            .asBitmap()
            .load(bannerItem.imageUrl)
            .into(view.bannerImageView)
    }
}