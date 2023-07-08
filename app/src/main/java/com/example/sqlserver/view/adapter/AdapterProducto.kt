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
    var listaProducto: MutableList<Producto>,
    val onClickDelete: (Int, String) -> Unit,
    val onclickUpdate: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterProducto.MyHolder>(), Filterable {

    var filteredList: List<Producto> = listaProducto
    fun actualizarLista(newList: List<Producto>) {
        val diffUtilRecycler = DiffUtilRecycler(newList, filteredList)
        val result = DiffUtil.calculateDiff(diffUtilRecycler)
        result.dispatchUpdatesTo(this)
    }

    fun actualizarElemento(posicion: Int, productoActualizado: Producto) {
        listaProducto[posicion] = productoActualizado
        notifyItemChanged(posicion)
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemProductoBinding.bind(view)

        fun bind(
            producto: Producto,
            onClickDelete: (Int, String) -> Unit,
            onclickUpdate: (Int) -> Unit
        ) {
            binding.tvVisibleProducto.text = producto.producto
            binding.tvVisibleDescripcion.text = producto.descripcion
            binding.btnEditar.setOnClickListener { onclickUpdate(adapterPosition) }
            binding.btnEliminar.setOnClickListener {
                onClickDelete(adapterPosition, producto.producto)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return MyHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = filteredList[position]
        holder.bind(producto, onClickDelete, onclickUpdate)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            //realizar el filtrado y actualizar la lista filtrada respectivamente.
            override fun performFiltering(textoBusqueda: CharSequence?): FilterResults {
                val query = textoBusqueda?.toString()?.trim() ?: ""
                filteredList = if (query.isEmpty()) {
                    listaProducto.toMutableList()
                } else {
                    listaProducto.filter { producto ->
                        producto.producto.contains(query, true)
                    }.toMutableList()
                }
                val result = FilterResults()
                result.values = filteredList //lista filtada
                result.count = filteredList.size //tamaño de lista
                return result
            }

            override fun publishResults(contraint: CharSequence?, result: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = result?.values as? MutableList<Producto> ?: mutableListOf()
                notifyDataSetChanged()
            }

        }
    }


}
