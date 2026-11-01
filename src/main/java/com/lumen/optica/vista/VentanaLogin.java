/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.modelo.Usuario;
import com.lumen.optica.servicio.AutenticacionServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
/**
 *
 * @author Alexander
 */
public class VentanaLogin extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;

    private final AutenticacionServicio autenticacion;

    public VentanaLogin() {
        autenticacion = new AutenticacionServicio();

        configurarVentana();
        construirInterfaz();
        configurarEventos();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Iniciar sesión");
        setSize(480, 580);
        setMinimumSize(new Dimension(420, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 25, 40));

        JPanel encabezado = new JPanel(new GridBagLayout());
        encabezado.setBackground(BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel titulo = new JLabel(
                "LUMEN ÓPTICA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(AZUL);

        gbc.gridy = 0;
        encabezado.add(titulo, gbc);

        JLabel subtitulo = new JLabel(
                "Tu visión, nuestra prioridad",
                SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(AZUL);

        gbc.gridy = 1;
        encabezado.add(subtitulo, gbc);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO);

        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 0;
        fc.weightx = 1;
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.insets = new Insets(8, 0, 4, 0);

        JLabel etiquetaCorreo = new JLabel("Correo electrónico");
        etiquetaCorreo.setForeground(AZUL);
        etiquetaCorreo.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        fc.gridy = 0;
        formulario.add(etiquetaCorreo, fc);

        campoCorreo = new JTextField();
        campoCorreo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campoCorreo.setPreferredSize(new Dimension(300, 42));
        campoCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        fc.gridy = 1;
        formulario.add(campoCorreo, fc);

        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setForeground(AZUL);
        etiquetaContrasena.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        fc.gridy = 2;
        formulario.add(etiquetaContrasena, fc);

        campoContrasena = new JPasswordField();
        campoContrasena.setFont(
                new Font("SansSerif", Font.PLAIN, 15));
        campoContrasena.setPreferredSize(new Dimension(300, 42));
        campoContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        fc.gridy = 3;
        formulario.add(campoContrasena, fc);

        botonIngresar = new JButton("INICIAR SESIÓN");
        botonIngresar.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        botonIngresar.setForeground(AZUL);
        botonIngresar.setBackground(TURQUESA);
        botonIngresar.setFocusPainted(false);
        botonIngresar.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR));
        botonIngresar.setPreferredSize(new Dimension(300, 45));
        botonIngresar.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15));
        botonIngresar.setOpaque(true);

        fc.gridy = 4;
        fc.insets = new Insets(22, 0, 5, 0);
        formulario.add(botonIngresar, fc);

        // Panel para el registro de nuevos clientes.
        JPanel panelRegistro = new JPanel();
        panelRegistro.setBackground(BLANCO);

        JLabel textoRegistro = new JLabel("¿No tienes cuenta?");
        textoRegistro.setForeground(AZUL);
        textoRegistro.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        JButton botonRegistro = new JButton("Regístrate");
        botonRegistro.setForeground(AZUL);
        botonRegistro.setBackground(BLANCO);
        botonRegistro.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        botonRegistro.setBorderPainted(false);
        botonRegistro.setContentAreaFilled(false);
        botonRegistro.setFocusPainted(false);
        botonRegistro.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR));

        panelRegistro.add(textoRegistro);
        panelRegistro.add(botonRegistro);

        fc.gridy = 5;
        fc.insets = new Insets(4, 0, 5, 0);
        formulario.add(panelRegistro, fc);

        JLabel pie = new JLabel(
                "Acceso seguro para clientes y vendedores",
                SwingConstants.CENTER);
        pie.setForeground(new Color(90, 105, 125));
        pie.setFont(new Font("SansSerif", Font.PLAIN, 11));

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(formulario, BorderLayout.CENTER);
        fondo.add(pie, BorderLayout.SOUTH);

        setContentPane(fondo);

        // Abrir la ventana de registro al pulsar el botón.
        botonRegistro.addActionListener(e -> {
            VentanaRegistro ventana = new VentanaRegistro();
            ventana.setLocationRelativeTo(this);
            ventana.setVisible(true);
        });
    }

    private void configurarEventos() {
        botonIngresar.addActionListener(e -> iniciarSesion());
        campoContrasena.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correo = campoCorreo.getText().trim();
        char[] caracteres = campoContrasena.getPassword();

        try {
            if (correo.isEmpty() || caracteres.length == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingresa tu correo y contraseña.",
                        "Campos obligatorios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String contrasena = new String(caracteres);

            Usuario usuario = autenticacion.iniciarSesion(
                    correo, contrasena);

            if (usuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("CLIENTE".equalsIgnoreCase(usuario.getRol())) {
                PanelCliente panel = new PanelCliente(usuario);
                panel.setVisible(true);
                dispose();

} else if ("VENDEDOR".equalsIgnoreCase(usuario.getRol())) {
    PanelVendedor panel = new PanelVendedor(usuario);
    panel.setVisible(true);
    dispose();

} else {
    JOptionPane.showMessageDialog(
            this,
            "El rol de esta cuenta no es válido.",
            "Acceso denegado",
            JOptionPane.ERROR_MESSAGE);
}

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo completar el inicio de sesión. "
                            + "Verifica la conexión con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            System.err.println(
                    "Error de autenticación: " + ex.getMessage());

        } finally {
            java.util.Arrays.fill(caracteres, '\0');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                com.formdev.flatlaf.FlatLightLaf.setup();
            } catch (Exception e) {
                System.err.println(
                        "No se pudo aplicar FlatLaf: "
                                + e.getMessage());
            }

            new VentanaLogin().setVisible(true);
        });
    }
}