package com.example.sqlserver.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
    private var listaProducto = mutableListOf<Producto>()

    private lateinit var binding: ActivityProductoBinding
    private lateinit var connectionSQL: ConnectionSQL
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterProducto
    private val viewModelProducto: ViewModelProducto by viewModels()
    var nombreActual: Int = 0

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { lauchActivity ->
            if (lauchActivity.resultCode == RESULT_OK) {
                val data = lauchActivity.data
                if (data != null) {
                    val nombreProducto = data.getStringExtra(PRODUCTO_CODE)
                    val descripcionProducto = data.getStringExtra(DESCRIPCION_CODE)
                    val decision = data.getBooleanExtra(EDICION, false)
                    val posicion = data.getIntExtra(POSICION_CODE, -1)
                    val registro = data.getBooleanExtra("registro", false)



                    if (nombreProducto != null && descripcionProducto != null) {
                        if (decision && posicion != -1) {

                            val actual = listaProducto[nombreActual].producto
                            val productoActualizado =
                                Producto(nombreProducto, descripcionProducto)
                            viewModelProducto.editarProducto(
                                nombreProducto,
                                descripcionProducto, actual
                            )
                            adapter.actualizarElemento(posicion, productoActualizado)
                        } else {
                            viewModelProducto.agregarProducto(
                                nombreProducto,
                                descripcionProducto
                            )
                        }

                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
        viewModelProducto.asignarActivity(this)
        viewModelProducto.obtenerProductos()
        viewModelProducto.modelProducto.observe(this, Observer { productoObserver ->
            if (productoObserver != null) {
                listaProducto.clear() // Limpia la lista actual
                listaProducto.addAll(productoObserver) // Agrega los nuevos datos del ViewModel
                adapter.notifyDataSetChanged() // Notifica al adaptador para que actualice la vista
            }
        })


        binding.svFiltro.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(textoFiltrado: String?): Boolean {
                adapter.filter.filter(textoFiltrado)
                return true
            }
        })

        binding.btnAgregarProducto.setOnClickListener {
            agregarProducto()
        }
    }

    private fun initComponent() {
        connectionSQL = ConnectionSQL()
        val recycler = binding.recyclerviewProducto
        recyclerView = recycler

        adapter = AdapterProducto(
            listaProducto = listaProducto,
            onClickDelete = { position, nombre -> deleteItem(position, nombre) },
            onclickUpdate = { update -> updateItem(update) }
        )

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
    }

    private fun deleteItem(position: Int, nombre: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Eliminar elemento")
        alertDialog.setMessage("¿Está seguro que desea eliminar este elemento?")
        alertDialog.setPositiveButton("Sí") { dialog, _ ->
            viewModelProducto.eliminarProducto(nombre)
            listaProducto.removeAt(position)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()
    }


    private fun updateItem(update: Int) {
        nombreActual = update
        val datosPro = listaProducto[update]

        val producto = datosPro.producto
        val descripcion = datosPro.descripcion
        val intent = Intent(this, AgregarProducto::class.java)
        intent.putExtra(PRODUCTO_CODE, producto)
        intent.putExtra(DESCRIPCION_CODE, descripcion)
        intent.putExtra(EDICION, true)
        intent.putExtra(POSICION_CODE, update)
        launcher.launch(intent)
        adapter.notifyItemChanged(update)

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
    }
}
