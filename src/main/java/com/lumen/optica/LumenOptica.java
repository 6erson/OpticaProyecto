/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.lumen.optica;
import com.lumen.optica.conexion.conexionDB;
import java.sql.Connection;
/**
 *
 * @author Alexander
 */
public class LumenOptica {

    public static void main(String[] args) {

        System.out.println("Iniciando Lumen Óptica...");

        Connection conexion = conexionDB.getConexion();

        if (conexion != null) {
            System.out.println("Prueba de conexión completada.");
            conexionDB.cerrarConexion();
        } else {
            System.out.println("No se pudo conectar a LumenDB.");
        }
    }
}