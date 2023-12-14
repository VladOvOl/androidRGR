package com.example.lab43.View.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.lab43.R
import com.example.lab43.ViewModel.factory
import com.example.lab43.ViewModel.navigator
import com.example.lab43.ViewModel.user.UserDetailsViewModel
import com.example.lab43.databinding.FragmentPizzaDetailsAdminBinding
import com.example.lab43.model.ApiService
import com.example.lab43.model.Pizza
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AdminDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPizzaDetailsAdminBinding
    private val viewModel: UserDetailsViewModel by viewModels { factory() }
    private lateinit var pizza: Pizza

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPizzaDetailsAdminBinding.inflate(layoutInflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            pizza = it.pizza
            //binding.userNameTextView.text = it.pizza.name
            binding.userNameTextView2.setText(it.pizza.name)
            //binding.userDetailsTextViewPrice.text = it.pizza.company
            binding.userDetailsTextViewPrice.setText(it.pizza.price)
            if (it.pizza.image.isNotBlank()) {
                Glide.with(this)
                    .load(it.pizza.image)
                    .circleCrop()
                    .into(binding.photoImageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_user_avatar)
                    .into(binding.photoImageView)
            }
            binding.userDetailsTextView.setText(pizza.ingridients)


        })

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            navigator().goBack()
        }

        binding.backButton.setOnClickListener{
            navigator().goBack()
        }

        binding.updateButton.setOnClickListener {
            val name = binding.userNameTextView2.text.toString()
            val price = binding.userDetailsTextViewPrice.text.toString()
            val details = binding.userDetailsTextView.text.toString()

            // Пример данных для отправки
            val retrofit = Retrofit.Builder()
                .baseUrl("https://64c52e47c853c26efada96fd.mockapi.io/") // Замените "your_base_url" на базовый URL вашего API
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)


            val newData = Pizza(pizza.id, pizza.image,name,price,details)
            val request = createRequestBody(newData)
            val call = apiService.updatePizza(pizza.id, request) // Замените "123" на фактический идентификатор

            call.enqueue(object : retrofit2.Callback<String> {
                override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        // Обработка успешного ответа
                        println(responseBody)
                    } else {
                        // Обработка ошибки
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Обработка ошибки сети
                    println("Network Error: ${t.message}")
                }
            })
            viewModel.loadUsers()


        }

        return binding.root
    }

    fun createRequestBody(obj: Any): RequestBody {
        val json = Gson().toJson(obj)
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        return RequestBody.create(mediaType, json)
    }

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: Long): AdminDetailsFragment {
            val fragment = AdminDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }

}