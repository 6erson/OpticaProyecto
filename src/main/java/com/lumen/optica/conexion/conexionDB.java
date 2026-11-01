/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Alexander
 */
public class conexionDB {

    private static final String URL =
            "jdbc:sqlserver://optica-db.database.windows.net:1433;"
            + "databaseName=opticaDB;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "hostNameInCertificate=*.database.windows.net;"
            + "loginTimeout=30;";

    private static final String USER = "opticadb";

    private static final String PASSWORD = "UTH1234$";

    private static Connection conexion = null;

    private conexionDB() {
    }

    public static Connection getConexion() {
            try {
                if (conexion == null || conexion.isClosed()) {

                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                    conexion = DriverManager.getConnection(URL, USER, PASSWORD);

                    System.out.println("Conexión exitosa a Azure SQL.");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("No se encontró el controlador JDBC: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error al conectar con SQL Server en Azure: " + e.getMessage());
            }

            return conexion;
        }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;

                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println(
                    "Error al cerrar la conexión: "
                    + e.getMessage()
            );
        }
    }
}