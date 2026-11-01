/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.ProductoDAO;
import com.lumen.optica.modelo.Producto;
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
public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public boolean registrar(Producto producto) {
        String sql = "INSERT INTO PRODUCTO "
                + "(nombre, tipo, marca, material, forma, precio, stock, activo, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getTipo());
                ps.setString(3, producto.getMarca());
                ps.setString(4, producto.getMaterial());
                ps.setString(5, producto.getForma());
                ps.setDouble(6, producto.getPrecio());
                ps.setInt(7, producto.getStock());
                ps.setBoolean(8, producto.isActivo());
                ps.setString(9, producto.getImagen());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar producto: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT * FROM PRODUCTO WHERE id_producto = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idProducto);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearProducto(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO ORDER BY nombre";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: "
                    + e.getMessage());
        }

        return productos;
    }

    @Override
    public List<Producto> listarActivos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO "
                + "WHERE activo = 1 ORDER BY nombre";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos activos: "
                    + e.getMessage());
        }

        return productos;
    }

    @Override
    public List<Producto> listarPorTipo(String tipo) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO "
                + "WHERE tipo = ? AND activo = 1 ORDER BY nombre";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tipo);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        productos.add(mapearProducto(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar por tipo: "
                    + e.getMessage());
        }

        return productos;
    }

    @Override
    public List<Producto> buscarPorFiltros(
            String marca,
            String material,
            String forma,
            double precioMinimo,
            double precioMaximo) {

        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM PRODUCTO "
                + "WHERE activo = 1 "
                + "AND (? IS NULL OR marca = ?) "
                + "AND (? IS NULL OR material = ?) "
                + "AND (? IS NULL OR forma = ?) "
                + "AND precio BETWEEN ? AND ? "
                + "ORDER BY nombre";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, marca);
                ps.setString(2, marca);
                ps.setString(3, material);
                ps.setString(4, material);
                ps.setString(5, forma);
                ps.setString(6, forma);
                ps.setDouble(7, precioMinimo);
                ps.setDouble(8, precioMaximo);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        productos.add(mapearProducto(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al filtrar productos: "
                    + e.getMessage());
        }

        return productos;
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE PRODUCTO SET "
                + "nombre = ?, tipo = ?, marca = ?, material = ?, "
                + "forma = ?, precio = ?, stock = ?, activo = ?, imagen = ? "
                + "WHERE id_producto = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getTipo());
                ps.setString(3, producto.getMarca());
                ps.setString(4, producto.getMaterial());
                ps.setString(5, producto.getForma());
                ps.setDouble(6, producto.getPrecio());
                ps.setInt(7, producto.getStock());
                ps.setBoolean(8, producto.isActivo());
                ps.setString(9, producto.getImagen());
                ps.setInt(10, producto.getIdProducto());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM PRODUCTO WHERE id_producto = ?";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idProducto);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> listarArosConStockBajo() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM PRODUCTO "
                + "WHERE tipo = 'ARO' AND activo = 1 AND stock < 5 "
                + "ORDER BY stock ASC";

        try (Connection con = conexionDB.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar stock bajo: "
                    + e.getMessage());
        }

        return productos;
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