package com.example.sqlserver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sqlserver.databinding.FragmentEditarProductoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentEditarProducto : Fragment() {

    private lateinit var binding: FragmentEditarProductoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditarProductoBinding.inflate(inflater, container, false)
        val selectedPosition = arguments?.getInt("selectedPosition")
        val nombre = arguments?.getString("nombre")
        val descripcion = arguments?.getString("descripcion")

        return binding.root
    }


}
