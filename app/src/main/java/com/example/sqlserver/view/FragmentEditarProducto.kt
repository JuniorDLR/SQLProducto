package com.example.sqlserver.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sqlserver.databinding.FragmentEditarProductoBinding
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_DESCRIPCION
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_POSITION
import com.example.sqlserver.view.FragmentOperar.Companion.CODE_PRODCUTO


import com.example.sqlserver.viewmodel.ViewModelProducto

class FragmentEditarProducto : Fragment() {

    private lateinit var binding: FragmentEditarProductoBinding
    private val viewModelProducto: ViewModelProducto by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditarProductoBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProducto.asignarActivity(requireActivity())
        val nombre = arguments?.getString(CODE_PRODCUTO)
        val descripcion = arguments?.getString(CODE_DESCRIPCION)
        val position = arguments?.getInt(CODE_POSITION)
        binding.tiFragmentProducto.setText(nombre)
        binding.tiFragmentDescripcion.setText(descripcion)
        binding.btnActualizar.setOnClickListener { actualizarDatos(nombre, descripcion, position) }
    }

    private fun actualizarDatos(nombre: String?, descripcion: String?, position: Int?) {
        if (nombre != null && descripcion != null && position != null) {
            val nombreNuevo = binding.tiFragmentProducto.text.toString()
            val descripcionNueva = binding.tiFragmentDescripcion.text.toString()
            viewModelProducto.editarProducto(nombreNuevo, descripcionNueva, nombre)
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

}


