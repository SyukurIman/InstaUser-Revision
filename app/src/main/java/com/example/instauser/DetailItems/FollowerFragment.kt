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
import com.example.instauser.databinding.FragmentFollowerBinding


class FollowerFragment : Fragment() {
    private lateinit var adapter: ListAdapter
    private lateinit var mainView: MainView

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListAdapter()

        setListClickAction()
        binding.ViewFollower.layoutManager = LinearLayoutManager(context)
        binding.ViewFollower.adapter = adapter
        binding.ViewFollower.setHasFixedSize(true)

        mainView =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainView::class.java]
        val message = activity?.intent?.getStringExtra(EXTRA_NAME)
        val follower = "followers"
        mainView.connectToApi(message, follower)

        mainView.getList().observe(viewLifecycleOwner, { userItem ->
            if (userItem != null) {
                adapter.setData(userItem)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListClickAction() {
        adapter.setOnItemClickCallback(object :
            ListAdapter.OnItemClickCallback {
            override fun onItemClick(itemsView: UserFav) {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_NAME, itemsView.name)
                    putExtra(DetailActivity.EXTRA_NAME, itemsView.name)
                    putExtra(DetailActivity.EXTRA_TYPE, itemsView.type)
                    putExtra(DetailActivity.EXTRA_AVATAR, itemsView.avatarName)
                    putExtra(EXTRA_NAME, itemsView.name)
                }
                startActivity(intent)
            }
        })
    }
}