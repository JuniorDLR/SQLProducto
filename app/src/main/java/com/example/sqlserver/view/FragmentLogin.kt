package com.example.sqlserver.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.sqlserver.R
import com.example.sqlserver.databinding.FragmentLoginBinding
import com.example.sqlserver.viewmodel.ViewModelProducto



class FragmentLogin : Fragment() {
    private lateinit var bindingLogin: FragmentLoginBinding
    private val viewModelProducto: ViewModelProducto by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingLogin = FragmentLoginBinding.inflate(inflater, container, false)
        val navController = NavHostFragment.findNavController(this)

        viewModelProducto.asignarActivity(requireActivity(), navController)

        bindingLogin.btnIniciar.setOnClickListener {
            inicio()
        }
        bindingLogin.tvRegistro.setOnClickListener {
            registrarUsuario()
        }

        return bindingLogin.root
    }

    private fun registrarUsuario() {
        Navigation.findNavController(bindingLogin.root).navigate(R.id.fragmentUsuario)
    }

    private fun inicio() {
        val user = bindingLogin.etUser.text.toString()
        val pw = bindingLogin.etPW.text.toString()
        viewModelProducto.iniciarSesion(user, pw)


    }
}
