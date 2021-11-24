package com.example.instauser.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instauser.BuildConfig
import com.example.instauser.Database.UserFav
import com.example.instauser.DetailItems.DetailActivity
import com.example.instauser.DetailItems.FollowerFragment
import com.example.instauser.DetailItems.FollowingFragment
import com.example.instauser.ImageSlider.BannerAdapter
import com.example.instauser.ImageSlider.BannerMainView
import com.example.instauser.R
import com.example.instauser.RecyclerViewItems.ItemsView
import com.example.instauser.RecyclerViewItems.ListAdapter
import com.example.instauser.Viewmodel.MainView
import com.example.instauser.Activity.SearchResult
import com.example.instauser.databinding.FragmentHomeBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray


class HomeFragment : Fragment() {
    private lateinit var itemsView: ItemsView
    private lateinit var adapter: ListAdapter
    private lateinit var mainView: MainView
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var pageIndicatorView: CircleIndicator3

    private lateinit var bannerMainView: BannerMainView

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        bannerAdapter = BannerAdapter()
        bannerMainView = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[BannerMainView::class.java]

        val timerM: Long = if (savedInstanceState == null) {
            600
        } else {
            200
        }

        val timer = object : CountDownTimer(timerM, 10) {
            override fun onTick(millisUntilFinished: Long) {
                bannerMainView.bannerSet()
            }

            override fun onFinish() {
                bannerMainView.getList().observe(viewLifecycleOwner, { bannerModel ->
                    bannerAdapter.setData(bannerModel)
                })
                binding.bannerViewPager.adapter = bannerAdapter
                pageIndicatorView = view.findViewById(R.id.indicator)
                pageIndicatorView.setViewPager(binding.bannerViewPager)
                pageIndicatorView.animatePageSelected(2)
            }
        }
        timer.start()

        setList()
        binding.toolbar.title = getString(R.string.title_home)
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        binding.allBtn.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            val intent = Intent(context, SearchResult::class.java)


            startActivity(intent)
        }
        if (item.itemId == R.id.language) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setList() {
        adapter = ListAdapter()
        binding.ViewHome.layoutManager = LinearLayoutManager(context)
        binding.ViewHome.adapter = adapter

        itemsView = ItemsView()
        mainView =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainView::class.java]

        mainView.let { mainView ->
            this.mainView = setListUserHome()
            mainView.getList().observe(this, { userItem ->
                val position = userItem.size
                if (position > 0) {
                    showLoading(false)
                    adapter.setData(userItem)
                } else {
                    showLoading(false)
                }
            })
            setListClickAction()
        }
    }


    private fun setListUserHome(): MainView {
        val listUser = mainView.listUser
        val listItems = ArrayList<UserFav>()
        val url = "https://api.github.com/users/sidiqpermana/followers"



        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.TOKEN_HOME)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItem = UserFav()
                        userItem.avatarName = jsonObject.getString("avatar_url")
                        userItem.url = jsonObject.getString("url")
                        userItem.name = jsonObject.getString("login")
                        userItem.type = jsonObject.getString("type")
                        listItems.add(userItem)
                    }

                    listUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    alert()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("OnFailure", error?.message.toString())
                alert()
            }
        })

        return mainView
    }

    private fun setListClickAction() {
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClick(itemsView: UserFav) {
                val intent = Intent(context, DetailActivity::class.java).apply {
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

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE

        }
    }

    private fun alert() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            val dialogTitle = getString(R.string.Error)
            val dialogMessage = getString(R.string.error_message)
            builder.apply {
                setTitle(dialogTitle)
                setMessage(dialogMessage)
                setPositiveButton(R.string.try_again) { _, _ -> setList() }
                setNegativeButton(R.string.close) { _, _ -> it.finish() }
                setCancelable(false)
            }
            builder.create()
            builder.show()
        }
        alertDialog?.show()
    }

}