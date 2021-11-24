package com.example.instauser.DetailItems

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.instauser.BuildConfig
import com.example.instauser.Database.UserFav
import com.example.instauser.R
import com.example.instauser.Viewmodel.MainView
import com.example.instauser.databinding.ActivityDetailBinding
import com.example.instauser.helper.ViewModelFactory2
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var mainView: MainView

    private var isFav = false
    private var position = 0
    private var userListFav: String? = null
    private var userFav: UserFav? = null

    private lateinit var detailViewModel: DetailViewModel

    private var detailBinding: ActivityDetailBinding? = null
    private val binding get() = detailBinding as ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainView = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainView::class.java]

        binding.toolbar.title = intent.getStringExtra(EXTRA_NAME)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        setContentDetail()
        setFavorite()

    }

    private fun checkIconFav(state: Boolean) {
        if (!state) {
            val unChecked: Int = R.drawable.ic_favorite_border
            binding.favoriteImage.setImageResource(unChecked)
            binding.favoriteName.text = getString(R.string.favorite)
        } else {
            val checked: Int = R.drawable.ic_favorite
            binding.favoriteImage.setImageResource(checked)
            binding.favoriteName.text = getString(R.string.Favorited)
        }
    }

    private fun setFavorite() {
        detailViewModel = obtainViewModel(this@DetailActivity)
        userFav = intent.getParcelableExtra(EXTRA_FAV)
        val nameUser = intent.getStringExtra(EXTRA_NAME)

        if (userFav != null) {
            isFav = true
            checkIconFav(isFav)
        } else {
            val mainViewModel2 = obtainViewModel(this)
            mainViewModel2.checkFav().observe(this, { favList ->
                var positionFirst = 0
                position = favList.size

                while (position > positionFirst) {
                    userListFav = favList[positionFirst].name.toString()
                    if (nameUser.toString() == userListFav) {
                        userFav = favList[positionFirst]
                        isFav = true
                    }
                    positionFirst += 1
                }
                if (!isFav) {
                    userFav = UserFav()
                }
                checkIconFav(isFav)
            })
        }

        binding.btnFavorite.setOnClickListener {
            val name = intent?.getStringExtra(EXTRA_NAME).toString().trim()
            val avatar = intent?.getStringExtra(EXTRA_AVATAR).toString().trim()
            val type = intent?.getStringExtra(EXTRA_TYPE).toString().trim()
            val fullName = binding.tvFullName.text.toString().trim()
            val locationName = binding.location.text.toString().trim()
            val websiteName = binding.Website.text.toString().trim()

            if (isFav) {
                detailViewModel.delete(userFav as UserFav)
                showToast(getString(R.string.fav_delete))

                val unChecked: Int = R.drawable.ic_favorite_border
                binding.favoriteImage.setImageResource(unChecked)
                isFav = false
            } else {
                userFav.let { userFav ->
                    userFav?.name = name
                    userFav?.avatarName = avatar
                    userFav?.type = type
                    userFav?.fullName = fullName
                    userFav?.websiteName = websiteName
                    userFav?.location = locationName
                    detailViewModel.insert(userFav as UserFav)
                    showToast(getString(R.string.Fav_succes))
                }
                val checked: Int = R.drawable.ic_favorite
                binding.favoriteImage.setImageResource(checked)
                binding.favoriteName.text = getString(R.string.Favorited)
                isFav = true
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setContentDetail() {
        binding.progressBar.visibility = View.VISIBLE

        try {
            val detailPagerAdapter = DetailPagerAdapter(this, supportFragmentManager)
            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.adapter = detailPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
        if (isFav) {
            binding.let {
                Glide.with(this)
                    .load(intent.getStringExtra(EXTRA_AVATAR))
                    .into(it.tvImage)
                binding.tvFullName.text = userFav?.fullName
                binding.location.text = userFav?.location
                binding.Website.text = userFav?.websiteName
            }
        } else {
            connectToApi()
        }

    }

    private fun connectToApi() {
        val userName = intent?.getStringExtra(EXTRA_NAME)
        Glide.with(this)
            .load(intent.getStringExtra(EXTRA_AVATAR))
            .into(binding.tvImage)

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${userName}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val userItem = UserFav()

                    val fullName = responseObject.getString("name")
                    val locationUser = responseObject.getString("location")
                    val websiteUser = responseObject.getString("blog")
                    userItem.type = responseObject.getString("type")

                    binding.tvFullName.text = fullName
                    binding.location.text = locationUser
                    binding.Website.text = websiteUser

                } catch (e: Exception) {
                    showToast(e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                showToast(getString(R.string.error_code) + ": $statusCode ")
                showAlertDialog()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        detailBinding = null
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val dialogTitle = getString(R.string.Error)
        val dialogMessage = getString(R.string.error_message)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton(getString(R.string.close)) { _, _ -> finish() }
            setNegativeButton(getString(R.string.try_again)) { _, _ -> setContentDetail() }
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.show()
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_TYPE = "extra_type"
    }
}