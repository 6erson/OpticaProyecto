/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.Medicion;
import java.util.List;
/**
 *
 * @author Alexander
 */
public interface MedicionDAO {

    boolean registrar(Medicion medicion);

    List<Medicion> listarPorUsuario(int idUsuario);

    Medicion buscarPorId(int idMedicion);

    boolean actualizar(Medicion medicion);

    boolean eliminar(int idMedicion);
}