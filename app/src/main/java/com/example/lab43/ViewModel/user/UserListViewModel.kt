package com.example.lab43.ViewModel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab43.model.Pizza
import com.example.lab43.model.PizzaService
import com.example.lab43.model.UsersListener


class UsersListViewModel(
    private val pizzaService: PizzaService
) : ViewModel() {

    private val _users = MutableLiveData<List<Pizza>>()
    val users: LiveData<List<Pizza>> = _users
    //private val listeners = mutableSetOf<UsersListener>()

    private  val listener : UsersListener ={
        _users.value = it
    }

    init {

        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        pizzaService.removeListener(listener)
    }

    fun loadUsers() {
        pizzaService.addListener(listener)
    }

    fun deleteUser(pizza: Pizza) {
        pizzaService.deletePizza(pizza)
    }


}