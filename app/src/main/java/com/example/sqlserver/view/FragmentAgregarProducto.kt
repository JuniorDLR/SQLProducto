package com.example.sqlserver.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.sqlserver.R
import com.example.sqlserver.databinding.FragmentAgregarProductoBinding
import com.example.sqlserver.viewmodel.ViewModelProducto


class FragmentAgregarProducto : Fragment() {
    private lateinit var binding: FragmentAgregarProductoBinding
    private val viewModelProducto: ViewModelProducto by viewModels()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarProductoBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            guardarDatos()

        }
    }

    private fun guardarDatos() {
        val producto = binding.tiProducto.text.toString()
        val descripcion = binding.tiDescripcion.text.toString()
        viewModelProducto.agregarProducto(producto, descripcion)
        viewModelProducto.asignarActivity(requireActivity(), navController)
        requireActivity().supportFragmentManager.popBackStack()
    }


}