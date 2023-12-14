package com.example.lab43.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lab43.UsersAdapter
import com.example.lab43.ViewModel.navigator
import com.example.lab43.databinding.FragmentSignInBinding
import com.example.lab43.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.security.MessageDigest

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private lateinit var adapter: UsersAdapter

    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("users")
    var user: User? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SignInButtonFromSignInFragment.setOnClickListener {
            val email = binding.inputTextEmailFromSignInFragment.text.toString()
            val password = binding.inputTextPasswordFromSignInFragment.text.toString()

            if (email.isNotEmpty() || password.isNotEmpty()) {
                val database = Firebase.database
                val myRef = database.reference.child("users/${encrypt(email).toString()}")

                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val foundObject = dataSnapshot.getValue(User::class.java)
                        println(foundObject)
                        if (foundObject != null) {
                            user = foundObject
                            if (foundObject.email == email && foundObject.password == password) {
                                if (foundObject.admin == true) {
                                    // Если пользователь - админ, вывести в консоль
                                    navigator().toast("ADMIN")
                                    navigator().showListPizzaForAdmin()
                                } else {
                                    navigator().toast("Ви успішно увійшли!")
                                    navigator().showListPizzaForUser()
                                }
                            } else {
                                navigator().toast("Невірна пошта або пароль!!!!")
                            }
                        } else {
                            navigator().toast("Невірна пошта або пароль!!!!")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("ERROR")
                    }
                })
            } else {
                navigator().toast("Будь ласка, введіть електронну пошту aбо пароль.")
            }
        }

        binding.RegistrationButtonFromSignInFragment.setOnClickListener {
            navigator().goToRegistration()

        }


    }

    fun encrypt(email: String): Long {
        // Используем SHA-256 для создания хеша
        val sha256 = MessageDigest.getInstance("SHA-256")
        val bytes = sha256.digest(email.toByteArray())

        // Преобразуем хеш в число
        val hashAsLong = bytes.fold(0L) { acc, byte -> (acc shl 8) or byte.toLong() }

        // Ограничиваем число до 15 цифр
        return hashAsLong % 10_000_000_000_000L
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}