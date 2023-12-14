package com.example.lab43.View.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab43.UserActionListener
import com.example.lab43.UsersAdapter
import com.example.lab43.ViewModel.factory
import com.example.lab43.ViewModel.navigator
import com.example.lab43.ViewModel.user.UsersListViewModel
import com.example.lab43.databinding.FragmentListAdminBinding
import com.example.lab43.model.Pizza

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AdminListFragment : Fragment() {

    private lateinit var binding: FragmentListAdminBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels{factory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAdminBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(object : UserActionListener{
            override fun onUserMove(pizza: Pizza, moveBy: Int) {
                TODO("Not yet implemented")
            }

            override fun onUserDelete(pizza: Pizza){
                viewModel.deleteUser(pizza)
            }

            override fun onUserDetails(pizza: Pizza){
                navigator().showDetailsForAdmin(pizza)
            }

        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.pizzas = it
        })

        binding.createButton.setOnClickListener(){
            navigator().goToCreate()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }

}