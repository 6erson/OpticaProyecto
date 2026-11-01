/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.servicio.UsuarioServicio;
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

/**
 *
 * @author Alexander
 */
public class VentanaRegistro extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private JTextField campoNombre;
    private JTextField campoDni;
    private JTextField campoTelefono;
    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmar;

    private final UsuarioServicio usuarioServicio;

    public VentanaRegistro() {
        usuarioServicio = new UsuarioServicio();

        configurarVentana();
        construirInterfaz();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Crear cuenta");
        setSize(480, 700);
        setMinimumSize(new Dimension(440, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout(0, 15));
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel(
                "CREAR CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(AZUL);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 3, 0);

        campoNombre = crearCampo();
        campoDni = crearCampo();
        campoTelefono = crearCampo();
        campoCorreo = crearCampo();
        campoContrasena = crearCampoContrasena();
        campoConfirmar = crearCampoContrasena();

        agregarCampo(formulario, gbc, "Nombre completo",
                campoNombre, 0);
        agregarCampo(formulario, gbc, "DNI",
                campoDni, 1);
        agregarCampo(formulario, gbc, "Teléfono",
                campoTelefono, 2);
        agregarCampo(formulario, gbc, "Correo electrónico",
                campoCorreo, 3);
        agregarCampo(formulario, gbc, "Contraseña",
                campoContrasena, 4);
        agregarCampo(formulario, gbc, "Confirmar contraseña",
                campoConfirmar, 5);

        JButton botonRegistrar = new JButton("CREAR CUENTA");
        botonRegistrar.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        botonRegistrar.setForeground(AZUL);
        botonRegistrar.setBackground(TURQUESA);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setOpaque(true);
        botonRegistrar.setPreferredSize(new Dimension(300, 43));
        botonRegistrar.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15));

        gbc.gridy = 12;
        gbc.insets = new Insets(18, 0, 5, 0);
        formulario.add(botonRegistrar, gbc);

        JButton botonVolver = new JButton("Volver al inicio de sesión");
        botonVolver.setForeground(AZUL);
        botonVolver.setBackground(BLANCO);
        botonVolver.setFocusPainted(false);
        botonVolver.setBorderPainted(false);

        gbc.gridy = 13;
        gbc.insets = new Insets(5, 0, 0, 0);
        formulario.add(botonVolver, gbc);

        botonRegistrar.addActionListener(e -> registrar());
        botonVolver.addActionListener(e -> dispose());

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(formulario, BorderLayout.CENTER);

        setContentPane(fondo);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 220)),
                BorderFactory.createEmptyBorder(6, 9, 6, 9)));
        return campo;
    }

    private JPasswordField crearCampoContrasena() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 220)),
                BorderFactory.createEmptyBorder(6, 9, 6, 9)));
        return campo;
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            String texto,
            JTextField campo,
            int fila) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(AZUL);
        etiqueta.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        gbc.gridy = fila * 2;
        gbc.insets = new Insets(5, 0, 2, 0);
        panel.add(etiqueta, gbc);

        gbc.gridy = fila * 2 + 1;
        gbc.insets = new Insets(0, 0, 3, 0);
        panel.add(campo, gbc);
    }

    private void registrar() {
        char[] clave = campoContrasena.getPassword();
        char[] confirmacion = campoConfirmar.getPassword();

        try {
            if (!java.util.Arrays.equals(clave, confirmacion)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Las contraseñas no coinciden.",
                        "Verifica los datos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (clave.length < 8) {
                JOptionPane.showMessageDialog(
                        this,
                        "La contraseña debe tener al menos 8 caracteres.",
                        "Contraseña no válida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean registrado = usuarioServicio.registrarUsuario(
                    campoNombre.getText(),
                    campoDni.getText(),
                    campoTelefono.getText(),
                    campoCorreo.getText(),
                    new String(clave),
                    "CLIENTE");

            if (registrado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tu cuenta se ha creado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo crear la cuenta. "
                        + "Verifica los datos y que el correo "
                        + "y DNI no estén registrados.",
                        "No se pudo registrar",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocurrió un error al registrar la cuenta. "
                    + "Comprueba la conexión con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            System.err.println(
                    "Error de registro: " + ex.getMessage());

        } finally {
            java.util.Arrays.fill(clave, '\0');
            java.util.Arrays.fill(confirmacion, '\0');
        }
    }
}