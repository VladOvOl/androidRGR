package com.example.lab43.model


data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val password: String,
    val admin : Boolean
){
    // Конструктор без параметров для использования с Firebase
    constructor() : this("", "", "", "",false)

}


