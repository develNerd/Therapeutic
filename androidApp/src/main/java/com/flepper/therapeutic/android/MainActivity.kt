package com.flepper.therapeutic.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.flepper.therapeutic.Greeting
import com.flepper.therapeutic.android.databinding.ActivityMainBinding
import com.flepper.therapeutic.android.presentation.MainActivityViewModel


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainActivityViewModel: MainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        if (savedInstanceState == null && !mainActivityViewModel.appPreferences.isBeenToDashboard){
            navController.navigate(R.id.intro_fragment)
        }else if (savedInstanceState == null && mainActivityViewModel.appPreferences.isBeenToDashboard){
            val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.home, true).build()
            navController.popBackStack()
            navController.navigate(R.id.action_to_home,null,navOptions)
        }
    }
}
