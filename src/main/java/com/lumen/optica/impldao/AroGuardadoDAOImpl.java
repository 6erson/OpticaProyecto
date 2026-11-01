/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.AroGuardadoDAO;
import com.lumen.optica.modelo.AroGuardado;
import com.lumen.optica.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander
 */
public class AroGuardadoDAOImpl implements AroGuardadoDAO {

    @Override
    public boolean guardar(AroGuardado aroGuardado) {
        String sql = "INSERT INTO ARO_GUARDADO "
                + "(id_usuario, id_producto) VALUES (?, ?)";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, aroGuardado.getIdUsuario());
                ps.setInt(2, aroGuardado.getIdProducto());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar aro favorito: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean quitar(int idUsuario, int idProducto) {
        String sql = "DELETE FROM ARO_GUARDADO "
                + "WHERE id_usuario = ? AND id_producto = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idProducto);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al quitar aro favorito: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> listarPorUsuario(int idUsuario) {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.* "
                + "FROM PRODUCTO p "
                + "INNER JOIN ARO_GUARDADO ag "
                + "ON p.id_producto = ag.id_producto "
                + "WHERE ag.id_usuario = ? "
                + "ORDER BY ag.fecha_guardado DESC";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        productos.add(mapearProducto(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar aros guardados: "
                    + e.getMessage());
        }

        return productos;
    }

    @Override
    public boolean existe(int idUsuario, int idProducto) {
        String sql = "SELECT COUNT(*) FROM ARO_GUARDADO "
                + "WHERE id_usuario = ? AND id_producto = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idProducto);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar aro guardado: "
                    + e.getMessage());
        }

        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();

        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setTipo(rs.getString("tipo"));
        producto.setMarca(rs.getString("marca"));
        producto.setMaterial(rs.getString("material"));
        producto.setForma(rs.getString("forma"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setImagen(rs.getString("imagen"));

        return producto;
    }
}
