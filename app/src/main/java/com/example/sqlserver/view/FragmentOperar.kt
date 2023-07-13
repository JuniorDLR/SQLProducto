package com.example.sqlserver.view

import com.example.sqlserver.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
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
    private lateinit var bindingOperar: FragmentOperarBinding
    private lateinit var connectionSQL: ConnectionSQL
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterProducto
    private lateinit var navController: NavController
    private val viewModelProducto: ViewModelProducto by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingOperar = FragmentOperarBinding.inflate(inflater, container, false)



        return bindingOperar.root
    }

    private var isObserverAdded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        navController = NavHostFragment.findNavController(this)
        viewModelProducto.asignarActivity(requireActivity(), navController)

        //viewModelProducto.modelProducto.removeObservers(viewLifecycleOwner)
        viewModelProducto.modelProducto.observe(
            viewLifecycleOwner,
            Observer { productoObserver ->
                if (productoObserver != null) {
                    adapter.setList(productoObserver)
                }
            }
        )



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
    }

    override fun onStart() {
        super.onStart()
        viewModelProducto.obtenerProductos()
    }

    /**
     * override fun onDestroyView() {
    super.onDestroyView()
    // Reiniciar el valor de la variable isObserverAdded al destruir la vista
    isObserverAdded = false
    }
     * */


    private fun initComponent() {
        connectionSQL = ConnectionSQL()
        val recycler = bindingOperar.recyclerviewProducto
        recyclerView = recycler

        adapter = AdapterProducto(
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
            adapter.notifyItemRemoved(position)
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()
    }

    private fun updateItem(update: Int) {
        val selectedPosition = adapter.listaProducto[update]

        val producto = selectedPosition.producto
        val descripcion = selectedPosition.descripcion
        val bundle = Bundle().apply {
            putString(CODE_PRODCUTO, producto)
            putString(CODE_DESCRIPCION, descripcion)
            putInt(CODE_POSITION, update)
        }

        val fragmentEditarProducto = FragmentEditarProducto()
        fragmentEditarProducto.arguments = bundle
        fragmentEditarProducto.navController = navController

        val fragmentManaguer = requireActivity().supportFragmentManager
        fragmentManaguer.beginTransaction()
            .replace(R.id.rl_layout, fragmentEditarProducto)
            .addToBackStack(null)
            .commit()

    }


    private fun agregarProducto() {
        Navigation.findNavController(bindingOperar.root).navigate(R.id.fragmentAgregarProducto)

    }

    companion object {
        const val CODE_PRODCUTO = "producto"
        const val CODE_DESCRIPCION = "descripcion"
        const val CODE_POSITION = "position"

    }


}