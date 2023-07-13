package com.example.sqlserver.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.sqlserver.model.Producto

class DiffUtilRecycler(
    val nuevaLista: List<Producto>,
    val viejaLista: List<Producto>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = viejaLista.size

    override fun getNewListSize(): Int = nuevaLista.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return viejaLista[oldItemPosition].producto == nuevaLista[newItemPosition].producto
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return viejaLista[oldItemPosition] == nuevaLista[newItemPosition]
    }
}