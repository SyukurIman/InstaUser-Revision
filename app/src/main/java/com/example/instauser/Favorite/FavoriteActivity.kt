package com.example.instauser.Favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instauser.R
import com.example.instauser.Viewmodel.MainViewModel2
import com.example.instauser.databinding.ActivityFavoriteBinding
import com.example.instauser.helper.ViewModelFactory2

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavorite: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavorite as ActivityFavoriteBinding

    private var isFav = false

    private lateinit var adapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavorite = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.favorite)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        showLoading(true)

        val mainViewModel2 = obtainViewModel(this)
        mainViewModel2.getAllFav().observe(this, { favList ->
            val position = favList.size
            if (position > 0) {
                adapter.setListFav(favList)

                isFav = true
                favoriteList(isFav)
            } else {
                favoriteList(isFav)
            }
            showLoading(false)
        })

        adapter = FavoriteAdapter()

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.adapter = adapter
    }

    private fun favoriteList(state: Boolean) {
        if (state) {
            binding.rvNotes.visibility = View.VISIBLE
            binding.noItemFav.visibility = View.INVISIBLE
        } else {
            binding.noItemFav.visibility = View.VISIBLE
            binding.rvNotes.visibility = View.INVISIBLE
            Toast.makeText(this, getString(R.string.no_item_favorite), Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel2 {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel2::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavorite = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}