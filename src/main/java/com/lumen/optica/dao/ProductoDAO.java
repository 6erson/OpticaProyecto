/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.Producto;
import java.util.List;

/**
 *
 * @author Alexander
 */
public interface ProductoDAO {

    boolean registrar(Producto producto);

    Producto buscarPorId(int idProducto);

    List<Producto> listarTodos();

    List<Producto> listarActivos();

    List<Producto> listarPorTipo(String tipo);

    List<Producto> buscarPorFiltros(
            String marca,
            String material,
            String forma,
            double precioMinimo,
            double precioMaximo);

    boolean actualizar(Producto producto);

    boolean eliminar(int idProducto);

    List<Producto> listarArosConStockBajo();
}