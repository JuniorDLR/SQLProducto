package com.example.sqlserver.model

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.example.sqlserver.R
import com.example.sqlserver.model.sqlserver.ConnectionSQL
import com.example.sqlserver.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException


class ProviderProducto() {
    private lateinit var connectionSQL: ConnectionSQL

    private lateinit var activityContext: Activity

    fun setActivity(activity: Activity) {
        activityContext = activity
    }

    fun teclado() {
        val imm = activityContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activityContext.window.decorView.windowToken, 0)
    }

    fun obtenerProducto(callback: (List<Producto?>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            connectionSQL = ConnectionSQL()

            try {
                val listaProducto =
                    mutableListOf<Producto?>() // Crear una nueva lista en cada llamada

                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "SELECT nombreProducto, descripcionProducto FROM dbo.Producto"
                    connection.prepareStatement(query).use { producto ->

                        val next = producto.executeQuery()

                        while (next.next()) {
                            val nombreProducto = next.getString("nombreProducto")
                            val descripcionProducto = next.getString("descripcionProducto")
                            val productoNow = Producto(nombreProducto, descripcionProducto)
                            listaProducto.add(productoNow)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        callback(listaProducto)
                    }
                }
            } catch (ex: SQLException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        activityContext, "Error al obtener los datos", Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ERROR OBTENER DATOS", ex.message!!)
                    callback(emptyList())
                }
            }
        }
    }


    fun AddUser(user: String, pw: String) {
        CoroutineScope(Dispatchers.IO).launch {
            connectionSQL = ConnectionSQL()

            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "INSERT INTO dbo.Usuario(usuario,pw)VALUES (?,?);"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, user)
                    statement.setString(2, pw)
                    statement.executeUpdate()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activityContext, "BD NULO", Toast.LENGTH_SHORT).show()
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        activityContext, "DATOS INGRESADOS A LA BD", Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ex: SQLException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        activityContext, "ERROR AL ENVIAR LOS DATOS", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    fun iniciarSesion(userIngresado: String, pwIngresado: String, navController: NavController) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = ConnectionSQL().dbConn()
                if (connection != null) {
                    val query = "SELECT usuario, pw FROM dbo.Usuario WHERE usuario = ?"
                    connection.prepareStatement(query).use { statement ->
                        statement.setString(1, userIngresado)
                        val result = statement.executeQuery()

                        while (result.next()) {
                            val usuario = result.getString("usuario")
                            val pw = result.getString("pw")
                            CoroutineScope(Dispatchers.Main).launch {
                                if (userIngresado.isBlank() || pwIngresado.isBlank()) {
                                    AlertDialog.Builder(activityContext).setTitle("Campos vacíos")
                                        .setMessage("Debe ingresar sus credenciales para iniciar sesión.")
                                        .setPositiveButton("Aceptar", null).show()
                                    teclado()
                                } else {
                                    if (userIngresado == usuario && pwIngresado == pw) {
                                        teclado()
                                        navController.navigate(R.id.action_fragmentLogin_to_fragmenMenu)
                                        (activityContext as MainActivity).findViewById<BottomNavigationView>(
                                            R.id.BarraNavegacion
                                        ).isVisible = true
                                    } else {
                                        AlertDialog.Builder(activityContext)
                                            .setTitle("Credenciales incorrectas")
                                            .setMessage("Los datos ingresados no coinciden con los datos de la base de datos.")
                                            .setPositiveButton("Aceptar", null).show()
                                    }
                                }
                            }
                        }
                    }
                    connection.close()
                }
            } catch (ex: SQLException) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        activityContext, "Error al obtener los datos", Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ERROR", ex.message!!)
                }
            }
        }

    }

    fun agrearProducto(producto: String, descripcion: String) {
        CoroutineScope(Dispatchers.IO).launch {
            connectionSQL = ConnectionSQL()
            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query =
                        "INSERT INTO dbo.Producto(nombreProducto,descripcionProducto)VALUES(?,?)"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, producto)
                    statement.setString(2, descripcion)
                    statement.executeUpdate()

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            activityContext, "Producto guarado exitosamente", Toast.LENGTH_SHORT
                        ).show()
                    }

                    connection.close()
                }

            } catch (ex: SQLException) {

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        activityContext, "Error al agregar el producto a la BD", Toast.LENGTH_SHORT
                    ).show()
                }


            }


        }
    }

    fun actualizarProducto(producto: String, descripcion: String, filtrado: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                connectionSQL = ConnectionSQL()
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query =
                        "UPDATE dbo.Producto SET nombreProducto =? ,descripcionProducto=? WHERE nombreProducto =?"
                    connection.prepareStatement(query).use { statement ->
                        statement.setString(1, producto)
                        statement.setString(2, descripcion)
                        statement.setString(3, filtrado)
                        statement.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                activityContext,
                                "Prodcuto actualizado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }

            } catch (ex: SQLException) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        activityContext, "Error al actualizar el producto a la", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }


    }


    fun eliminarProducto(nombre: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            connectionSQL = ConnectionSQL()

            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "DELETE FROM dbo.Producto WHERE nombreProducto = ?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, nombre)
                    statement.executeUpdate()
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            activityContext, "Producto eliminado", Toast.LENGTH_SHORT
                        ).show()
                    }
                    callback(true)
                }
            } catch (ex: SQLException) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        activityContext, "Error al eliminar el producto", Toast.LENGTH_SHORT
                    ).show()
                    callback(false)
                }
            }
        }

    }

    fun mensaje() {
        Toast.makeText(
            activityContext, "Error al eliminar el producto", Toast.LENGTH_SHORT
        ).show()
    }


}