package com.example.sqlserver.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.sqlserver.R
import com.example.sqlserver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newView: BottomNavigationView = binding.BarraNavegacion
        navController = Navigation.findNavController(this, R.id.fragmentContainerView)
        newView.setupWithNavController(navController)

        newView.isVisible = false

    }



}
