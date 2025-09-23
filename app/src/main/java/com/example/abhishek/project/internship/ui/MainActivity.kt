package com.example.abhishek.project.internship.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.abhishek.project.internship.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

   private lateinit var navigationView : BottomNavigationView

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)


       val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment

       val navController = navHostFragment.navController

       navigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

       NavigationUI.setupWithNavController(navigationView,navController)


       ViewCompat.setOnApplyWindowInsetsListener(navigationView) { view, insets ->
           val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

           view.updatePadding(bottom = navBarInsets.bottom)

           insets
       }

   }
}