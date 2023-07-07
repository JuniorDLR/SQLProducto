package com.example.sqlserver.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sqlserver.databinding.ActivityAgregarProductoBinding
import com.example.sqlserver.view.ProductoActivity.Companion.DESCRIPCION_CODE
import com.example.sqlserver.view.ProductoActivity.Companion.EDICION
import com.example.sqlserver.view.ProductoActivity.Companion.NOMBRE_CODE
import com.example.sqlserver.view.ProductoActivity.Companion.POSICION_CODE
import com.example.sqlserver.view.ProductoActivity.Companion.PRODUCTO_CODE


class AgregarProducto : AppCompatActivity() {


    private lateinit var binding: ActivityAgregarProductoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val desicion = intent.getBooleanExtra(EDICION, false)
        if (desicion) {

            val producto = intent.getStringExtra(PRODUCTO_CODE)
            val descripcion = intent.getStringExtra(DESCRIPCION_CODE)

            if (producto != null && descripcion != null) {
                binding.tiProducto.setText(producto)
                binding.tiDescripcion.setText(descripcion)
                binding.btnGuardar.text = "Actualizar datos"
            }

        }

        binding.btnGuardar.setOnClickListener {
            capturarDatos()
        }
    }

    private fun capturarDatos() {

        val producto = binding.tiProducto.text.toString()
        val descripcion = binding.tiDescripcion.text.toString()

        val intent = Intent(this, ProductoActivity::class.java)
        intent.putExtra(PRODUCTO_CODE, producto)
        intent.putExtra(DESCRIPCION_CODE, descripcion)
        intent.putExtra(EDICION, intent.getBooleanExtra(EDICION, false))
        setResult(RESULT_OK, intent)
        finish()
    }
}