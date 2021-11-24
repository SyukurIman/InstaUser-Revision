package com.example.instauser.Activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instauser.BuildConfig
import com.example.instauser.Database.UserFav
import com.example.instauser.DetailItems.DetailActivity
import com.example.instauser.DetailItems.FollowerFragment
import com.example.instauser.DetailItems.FollowingFragment
import com.example.instauser.R
import com.example.instauser.RecyclerViewItems.ListAdapter
import com.example.instauser.Viewmodel.MainView
import com.example.instauser.databinding.ActivitySearchResutBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class SearchResult : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    private lateinit var mainView: MainView

    private  var _binding: ActivitySearchResutBinding? = null

    private val binding get() = _binding as ActivitySearchResutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchResutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        onCreateOptionsMenu(binding.toolbar.menu)

        mainView = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainView::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu.findItem(R.id.search2).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.title_searh)
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }
        })
        return true
    }

    private fun search(newText: String?) {
        adapter = ListAdapter()
        showLoading(true)

        if (newText.isNullOrBlank()) {
            search(false)
            showLoading(false)
        } else if (!newText.isNullOrBlank()) {
            showLoading(true)
            search(false)

            mainView.let { mainView ->
                this@SearchResult.mainView = setListUserSearch(newText)
                mainView.getList().observe(this@SearchResult, { userItem ->
                    val position = userItem.size
                    binding.ViewSearch.layoutManager = LinearLayoutManager(this@SearchResult)
                    binding.ViewSearch.adapter = adapter
                    if (position > 0) {
                        adapter.setData(userItem)
                        showLoading(false)

                    }
                })
            }
            search(true)
        }

        setListClickAction()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun search(state: Boolean): Boolean {
        if (state) {
            binding.ViewSearch.visibility = View.VISIBLE
            binding.textEmpty.visibility = View.INVISIBLE
        } else {
            binding.ViewSearch.visibility = View.INVISIBLE
            binding.textEmpty.visibility = View.VISIBLE
        }
        return false
    }

    private fun setListClickAction() {
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClick(itemsView: UserFav) {
                val intent = Intent(this@SearchResult, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_NAME, itemsView.name)
                    putExtra(DetailActivity.EXTRA_AVATAR, itemsView.avatarName)
                    putExtra(DetailActivity.EXTRA_TYPE, itemsView.type)
                    putExtra(FollowerFragment.EXTRA_NAME, itemsView.name)
                    putExtra(FollowingFragment.EXTRA_NAME, itemsView.name)
                }
                startActivity(intent)
            }
        })
    }

    private fun setListUserSearch(UserName: String?): MainView {
        val listUser = mainView.listUser
        val listItems = ArrayList<UserFav>()
        val url = "https://api.github.com/search/users?q=$UserName"

        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.TOKEN_SEARCH)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val jsonArray = responseObject.getJSONArray("items")

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItem = UserFav()
                        userItem.avatarName = jsonObject.getString("avatar_url")
                        userItem.url = jsonObject.getString("url")
                        userItem.type = jsonObject.getString("type")
                        userItem.name = jsonObject.getString("login")

                        listItems.add(userItem)
                    }

                    listUser.postValue(listItems)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (statusCode > 400) {
                    Toast.makeText(
                        this@SearchResult,
                        getString(R.string.not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@SearchResult,
                        getString(R.string.error_code) + ": $statusCode",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                search(false)
            }
        })
        return mainView
    }
}