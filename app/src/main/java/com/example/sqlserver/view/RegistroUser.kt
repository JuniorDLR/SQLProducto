package com.example.sqlserver.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.sqlserver.R
import com.example.sqlserver.databinding.ActivityAgregarProductoBinding
import com.example.sqlserver.databinding.ActivityRegistroUserBinding
import com.example.sqlserver.viewmodel.ViewModelProducto

class RegistroUser : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUserBinding
    private val viewModelProducto: ViewModelProducto by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelProducto.asignarActivity(this)
        binding.btnGuardarUsuario.setOnClickListener {
            guardarUser()
        }

    }

    private fun guardarUser() {
        val user = binding.tiUsuario.text.toString()
        val pw = binding.tiContraseA.text.toString()
        viewModelProducto.addUser(user, pw)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}