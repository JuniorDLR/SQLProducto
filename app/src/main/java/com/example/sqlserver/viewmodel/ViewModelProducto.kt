package com.example.sqlserver.viewmodel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqlserver.model.Producto
import com.example.sqlserver.model.ProviderProducto
import com.example.sqlserver.model.User

class ViewModelProducto() : ViewModel() {

    val modelProducto = MutableLiveData<List<Producto>?>()
    val modelUser = MutableLiveData<User>()
    val providerProducto = ProviderProducto()

    fun asignarActivity(activity: Activity) {
        providerProducto.setActivity(activity)
    }

    fun obtenerProductos() {
        providerProducto.obtenerProducto { productos ->
            modelProducto.postValue(productos as List<Producto>?)
        }
    }


    fun addUser(user: String, pw: String) {
        providerProducto.AddUser(user, pw)
        val productoValue = User(user, pw)
        modelUser.postValue(productoValue)
    }

    fun iniciarSesion(user: String, pw: String) {
        providerProducto.iniciarSesion(user, pw)
        val productoValue = User(user, pw)
        modelUser.postValue(productoValue)
    }

    fun agrearProducto(producto: String, descripcion: String) {
        providerProducto.agrearProducto(producto, descripcion)
        val producto = Producto(producto, descripcion)
        modelProducto.postValue(listOf(producto))

    }


    fun eliminarProducto(nombre: String) {
        providerProducto.eliminarProducto(nombre) { eliminado ->
            if (eliminado) {
                val listaActualizada = modelProducto.value?.filter { it.producto != nombre }
                modelProducto.postValue(listaActualizada)
            }
        }
    }

    fun editarProducto(nombreProducto: String, descripcion: String, filtrado: String) {
        val lista = modelProducto.value?.toMutableList()
        if (lista != null) {
            val propiedades = lista.find { it.producto == filtrado }
            if (propiedades != null) {
                propiedades.producto = nombreProducto
                propiedades.descripcion = descripcion
                providerProducto.actualizarProdcuto(nombreProducto, descripcion, filtrado)
                modelProducto.postValue(lista)
            }
        }
    }


}