/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.AroGuardado;
import com.lumen.optica.modelo.Producto;
import java.util.List;
/**
 *
 * @author Alexander
 */
public interface AroGuardadoDAO {

    boolean guardar(AroGuardado aroGuardado);

    boolean quitar(int idUsuario, int idProducto);

    List<Producto> listarPorUsuario(int idUsuario);

    boolean existe(int idUsuario, int idProducto);
}