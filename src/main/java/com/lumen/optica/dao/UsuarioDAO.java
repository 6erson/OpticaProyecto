/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.dao;
import com.lumen.optica.modelo.Usuario;
import java.util.List;
/**
 *
 * @author Alexander
 */
public interface UsuarioDAO {

    boolean registrar(Usuario usuario);

    Usuario buscarPorCorreo(String correo);

    Usuario buscarPorId(int idUsuario);

    List<Usuario> listarClientes();

    List<Usuario> listarVendedores();

    boolean actualizar(Usuario usuario);

    boolean eliminar(int idUsuario);
}