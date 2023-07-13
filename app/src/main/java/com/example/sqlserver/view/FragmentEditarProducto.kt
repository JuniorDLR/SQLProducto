package com.example.sqlserver.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sqlserver.databinding.FragmentEditarProductoBinding
import com.example.sqlserver.model.Producto
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_DESCRIPCION
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_POSITION
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_PRODCUTO


import com.example.sqlserver.viewmodel.ViewModelProducto

class FragmentEditarProducto : Fragment() {

    private lateinit var binding: FragmentEditarProductoBinding
    private val viewModelProducto: ViewModelProducto by viewModels()
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditarProductoBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val nombre = arguments?.getString(CODE_PRODCUTO)
        val descripcion = arguments?.getString(CODE_DESCRIPCION)
        val position = arguments?.getInt(CODE_POSITION)

        if (nombre != null && descripcion != null && position != null) {
            binding.tiFragmentProducto.setText(nombre)
            binding.tiFragmentDescripcion.setText(descripcion)

            binding.btnActualizar.setOnClickListener {
                val nombreNuevo = binding.tiFragmentProducto.text.toString()
                val descripcionNueva = binding.tiFragmentDescripcion.text.toString()
                viewModelProducto.editarProducto(nombreNuevo, descripcionNueva, nombre)
                viewModelProducto.asignarActivity(requireActivity(), navController)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}


