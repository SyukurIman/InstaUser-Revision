package com.example.instauser.DetailItems

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instauser.Database.UserFav
import com.example.instauser.RecyclerViewItems.ListAdapter
import com.example.instauser.Viewmodel.MainView
import com.example.instauser.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment() {
    private lateinit var adapter: ListAdapter
    private lateinit var mainView: MainView

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter()

        binding.ViewFollowing.layoutManager = LinearLayoutManager(context)
        binding.ViewFollowing.adapter = adapter
        binding.ViewFollowing.setHasFixedSize(true)
        setListClickAction()

        mainView =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainView::class.java]

        val message = activity?.intent?.getStringExtra(EXTRA_NAME)
        val following = "following"
        mainView.connectToApi(message, following)

        mainView.getList().observe(viewLifecycleOwner, { userItem ->
            if (userItem != null) {
                adapter.setData(userItem)
            }
        })
    }

    private fun setListClickAction() {
        adapter.setOnItemClickCallback(object :
            ListAdapter.OnItemClickCallback {
            override fun onItemClick(itemsView: UserFav) {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_NAME, itemsView.name)
                    putExtra(DetailActivity.EXTRA_AVATAR, itemsView.avatarName)
                    putExtra(DetailActivity.EXTRA_TYPE, itemsView.type)
                    putExtra(FollowerFragment.EXTRA_NAME, itemsView.name)
                    putExtra(EXTRA_NAME, itemsView.name)
                }
                startActivity(intent)
            }
        })
    }

}