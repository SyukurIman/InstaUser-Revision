package com.example.instauser.DetailItems

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.instauser.R

@Suppress("DEPRECATION")
class DetailPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    private val pages = listOf(
        FollowerFragment(),
        FollowingFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): String {
        return when (position) {
            0 -> context.getString(R.string.Follower)
            else -> context.getString(R.string.Following)
        }
    }

    override fun getCount(): Int {
        return pages.size
    }

}