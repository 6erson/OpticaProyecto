/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.servicio;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import com.lumen.optica.modelo.Usuario;
/**
 *
 * @author Alexander
 */
public class UsuarioServicio {

    private final UsuarioDAOImpl usuarioDAO;
    private final SeguridadServicio seguridad;

    public UsuarioServicio() {
        usuarioDAO = new UsuarioDAOImpl();
        seguridad = new SeguridadServicio();
    }

    public boolean registrarUsuario(
            String nombre,
            String dni,
            String telefono,
            String correo,
            String contrasena,
            String rol) {

        if (nombre == null || dni == null || telefono == null
                || correo == null || contrasena == null || rol == null) {
            return false;
        }

        nombre = nombre.trim();
        dni = dni.trim();
        telefono = telefono.trim();
        correo = correo.trim();

        if (nombre.isEmpty() || dni.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()
                || contrasena.isEmpty()) {
            return false;
        }

        if (!rol.equals("CLIENTE") && !rol.equals("VENDEDOR")) {
            return false;
        }

        if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return false;
        }

        if (usuarioDAO.buscarPorCorreo(correo) != null) {
            return false;
        }

        String hash = seguridad.generarHash(contrasena);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(hash);
        usuario.setRol(rol);

        return usuarioDAO.registrar(usuario);
    }
}