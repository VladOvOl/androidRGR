package com.example.lab43.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab43.MainActivity
import com.example.lab43.Navigator
import com.example.lab43.ViewModel.user.UserDetailsViewModel
import com.example.lab43.ViewModel.user.UsersListViewModel
import com.example.lab43.model.PizzaService

class ViewModelFactory(
    private val pizzaService: PizzaService
) : ViewModelProvider.Factory {

     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            UsersListViewModel::class.java -> {
                UsersListViewModel(pizzaService)
            }
            UserDetailsViewModel::class.java -> {
                UserDetailsViewModel(pizzaService)
            }
            else -> {
                throw IllegalAccessError("Unknown")
            }
        } as T
    }
}


fun Fragment.factory() = ViewModelFactory((requireActivity() as MainActivity).provideUsersService())

fun Fragment.navigator() = requireActivity() as Navigator