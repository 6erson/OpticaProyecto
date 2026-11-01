/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.impldao;
import com.lumen.optica.conexion.conexionDB;
import com.lumen.optica.dao.MedicionDAO;
import com.lumen.optica.modelo.Medicion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Alexander
 */
public class MedicionDAOImpl implements MedicionDAO {

    @Override
    public boolean registrar(Medicion medicion) {
        String sql = "INSERT INTO MEDICION "
                + "(id_usuario, fecha_medicion, esfera, cilindro, eje, adicion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                return false;
            }

            ps.setInt(1, medicion.getIdUsuario());
            ps.setDate(2, Date.valueOf(medicion.getFechaMedicion()));
            ps.setDouble(3, medicion.getEsfera());
            ps.setDouble(4, medicion.getCilindro());
            ps.setInt(5, medicion.getEje());

            if (medicion.getAdicion() == null) {
                ps.setNull(6, Types.DECIMAL);
            } else {
                ps.setDouble(6, medicion.getAdicion());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error al registrar medición: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Medicion> listarPorUsuario(int idUsuario) {
        List<Medicion> mediciones = new ArrayList<>();

        String sql = "SELECT * FROM MEDICION "
                + "WHERE id_usuario = ? ORDER BY fecha_medicion DESC";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                return mediciones;
            }

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mediciones.add(mapearMedicion(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar mediciones: "
                    + e.getMessage());
        }

        return mediciones;
    }

    @Override
    public Medicion buscarPorId(int idMedicion) {
        String sql = "SELECT * FROM MEDICION WHERE id_medicion = ?";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                return null;
            }

            ps.setInt(1, idMedicion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMedicion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar medición: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean actualizar(Medicion medicion) {
        String sql = "UPDATE MEDICION SET "
                + "fecha_medicion = ?, esfera = ?, cilindro = ?, "
                + "eje = ?, adicion = ? "
                + "WHERE id_medicion = ?";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                return false;
            }

            ps.setDate(1, Date.valueOf(medicion.getFechaMedicion()));
            ps.setDouble(2, medicion.getEsfera());
            ps.setDouble(3, medicion.getCilindro());
            ps.setInt(4, medicion.getEje());

            if (medicion.getAdicion() == null) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setDouble(5, medicion.getAdicion());
            }

            ps.setInt(6, medicion.getIdMedicion());

            return ps.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error al actualizar medición: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idMedicion) {
        String sql = "DELETE FROM MEDICION WHERE id_medicion = ?";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                return false;
            }

            ps.setInt(1, idMedicion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar medición: "
                    + e.getMessage());
            return false;
        }
    }

    private Medicion mapearMedicion(ResultSet rs) throws SQLException {
        Medicion medicion = new Medicion();

        medicion.setIdMedicion(rs.getInt("id_medicion"));
        medicion.setIdUsuario(rs.getInt("id_usuario"));
        medicion.setFechaMedicion(
                rs.getDate("fecha_medicion").toLocalDate());
        medicion.setEsfera(rs.getDouble("esfera"));
        medicion.setCilindro(rs.getDouble("cilindro"));
        medicion.setEje(rs.getInt("eje"));

        double adicion = rs.getDouble("adicion");

        if (rs.wasNull()) {
            medicion.setAdicion(null);
        } else {
            medicion.setAdicion(adicion);
        }

        return medicion;
    }
}