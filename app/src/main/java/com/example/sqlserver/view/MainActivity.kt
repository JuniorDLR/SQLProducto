package com.example.sqlserver.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlserver.model.sqlserver.ConnectionSQL
import com.example.sqlserver.databinding.ActivityMainBinding
import com.example.sqlserver.view.adapter.AdapterProducto
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.sqlserver.model.Producto
import com.example.sqlserver.viewmodel.ViewModelProducto


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModelProducto: ViewModelProducto by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciar.setOnClickListener {
            inicio()
        }
    }

    private fun inicio() {
        val user = binding.etUser.text.toString()
        val pw = binding.etPW.text.toString()
        viewModelProducto.asignarActivity(this)
        viewModelProducto.iniciarSesion(user, pw)
    }


}
