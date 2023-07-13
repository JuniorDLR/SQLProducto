package com.example.sqlserver.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlserver.R
import com.example.sqlserver.databinding.FragmentSincronizarBinding

class FragmentSincronizar : Fragment() {
    private lateinit var binding: FragmentSincronizarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSincronizarBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        // Aquí puedes realizar acciones adicionales cuando el Fragmento está a punto de detenerse.
        Log.d("FragmentSincronizar", "El Fragmento se ha pausado.")
        // Guardar el estado de los datos o detener tareas en ejecución, por ejemplo.
    }

    override fun onStop() {
        super.onStop()
        // Aquí puedes realizar acciones adicionales cuando el Fragmento ya no es visible.
        Log.d("FragmentSincronizar", "El Fragmento se ha detenido.")
        // Liberar recursos u otras tareas necesarias.
    }
}
