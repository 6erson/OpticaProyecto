/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.VentaDAO;
import com.lumen.optica.modelo.Venta;
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
public class VentaDAOImpl implements VentaDAO {

    @Override
    public boolean registrar(Venta venta) {
        String sql = "INSERT INTO VENTA "
                + "(id_cliente, id_vendedor, id_medicion, "
                + "subtotal, tasa_impuesto, impuesto, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, venta.getIdCliente());
                ps.setInt(2, venta.getIdVendedor());

                if (venta.getIdMedicion() == null) {
                    ps.setNull(3, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(3, venta.getIdMedicion());
                }

                ps.setDouble(4, venta.getSubtotal());
                ps.setDouble(5, venta.getTasaImpuesto());
                ps.setDouble(6, venta.getImpuesto());
                ps.setDouble(7, venta.getTotal());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar venta: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public Venta buscarPorId(int idVenta) {
        String sql = "SELECT * FROM VENTA WHERE id_venta = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idVenta);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearVenta(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Venta> listarTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTA ORDER BY fecha_venta DESC";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return ventas;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ventas.add(mapearVenta(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ventas: "
                    + e.getMessage());
        }

        return ventas;
    }

    @Override
    public List<Venta> listarPorCliente(int idCliente) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTA "
                + "WHERE id_cliente = ? ORDER BY fecha_venta DESC";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return ventas;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ventas.add(mapearVenta(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar ventas del cliente: "
                    + e.getMessage());
        }

        return ventas;
    }

    @Override
    public List<Venta> listarPorVendedor(int idVendedor) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTA "
                + "WHERE id_vendedor = ? ORDER BY fecha_venta DESC";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return ventas;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idVendedor);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ventas.add(mapearVenta(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar ventas del vendedor: "
                    + e.getMessage());
        }

        return ventas;
    }

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();

        venta.setIdVenta(rs.getInt("id_venta"));

        Timestamp fecha = rs.getTimestamp("fecha_venta");
        if (fecha != null) {
            venta.setFechaVenta(fecha.toLocalDateTime());
        }

        venta.setIdCliente(rs.getInt("id_cliente"));
        venta.setIdVendedor(rs.getInt("id_vendedor"));

        int idMedicion = rs.getInt("id_medicion");
        if (rs.wasNull()) {
            venta.setIdMedicion(null);
        } else {
            venta.setIdMedicion(idMedicion);
        }

        venta.setSubtotal(rs.getDouble("subtotal"));
        venta.setTasaImpuesto(rs.getDouble("tasa_impuesto"));
        venta.setImpuesto(rs.getDouble("impuesto"));
        venta.setTotal(rs.getDouble("total"));

        return venta;
    }
}