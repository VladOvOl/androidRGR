package com.example.lab43.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.security.keystore.UserNotAuthenticatedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

typealias UsersListener = (pizzas: List<Pizza>) -> Unit

class PizzaService(val context: Context): Service() {

    private var pizzas = mutableListOf<Pizza>()

    private val listeners = mutableSetOf<UsersListener>()

    var db = MainDb.getDb(context)

    private val updateHandler = Handler()
    private val updateIntervalMillis: Long = 5 * 1000

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://64c52e47c853c26efada96fd.mockapi.io/") // Замените на фактический базовый URL вашего API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userServiceApi: ApiService = retrofit.create(ApiService::class.java)

    private val updateRunnable = object : Runnable {
        override fun run() {
            GlobalScope.launch(Dispatchers.IO) {
                loadPizza()
            }
            updateHandler.postDelayed(this, updateIntervalMillis)
        }
    }

    init {
        runBlocking {
            loadPizza()
        }

        updateHandler.postDelayed(updateRunnable, updateIntervalMillis)
    }

    private suspend fun loadPizza() {
        if (isNetworkAvailable()) {
            try {
                pizzas = userServiceApi.getPizzas().toMutableList()
                savePizzasToLocal()
                update()
            } catch (e: Exception) {
                // Обработайте исключение
            }
        } else {
            println("NO Inteenet")
            update()
            loadPizzaFromLocal()
        }

        notifyChanges()
    }

    private fun savePizzasToLocal() {
        try {
            // Save users to Room database
            GlobalScope.launch(Dispatchers.IO) {
                db.getBao().insertPizza(pizzas)
            }
            println("SAVED")
        } catch (e: Exception) {
            // Handle the exception
            println("NO SAVED")
            println(e)
        }


    }
    private  fun update(){
        GlobalScope.launch(Dispatchers.IO) {
            for (user in pizzas) {
                // Проверяем, существует ли пользователь с таким же ID в базе данных
                val existingUser = db.getBao().getUserById(user.id)
                if (existingUser != null) {
                    // Обновляем только те записи, которые изменились
                    if (!existingUser.equals(user)) {
                        db.getBao().updatePizza(user)
                    }
                } else {
                    // Если пользователя нет в базе данных, вставляем нового пользователя
                    db.getBao().insertPizza(mutableListOf(user))
                }
            }
        }
    }
    private fun loadPizzaFromLocal() {
        try {
            // Load users from Room database
            GlobalScope.launch(Dispatchers.IO) {
                pizzas = db.getBao().getAllPizzas()
            }
            notifyChanges()
            println(pizzas)
            println("LOAD")
        } catch (e: Exception) {
            // Handle the exception
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }


    fun getPizzas(): MutableList<Pizza> {
        return pizzas
    }

    fun getById(id: Long):UserDetails{
        val  user = pizzas.firstOrNull{it.id == id} ?: throw UserNotAuthenticatedException()
        return UserDetails(
            pizza = user,
            details = user.ingridients
        )
    }



    fun deletePizza(pizza: Pizza) {
        val indexToDelete = pizzas.indexOfFirst { it.id == pizza.id }
        if (indexToDelete != -1) {
            pizzas.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(pizzas)
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        Handler(Looper.getMainLooper()).post {
            listeners.forEach { it.invoke(pizzas) }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        // Остановите обновление при уничтожении сервиса
        updateHandler.removeCallbacks(updateRunnable)
        super.onDestroy()
    }




}




