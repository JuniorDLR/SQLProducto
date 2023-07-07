package com.example.sqlserver.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlserver.databinding.ActivityProductoBinding
import com.example.sqlserver.model.Producto
import com.example.sqlserver.model.sqlserver.ConnectionSQL
import com.example.sqlserver.view.adapter.AdapterProducto
import com.example.sqlserver.viewmodel.ViewModelProducto
import java.util.Objects

class ProductoActivity : AppCompatActivity() {
    val listaProducto = mutableListOf<Producto>()

    private lateinit var binding: ActivityProductoBinding
    private lateinit var connectionSQL: ConnectionSQL
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterProducto
    private val viewModelProducto: ViewModelProducto by viewModels()
    private var positionEditar: Int = -1


    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { lauchActivity ->

            if (lauchActivity.resultCode == RESULT_OK) {
                val data = lauchActivity.data
                if (data != null) {
                    val nombreProducto = data.getStringExtra(PRODUCTO_CODE)
                    val descripcionProducto = data.getStringExtra(DESCRIPCION_CODE)
                    val desicion = intent.getBooleanExtra(EDICION, false)

                    if (nombreProducto != null && descripcionProducto != null) {
                        if (desicion && positionEditar != -1) {
                            val productoActuali = listaProducto[positionEditar].producto
                            viewModelProducto.editarProducto(nombreProducto, descripcionProducto, productoActuali)

                            positionEditar = -1
                        } else {

                            viewModelProducto.agrearProducto(nombreProducto, descripcionProducto)
                        }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponet()
        viewModelProducto.asignarActivity(this)
        viewModelProducto.obtenerProductos()
        viewModelProducto.modelProducto.observe(this, Observer { productoObserver ->
            if (productoObserver != null) {
                listaProducto.addAll(productoObserver)
                adapter.notifyDataSetChanged()
            }

        })

        binding.svFiltro.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(textoFiltado: String?): Boolean {
                adapter.filter.filter(textoFiltado)
                return true
            }

        })


        binding.btnAgregarProducto.setOnClickListener {
            agregarProducto()
        }
    }


    private fun initComponet() {
        connectionSQL = ConnectionSQL()
        val recycler = binding.recyclerviewProducto
        recyclerView = recycler

        adapter = AdapterProducto(
            listaProducto = listaProducto,
            onClickDelete = { position, nombre -> deleteItem(position, nombre) },
            onclickUpdate = { update -> updateItem(update) })

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
    }

    private fun deleteItem(position: Int, nombre: String) {
        listaProducto.removeAt(position)
        viewModelProducto.eliminarProducto(nombre)
        adapter.notifyDataSetChanged()
    }


    private fun updateItem(update: Int) {
        positionEditar = update
        val datosPro = listaProducto[update]

        val producto = datosPro.producto
        val descripcion = datosPro.descripcion

        val intent = Intent(this, AgregarProducto::class.java)
        intent.putExtra(PRODUCTO_CODE, producto)
        intent.putExtra(DESCRIPCION_CODE, descripcion)
        intent.putExtra(EDICION, true)
        launcher.launch(intent)


    }


    private fun agregarProducto() {
        val intent = Intent(this, AgregarProducto::class.java)
        launcher.launch(intent)
    }

    companion object {
        const val PRODUCTO_CODE = "producto"
        const val DESCRIPCION_CODE = "descripcion"
        const val POSICION_CODE = "posicion"
        const val EDICION = "edicion"
        const val NOMBRE_CODE = "nombre"
    }


}