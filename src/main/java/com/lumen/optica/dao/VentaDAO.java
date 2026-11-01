/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.Venta;
import java.util.List;
/**
 *
 * @author Alexander
 */
public interface VentaDAO {

    boolean registrar(Venta venta);

    Venta buscarPorId(int idVenta);

    List<Venta> listarTodas();

    List<Venta> listarPorCliente(int idCliente);

    List<Venta> listarPorVendedor(int idVendedor);
}