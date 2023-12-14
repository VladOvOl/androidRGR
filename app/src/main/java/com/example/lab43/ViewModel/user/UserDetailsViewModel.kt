package com.example.lab43.ViewModel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab43.model.PizzaService
import com.example.lab43.model.UserDetails


class UserDetailsViewModel(
    private val pizzaService: PizzaService
):ViewModel(){

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails



    init {

    }
    fun loadUsers() {
        if (_userDetails.value != null) return
        pizzaService.getPizzas()

    }

    fun loadUser(userId: Long) {
        if (_userDetails.value != null) return
        _userDetails.value = pizzaService.getById(userId)

    }

    fun deleteUser() {
        val userDetails = this.userDetails.value ?: return
        pizzaService.deletePizza(userDetails.pizza)
    }



}