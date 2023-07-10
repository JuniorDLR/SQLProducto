package com.example.sqlserver.model

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sqlserver.R
import com.example.sqlserver.model.sqlserver.ConnectionSQL
import com.example.sqlserver.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.SQLException
import kotlin.concurrent.thread

class ProviderProducto() {
    private lateinit var connectionSQL: ConnectionSQL
    private lateinit var activityContext: Activity
    val listaProducto = mutableListOf<Producto>()


    fun setActivity(activity: Activity) {
        activityContext = activity
    }

    fun obtenerProducto(callback: (List<Producto?>) -> Unit) {
        connectionSQL = ConnectionSQL()
        thread {
            try {
                var nombreProducto: String
                var descripcionProducto: String
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "SELECT nombreProducto, descripcionProducto FROM dbo.Producto"
                    connection.prepareStatement(query).use { producto ->

                        val next = producto.executeQuery()

                        while (next.next()) {
                            nombreProducto = next.getString("nombreProducto")
                            descripcionProducto = next.getString("descripcionProducto")
                            val productoNow = Producto(nombreProducto, descripcionProducto)
                            listaProducto.add(productoNow)
                        }
                    }
                    callback(listaProducto)
                }


            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "Error al obtener los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ERROR OBTENER DATOS", ex.message!!)
                    callback(emptyList())
                }
            }
        }
    }


    fun AddUser(user: String, pw: String) {
        connectionSQL = ConnectionSQL()
        Thread {
            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "INSERT INTO dbo.Usuario(usuario,pw)VALUES (?,?);"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, user)
                    statement.setString(2, pw)
                    statement.executeUpdate()
                } else {
                    activityContext.runOnUiThread {
                        Toast.makeText(activityContext, "BD NULO", Toast.LENGTH_SHORT).show()
                    }
                }

                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "DATOS INGRESADOS A LA BD",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "ERROR AL ENVIAR LOS DATOS",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("ERROR_SQL", ex.message!!)
            }
        }.start()
    }


    fun iniciarSesion(userIngresado: String, pwIngresado: String, navController: NavController) {
        Thread {
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
                            activityContext.runOnUiThread {
                                if (userIngresado.isBlank() || pwIngresado.isBlank()) {
                                    AlertDialog.Builder(activityContext)
                                        .setTitle("Campos vacíos")
                                        .setMessage("Debe ingresar sus credenciales para iniciar sesión.")
                                        .setPositiveButton("Aceptar", null)
                                        .show()
                                } else {
                                    if (userIngresado == usuario && pwIngresado == pw) {
                                        navController.navigate(R.id.action_fragmentLogin_to_fragmenMenu)
                                        (activityContext as MainActivity).findViewById<BottomNavigationView>(
                                            R.id.BarraNavegacion
                                        ).isVisible =
                                            true
                                    } else {
                                        AlertDialog.Builder(activityContext)
                                            .setTitle("Credenciales incorrectas")
                                            .setMessage("Los datos ingresados no coinciden con los datos de la base de datos.")
                                            .setPositiveButton("Aceptar", null)
                                            .show()
                                    }
                                }
                            }
                        }
                    }
                    connection.close()
                }
            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "Error al obtener los datos",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.e("ERROR", ex.message!!)
                }
            }
        }.start()
    }

    fun agrearProducto(producto: String, descripcion: String) {
        connectionSQL = ConnectionSQL()
        Thread {
            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query =
                        "INSERT INTO dbo.Producto(nombreProducto,descripcionProducto)VALUES(?,?)"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, producto)
                    statement.setString(2, descripcion)
                    statement.executeUpdate()

                    activityContext.runOnUiThread {
                        Toast.makeText(
                            activityContext,
                            "Producto guarado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    connection.close()
                }

            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "Error al agregar el producto a la BD",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("ERROR_SQL", ex.message!!)
            }
        }.start()

    }

    fun actualizarProducto(producto: String, descripcion: String, filtrado: String) {
        connectionSQL = ConnectionSQL()
        Thread {
            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query =
                        "UPDATE dbo.Producto SET nombreProducto =? ,descripcionProducto=? WHERE nombreProducto =?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, producto)
                    statement.setString(2, descripcion)
                    statement.setString(3, filtrado)
                    statement.executeUpdate()

                    activityContext.runOnUiThread {
                        Toast.makeText(
                            activityContext,
                            "Producto  actualizado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    connection.close()
                }

            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "Error al agregar el producto a la BD",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("ERROR_SQL", ex.message!!)
            }
        }.start()

    }


    fun eliminarProducto(nombre: String, callback: (Boolean) -> Unit) {
        connectionSQL = ConnectionSQL()
        val thread = Thread {
            try {
                val connection = connectionSQL.dbConn()
                if (connection != null) {
                    val query = "DELETE FROM dbo.Producto WHERE nombreProducto = ?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, nombre)
                    statement.executeUpdate()
                    activityContext.runOnUiThread {
                        Toast.makeText(
                            activityContext,
                            "Producto eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    callback(true)
                }
            } catch (ex: SQLException) {
                activityContext.runOnUiThread {
                    Toast.makeText(
                        activityContext,
                        "Error al eliminar el producto",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e("ERROR", ex.message!!)
                    callback(false)
                }
            }
        }
        thread.start()
    }

    fun mensaje() {
        Toast.makeText(
            activityContext,
            "Error al eliminar el producto",
            Toast.LENGTH_SHORT
        ).show()
    }


}