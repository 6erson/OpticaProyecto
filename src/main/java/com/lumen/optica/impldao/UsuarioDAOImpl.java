/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO "
                + "(nombre, dni, telefono, correo, contrasena_hash, rol) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getDni());
                ps.setString(3, usuario.getTelefono());
                ps.setString(4, usuario.getCorreo());
                ps.setString(5, usuario.getContrasenaHash());
                ps.setString(6, usuario.getRol());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre, dni, telefono, "
                + "correo, contrasena_hash, rol "
                + "FROM USUARIO WHERE correo = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, correo);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(
                                rs.getInt("id_usuario"),
                                rs.getString("nombre"),
                                rs.getString("dni"),
                                rs.getString("telefono"),
                                rs.getString("correo"),
                                rs.getString("contrasena_hash"),
                                rs.getString("rol")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre, dni, telefono, "
                + "correo, contrasena_hash, rol "
                + "FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(
                                rs.getInt("id_usuario"),
                                rs.getString("nombre"),
                                rs.getString("dni"),
                                rs.getString("telefono"),
                                rs.getString("correo"),
                                rs.getString("contrasena_hash"),
                                rs.getString("rol")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Usuario> listarClientes() {
        List<Usuario> clientes = new ArrayList<>();

        String sql = "SELECT id_usuario, nombre, dni, telefono, "
                + "correo, contrasena_hash, rol "
                + "FROM USUARIO WHERE rol = 'CLIENTE' "
                + "ORDER BY nombre";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return clientes;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("dni"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getString("contrasena_hash"),
                            rs.getString("rol")
                    );

                    clientes.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: "
                    + e.getMessage());
        }

        return clientes;
    }
    
    @Override
public List<Usuario> listarVendedores() {
    List<Usuario> vendedores = new ArrayList<>();

    String sql = "SELECT id_usuario, nombre, dni, telefono, "
            + "correo, contrasena_hash, rol "
            + "FROM USUARIO WHERE rol = 'VENDEDOR' "
            + "ORDER BY nombre";

    try (Connection con = conexionDB.getConexion()) {
        if (con == null) {
            return vendedores;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("contrasena_hash"),
                        rs.getString("rol")
                );

                vendedores.add(usuario);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al listar vendedores: "
                + e.getMessage());
    }

    return vendedores;
}

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE USUARIO SET nombre = ?, dni = ?, "
                + "telefono = ?, correo = ?, rol = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getDni());
                ps.setString(3, usuario.getTelefono());
                ps.setString(4, usuario.getCorreo());
                ps.setString(5, usuario.getRol());
                ps.setInt(6, usuario.getIdUsuario());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: "
                    + e.getMessage());
            return false;
        }
    }
}