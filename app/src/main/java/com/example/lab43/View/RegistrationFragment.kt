package com.example.lab43.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lab43.ViewModel.navigator
import com.example.lab43.databinding.FragmentRegistrationBinding
import com.example.lab43.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.security.MessageDigest
import kotlin.random.Random


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.RegistrationButtonFromRegistrationFragment.setOnClickListener{
           val FullName = binding.inputTextFullnameFromRegistrationFragment.text.toString()
           val Email = binding.inputTextEmailFromRegistrationFragment.text.toString()
           val Password = binding.inputTextPasswordFromRegistrationFragment.text.toString()
           val randomLong: Long = Random.nextLong()
           println(encrypt(Email))
           if (FullName.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
               navigator().toast("Заповніть будласка всі поля")
           } else {
               val database = Firebase.database
               val usersRef = database.getReference("users")

               // Проверяем наличие пользователя с таким email
               usersRef.orderByChild("id").equalTo(encrypt(Email).toString()).addListenerForSingleValueEvent(object :
                   ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       if (snapshot.exists()) {
                           // Пользователь с таким email уже существует
                           navigator().toast("Пользователь с таким email уже зарегистрирован")
                       } else {
                           // Пользователя с таким email нет, регистрируем нового пользователя
                           val user = User(
                               id = encrypt(Email).toString(),
                               fullName = FullName,
                               email = Email,
                               password = Password,
                               admin = false
                           )
                           val myRef = database.getReference("users").child(user.id.toString())
                           myRef.setValue(user)
                           navigator().showListPizzaForUser()
                       }
                   }

                   override fun onCancelled(error: DatabaseError) {
                       // Обработка ошибок при чтении из базы данных
                       navigator().toast("Ошибка при проверке пользователя: ${error.message}")
                   }
               })
           }

       }

        binding.BackButtonFromRegistrationFragment.setOnClickListener{
            navigator().goBack()
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


