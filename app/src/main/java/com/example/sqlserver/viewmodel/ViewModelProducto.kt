package com.example.sqlserver.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.sqlserver.model.Producto
import com.example.sqlserver.model.ProviderProducto
import com.example.sqlserver.model.User

class ViewModelProducto : ViewModel() {

    private val _modelProducto = MutableLiveData<List<Producto>?>()
    private val _modelUser = MutableLiveData<User>()
    private val providerProducto = ProviderProducto()
    private lateinit var navController: NavController

    val modelProducto: LiveData<List<Producto>?> = _modelProducto
    val modelUser: LiveData<User> = _modelUser

    fun asignarActivity(activity: Activity, navController: NavController) {
        providerProducto.setActivity(activity)
        this.navController = navController
    }

    fun obtenerProductos() {
        providerProducto.obtenerProducto { productos ->
            _modelProducto.postValue(productos as List<Producto>?)
        }
    }

    fun addUser(user: String, pw: String) {
        providerProducto.AddUser(user, pw)
        val userValue = User(user, pw)
        _modelUser.postValue(userValue)
    }

    fun iniciarSesion(user: String, pw: String) {
        providerProducto.iniciarSesion(user, pw, navController)
        val userValue = User(user, pw)
        _modelUser.postValue(userValue)
    }

    fun agregarProducto(producto: String, descripcion: String) {
        providerProducto.agrearProducto(producto, descripcion)
        val nuevoProducto = Producto(producto, descripcion)
        val nuevaLista = _modelProducto.value?.toMutableList()
        nuevaLista?.add((nuevoProducto))
        _modelProducto.postValue(nuevaLista)
    }

    fun eliminarProducto(nombre: String) {
        providerProducto.eliminarProducto(nombre) { eliminado ->
            if (eliminado) {
                val listaActualizada = _modelProducto.value?.filter { it.producto != nombre }
                _modelProducto.postValue(listaActualizada)
            } else {
                providerProducto.mensaje()
            }
        }
    }

    fun editarProducto(nombreProducto: String, descripcion: String, filtrado: String) {
        providerProducto.actualizarProducto(nombreProducto, descripcion, filtrado)

    }
}
