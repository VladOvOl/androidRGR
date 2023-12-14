package com.example.lab43

import com.example.lab43.model.Pizza

interface Navigator {
    fun showDetails(pizza: Pizza)

    fun showDetailsForAdmin(pizza: Pizza)

    fun goBack()

    fun toast(messageRes: String)

    fun showListPizzaForUser()

    fun showListPizzaForAdmin()

    fun goToRegistration()

    fun goToCreate()
}