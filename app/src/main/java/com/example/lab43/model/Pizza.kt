package com.example.lab43.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class Pizza (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Long,
    @ColumnInfo(name = "img")
    @SerializedName("img") val image: String,
    @ColumnInfo(name = "title")
    @SerializedName("title") val name: String,
    @ColumnInfo(name = "bigPrice")
    @SerializedName("bigPrice") val price: String,
    @ColumnInfo(name = "ingridients")
    @SerializedName("ingridients") val ingridients: String,
    )

data class UserDetails(
    val pizza: Pizza,
    val details: String,
)

