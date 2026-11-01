/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.servicio;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.modelo.DetalleVenta;
import com.lumen.optica.modelo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
/**
 *
 * @author Alexander
 */
public class VentaServicio {

    public boolean registrarVenta(
            Venta venta, List<DetalleVenta> detalles) {

        // 1. Validaciones iniciales
        if (venta == null || detalles == null || detalles.isEmpty()) {
            return false;
        }

        if (venta.getIdCliente() <= 0
        || venta.getIdVendedor() <= 0) {
    return false;
}

if (venta.getIdMedicion() != null
        && venta.getIdMedicion() <= 0) {
    return false;
}

        double tasa = venta.getTasaImpuesto();

        if (!Double.isFinite(tasa) || tasa < 0) {
            return false;
        }

        for (DetalleVenta detalle : detalles) {
            if (detalle == null
                    || detalle.getIdProducto() <= 0
                    || detalle.getCantidad() <= 0) {
                return false;
            }
        }

        // 2. Consultas SQL
        String sqlUsuario =
                "SELECT rol FROM USUARIO "
                + "WITH (UPDLOCK, HOLDLOCK) "
                + "WHERE id_usuario = ?";

        String sqlMedicion =
                "SELECT id_medicion FROM MEDICION "
                + "WITH (UPDLOCK, HOLDLOCK) "
                + "WHERE id_medicion = ? AND id_usuario = ?";

        String sqlVenta =
                "INSERT INTO VENTA "
                + "(id_cliente, id_vendedor, id_medicion, "
                + "subtotal, tasa_impuesto, impuesto, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlProducto =
                "SELECT precio, stock, tipo FROM PRODUCTO "
                + "WITH (UPDLOCK, ROWLOCK) "
                + "WHERE id_producto = ? AND activo = 1";

        String sqlDetalle =
                "INSERT INTO DETALLE_VENTA "
                + "(id_venta, id_producto, cantidad, "
                + "precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        String sqlStock =
                "UPDATE PRODUCTO "
                + "SET stock = stock - ? "
                + "WHERE id_producto = ? "
                + "AND activo = 1 AND stock >= ?";

        String sqlActualizarVenta =
                "UPDATE VENTA SET subtotal = ?, impuesto = ?, "
                + "total = ? WHERE id_venta = ?";

        // 3. Abrir conexión y comenzar transacción
        try (Connection con = conexionDB.getConexion()) {

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            try {

                // 4. Validar que el cliente y vendedor existan
                // y tengan el rol correspondiente.

                try (PreparedStatement ps =
                        con.prepareStatement(sqlUsuario)) {

                    ps.setInt(1, venta.getIdCliente());

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()
                                || !"CLIENTE".equalsIgnoreCase(
                                        rs.getString("rol"))) {

                            throw new SQLException(
                                    "El usuario seleccionado no es "
                                    + "un cliente válido.");
                        }
                    }
                }

                try (PreparedStatement ps =
                        con.prepareStatement(sqlUsuario)) {

                    ps.setInt(1, venta.getIdVendedor());

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()
                                || !"VENDEDOR".equalsIgnoreCase(
                                        rs.getString("rol"))) {

                            throw new SQLException(
                                    "El usuario seleccionado no es "
                                    + "un vendedor válido.");
                        }
                    }
                }

// 5. Verificar la medición únicamente cuando exista.

if (venta.getIdMedicion() != null) {

    try (PreparedStatement ps =
            con.prepareStatement(sqlMedicion)) {

        ps.setInt(1, venta.getIdMedicion());
        ps.setInt(2, venta.getIdCliente());

        try (ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) {
                throw new SQLException(
                        "La medición no existe o no pertenece "
                        + "al cliente seleccionado.");
            }
        }
    }
}

                // 6. Registrar la cabecera de la venta.

                try (PreparedStatement psVenta =
                        con.prepareStatement(
                                sqlVenta,
                                java.sql.Statement.RETURN_GENERATED_KEYS)) {

                   psVenta.setInt(1, venta.getIdCliente());
psVenta.setInt(2, venta.getIdVendedor());

if (venta.getIdMedicion() != null) {
    psVenta.setInt(3, venta.getIdMedicion());
} else {
    psVenta.setNull(3, Types.INTEGER);
}

                    psVenta.setDouble(4, 0);
                    psVenta.setDouble(5, tasa);
                    psVenta.setDouble(6, 0);
                    psVenta.setDouble(7, 0);

                    psVenta.executeUpdate();

                    try (ResultSet rs = psVenta.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se obtuvo el ID de la venta.");
                        }

                        venta.setIdVenta(rs.getInt(1));
                    }
                }

                // 7. Registrar productos y descontar existencias.

                double subtotalVenta = 0;
               

                for (DetalleVenta detalle : detalles) {

                    double precio;
                    int stock;
                    String tipoProducto;

                    // Consultar producto y bloquear su registro.
                    try (PreparedStatement psProducto =
                            con.prepareStatement(sqlProducto)) {

                        psProducto.setInt(
                                1, detalle.getIdProducto());

                        try (ResultSet rs = psProducto.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                        "Producto inexistente o inactivo: "
                                        + detalle.getIdProducto());
                            }

                            precio = rs.getDouble("precio");
                            stock = rs.getInt("stock");
                            tipoProducto = rs.getString("tipo");
                        }
                    }

                    int cantidad = detalle.getCantidad();

                    // Verificar existencias.
                    if (stock < cantidad) {
                        throw new SQLException(
                                "Existencias insuficientes para el producto: "
                                + detalle.getIdProducto());
                    }

 

                    // Calcular subtotal del producto.
                    double subtotalDetalle = precio * cantidad;

                    subtotalVenta += subtotalDetalle;

                    // Registrar detalle de la venta.
                    try (PreparedStatement psDetalle =
                            con.prepareStatement(sqlDetalle)) {

                        psDetalle.setInt(1, venta.getIdVenta());
                        psDetalle.setInt(2, detalle.getIdProducto());
                        psDetalle.setInt(3, cantidad);
                        psDetalle.setDouble(4, precio);
                        psDetalle.setDouble(5, subtotalDetalle);

                        psDetalle.executeUpdate();
                    }

                    // Descontar existencias.
                    // Se aplica tanto a ARO como a LUNA y ACCESORIO.
                    try (PreparedStatement psStock =
                            con.prepareStatement(sqlStock)) {

                        psStock.setInt(1, cantidad);
                        psStock.setInt(2, detalle.getIdProducto());
                        psStock.setInt(3, cantidad);

                        if (psStock.executeUpdate() != 1) {
                            throw new SQLException(
                                    "No se pudo actualizar el stock del "
                                    + "producto: "
                                    + detalle.getIdProducto());
                        }
                    }

                    detalle.setIdVenta(venta.getIdVenta());
                    detalle.setPrecioUnitario(precio);
                    detalle.setSubtotal(subtotalDetalle);
                }



                // 9. Calcular impuesto y total.
                double impuesto = subtotalVenta * tasa;
                double total = subtotalVenta + impuesto;

                // 10. Actualizar importes de la venta.
                try (PreparedStatement ps =
                        con.prepareStatement(sqlActualizarVenta)) {

                    ps.setDouble(1, subtotalVenta);
                    ps.setDouble(2, impuesto);
                    ps.setDouble(3, total);
                    ps.setInt(4, venta.getIdVenta());

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo actualizar la venta.");
                    }
                }

                // 11. Confirmar toda la operación.
                con.commit();

                venta.setSubtotal(subtotalVenta);
                venta.setImpuesto(impuesto);
                venta.setTotal(total);

                return true;

            } catch (SQLException | RuntimeException e) {

                // Si algo falla, revertir todos los cambios.
                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }

                System.err.println(
                        "Error al registrar la venta: "
                        + e.getMessage());

                return false;

            } finally {

                // Restaurar el modo de confirmación.
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println(
                            "Error al restaurar la conexión: "
                            + e.getMessage());
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error de conexión al registrar venta: "
                    + e.getMessage());

            return false;
        }
    }
}