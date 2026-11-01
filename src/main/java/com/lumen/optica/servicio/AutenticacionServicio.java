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
public class AutenticacionServicio {

    private final UsuarioDAOImpl usuarioDAO;
    private final SeguridadServicio seguridad;

    public AutenticacionServicio() {
        usuarioDAO = new UsuarioDAOImpl();
        seguridad = new SeguridadServicio();
    }

    public Usuario iniciarSesion(String correo, String contrasena) {

        if (correo == null || contrasena == null
                || correo.trim().isEmpty() || contrasena.isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim());

        if (usuario == null) {
            return null;
        }

        boolean contrasenaCorrecta = seguridad.verificarContrasena(
                contrasena, usuario.getContrasenaHash());

        if (contrasenaCorrecta) {
            return usuario;
        }

        return null;
    }

    public boolean tieneRol(Usuario usuario, String rolEsperado) {

        if (usuario == null || usuario.getRol() == null
                || rolEsperado == null) {
            return false;
        }

        return usuario.getRol().equalsIgnoreCase(rolEsperado);
    }
}