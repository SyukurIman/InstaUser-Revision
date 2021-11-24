package com.example.instauser.ui.notifications


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.instauser.Favorite.FavoriteActivity
import com.example.instauser.R
import com.example.instauser.databinding.FragmentNotificationsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationsFragment : Fragment(), View.OnClickListener {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var switchTheme: SwitchMaterial
    private lateinit var mainView: MainViewModel
    private lateinit var favoriteBtn: CardView

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timer = object : CountDownTimer(200, 50) {
            override fun onTick(millisUntilFinished: Long) {
                showLoading(true)
            }

            override fun onFinish() {
                showLoading(false)
                binding.theme.visibility = View.VISIBLE
                binding.favorite.visibility = View.VISIBLE
            }
        }
        timer.start()

        binding.toolbar.title = getString(R.string.title_About)

        val pref = context?.dataStore?.let { SettingPreferences.getInstance(it) }
        switchTheme = view.findViewById(R.id.switch_theme)
        mainView = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainView.getThemeSetting().observe(viewLifecycleOwner, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                binding.switchTheme.isChecked = false
            }

        })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainView.saveThemeSetting(isChecked)
        }

        favoriteBtn = view.findViewById(R.id.favorite)
        favoriteBtn.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.favorite) {
            val intent = Intent(context, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}