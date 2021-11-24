package com.example.instauser.RecyclerViewItems


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instauser.Database.UserFav
import com.example.instauser.databinding.ItemListBinding
import com.example.instauser.helper.UserDiffCallback

class ListAdapter : RecyclerView.Adapter<ListAdapter.ItemsViewHolder>() {
    private val mData = ArrayList<UserFav>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClick(itemsView: UserFav)
    }

    fun setData(items: List<UserFav>) {
        val diffCallback = UserDiffCallback(this.mData, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.mData.clear()
        this.mData.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ItemsViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ItemsViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClick(mData[position])
        }
    }

    inner class ItemsViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsView: UserFav) {
            with(binding) {
                Glide.with(binding.root)
                    .asBitmap()
                    .load(itemsView.avatarName)
                    .into(imgPhoto)
                textName.text = itemsView.name
                typeAccount.text = itemsView.type
            }
        }
    }

}