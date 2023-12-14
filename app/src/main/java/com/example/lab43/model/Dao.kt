package com.example.lab43.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPizza(pizza: MutableList<Pizza>)
    @Query("SELECT * FROM users")
    fun  getAllPizzas(): MutableList<Pizza>
    @Update
    fun updatePizza(pizza: Pizza)
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): Pizza?






}