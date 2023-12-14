package com.example.lab43.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pizza::class], version = 1,  exportSchema = false)
abstract class MainDb : RoomDatabase() {
    abstract fun getBao() : Dao
    companion object {
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "localStorage2.db"
            ).build()
        }
    }
}