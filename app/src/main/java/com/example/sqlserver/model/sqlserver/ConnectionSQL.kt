package com.example.sqlserver.model.sqlserver

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionSQL {

    private val ip = "192.168.1.9:1433"
    private val db = "master"
    private val username = "sa"
    private val pw = "kotlinman"

    fun dbConn(): Connection? {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        val connString: String

        try {
            //controlador de JDBC
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            connString = "jdbc:jtds:sqlserver://$ip;databaseName=$db;user=$username;password=$pw"
            conn = DriverManager.getConnection(connString)
        } catch (ex: SQLException) {
            Log.e("ERROR_SQL", ex.message!!)
        } catch (ex1: ClassNotFoundException) {
            Log.e("ERROR_CLASS", ex1.message!!)
        } catch (ex2: Exception) {
            Log.e("ERROR_EXCEPTION", ex2.message!!)
        }

        return conn
    }
}
