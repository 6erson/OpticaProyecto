/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.DetalleVentaDAO;
import com.lumen.optica.modelo.DetalleVenta;
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
public class DetalleVentaDAOImpl implements DetalleVentaDAO {

    @Override
    public boolean registrar(DetalleVenta detalle) {
        String sql = "INSERT INTO DETALLE_VENTA "
                + "(id_venta, id_producto, cantidad, "
                + "precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, detalle.getIdVenta());
                ps.setInt(2, detalle.getIdProducto());
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getPrecioUnitario());
                ps.setDouble(5, detalle.getSubtotal());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al registrar detalle de venta: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DetalleVenta> listarPorVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();

        String sql = "SELECT * FROM DETALLE_VENTA "
                + "WHERE id_venta = ? ORDER BY id_detalle";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return detalles;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idVenta);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        DetalleVenta detalle = new DetalleVenta();

                        detalle.setIdDetalle(
                                rs.getInt("id_detalle"));
                        detalle.setIdVenta(
                                rs.getInt("id_venta"));
                        detalle.setIdProducto(
                                rs.getInt("id_producto"));
                        detalle.setCantidad(
                                rs.getInt("cantidad"));
                        detalle.setPrecioUnitario(
                                rs.getDouble("precio_unitario"));
                        detalle.setSubtotal(
                                rs.getDouble("subtotal"));

                        detalles.add(detalle);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar detalles de venta: "
                    + e.getMessage());
        }

        return detalles;
    }
}