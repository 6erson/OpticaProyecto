/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.DetalleVenta;
import java.util.List;
/**
 *
 * @author Alexander
 */
public interface DetalleVentaDAO {

    boolean registrar(DetalleVenta detalle);

    List<DetalleVenta> listarPorVenta(int idVenta);
}
