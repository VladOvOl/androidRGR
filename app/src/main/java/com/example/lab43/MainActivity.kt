package com.example.lab43

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.lab43.View.RegistrationFragment
import com.example.lab43.View.SignInFragment
import com.example.lab43.View.admin.AdminDetailsFragment
import com.example.lab43.View.admin.AdminListFragment
import com.example.lab43.View.admin.CreateCardFragment
import com.example.lab43.View.user.UserDetailsFragment
import com.example.lab43.View.user.UserListFragment
import com.example.lab43.databinding.ActivityMainBinding
import com.example.lab43.model.Pizza
import com.example.lab43.model.PizzaService

class MainActivity : AppCompatActivity() ,Navigator{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var pizzaService: PizzaService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, SignInFragment()).commit()
        }
        pizzaService = PizzaService(applicationContext)
    }

    override fun showDetails(pizza: Pizza) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(pizza.id))
            .commit()
    }

    override fun goToCreate() {
        val pizzaListFragment = CreateCardFragment()
        val fragmentManager =
            supportFragmentManager // используйте supportFragmentManager для активности
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, pizzaListFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun showDetailsForAdmin(pizza: Pizza) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, AdminDetailsFragment.newInstance(pizza.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: String) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    // Добавляем функцию, которая будет предоставлять экземпляр UsersService
    fun provideUsersService(): PizzaService {
        return pizzaService
    }

    override fun showListPizzaForUser() {
        val pizzaListFragment = UserListFragment()
        val fragmentManager =
            supportFragmentManager // используйте supportFragmentManager для активности
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, pizzaListFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun showListPizzaForAdmin() {
        val pizzaListFragment = AdminListFragment()
        val fragmentManager =
            supportFragmentManager // используйте supportFragmentManager для активности
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, pizzaListFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun goToRegistration() {
        val registrationFragment = RegistrationFragment()
        val fragmentManager =
            supportFragmentManager // используйте supportFragmentManager для активности
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, registrationFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
