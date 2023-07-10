package com.example.sqlserver


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlserver.databinding.FragmentOperarBinding
import com.example.sqlserver.model.Producto
import com.example.sqlserver.model.sqlserver.ConnectionSQL
import com.example.sqlserver.view.adapter.AdapterProducto
import com.example.sqlserver.viewmodel.ViewModelProducto


class FragmentOperar : Fragment() {
    private var listaProducto = mutableListOf<Producto>()
    private lateinit var bindingOperar: FragmentOperarBinding
    private lateinit var connectionSQL: ConnectionSQL
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterProducto
    private val viewModelProducto: ViewModelProducto by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingOperar = FragmentOperarBinding.inflate(inflater, container, false)
        val navController = NavHostFragment.findNavController(this)
        initComponent()
        viewModelProducto.asignarActivity(requireActivity(), navController)
        viewModelProducto.obtenerProductos()
        viewModelProducto.modelProducto.observe(requireActivity(), Observer { productoObserver ->
            if (productoObserver != null) {
                listaProducto.clear()
                listaProducto.addAll(productoObserver)
                adapter.notifyDataSetChanged()
            }
        })


        bindingOperar.svFiltro.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(textoFiltrado: String?): Boolean {
                adapter.filter.filter(textoFiltrado)
                return true
            }
        })

        bindingOperar.btnAgregarProducto.setOnClickListener {
            agregarProducto()
        }
        return bindingOperar.root
    }


    private fun initComponent() {
        connectionSQL = ConnectionSQL()
        val recycler = bindingOperar.recyclerviewProducto
        recyclerView = recycler

        adapter = AdapterProducto(
            listaProducto = listaProducto,
            onClickDelete = { position, nombre -> deleteItem(position, nombre) },
            onclickUpdate = { update -> updateItem(update) }
        )

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
    }

    private fun deleteItem(position: Int, nombre: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
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
        val selectedPosition = listaProducto[update]

        val producto = selectedPosition.producto
        val descripcion = selectedPosition.descripcion
        val bundle = Bundle().apply {
            putString("producto", producto)
            putString("descripcion", descripcion)
        }

        val fragmentEditarProducto = FragmentEditarProducto()
        fragmentEditarProducto.arguments = bundle

        val fragmentManaguer = requireActivity().supportFragmentManager
        fragmentManaguer.beginTransaction()
            .replace(com.google.android.material.R.id.container, fragmentEditarProducto)
            .addToBackStack(null)
            .commit()
    }


    private fun agregarProducto() {
        Navigation.findNavController(bindingOperar.root).navigate(R.id.fragmentAgregarProducto)

    }


}
