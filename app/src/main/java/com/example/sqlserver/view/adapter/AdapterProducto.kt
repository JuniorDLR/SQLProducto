package com.example.sqlserver.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlserver.R
import com.example.sqlserver.databinding.ItemProductoBinding
import com.example.sqlserver.model.Producto


class AdapterProducto(
    val onClickDelete: (Int, String) -> Unit,
    val onclickUpdate: (Int) -> Unit
) : RecyclerView.Adapter<AdapterProducto.MyHolder>(), Filterable {
    var listaProducto: List<Producto> = emptyList()
    var filteredList: MutableList<Producto> = listaProducto.toMutableList()

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemProductoBinding.bind(view)

        fun bind(producto: Producto) {
            binding.tvVisibleProducto.text = producto.producto
            binding.tvVisibleDescripcion.text = producto.descripcion
            binding.btnEditar.setOnClickListener { onclickUpdate(adapterPosition) }
            binding.btnEliminar.setOnClickListener {
                onClickDelete(adapterPosition, producto.producto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return MyHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = filteredList[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim() ?: ""
                val filteredItems = if (query.isEmpty()) {
                    listaProducto.toMutableList()
                } else {
                    listaProducto.filter { producto ->
                        producto.producto.contains(query, ignoreCase = true)
                    }.toMutableList()
                }
                val result = FilterResults()
                result.values = filteredItems
                result.count = filteredItems.size
                return result
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = results?.values as? MutableList<Producto> ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    fun setList(newList: List<Producto>) {
        listaProducto = newList
        filteredList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun getDataList(): List<Producto> = listaProducto


}
