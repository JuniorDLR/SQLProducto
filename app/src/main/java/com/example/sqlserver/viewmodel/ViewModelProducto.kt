package com.example.sqlserver.viewmodel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sqlserver.model.Producto
import com.example.sqlserver.model.ProviderProducto
import com.example.sqlserver.model.User

class ViewModelProducto() : ViewModel() {

    val modelProducto = MutableLiveData<List<Producto>?>()
    val modelUser = MutableLiveData<User>()
    val providerProducto = ProviderProducto()
    private lateinit var navController: NavController

    fun asignarActivity(activity: Activity, navController: NavController) {
        providerProducto.setActivity(activity)
        this.navController = navController
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
        providerProducto.iniciarSesion(user, pw, navController)
        val productoValue = User(user, pw)
        modelUser.postValue(productoValue)
    }

    fun agregarProducto(producto: String, descripcion: String) {
        providerProducto.agrearProducto(producto, descripcion)
        val nuevoProdcuto = Producto(producto, descripcion)
        val nuevaLista = modelProducto.value?.toMutableList()
        nuevaLista?.addAll(listOf(nuevoProdcuto))
        modelProducto.postValue(nuevaLista)


    }


    fun eliminarProducto(nombre: String) {
        providerProducto.eliminarProducto(nombre) { eliminado ->
            if (eliminado) {
                val listaActualizada = modelProducto.value?.filter { it.producto != nombre }
                modelProducto.postValue(listaActualizada)
            } else {
                providerProducto.mensaje()
            }
        }
    }

    fun editarProducto(nombreProducto: String, descripcion: String, filtrado: String) {
        providerProducto.actualizarProducto(nombreProducto, descripcion, filtrado)

    }


}