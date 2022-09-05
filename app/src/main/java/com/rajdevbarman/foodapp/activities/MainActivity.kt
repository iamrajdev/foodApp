package com.rajdevbarman.foodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rajdevbarman.foodapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btn_nav);
        val navController = Navigation.findNavController(this, R.id.host_fragment);

        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }
}