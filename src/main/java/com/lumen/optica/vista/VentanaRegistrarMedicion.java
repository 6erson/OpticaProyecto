/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.MedicionDAO;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.impldao.MedicionDAOImpl;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import com.lumen.optica.modelo.Medicion;
import com.lumen.optica.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Alexander
 */
public class VentanaRegistrarMedicion extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private final JComboBox<ClienteOpcion> comboClientes;
    private final JTextField campoFecha;
    private final JTextField campoEsfera;
    private final JTextField campoCilindro;
    private final JTextField campoEje;
    private final JTextField campoAdicion;

    private final UsuarioDAO usuarioDAO;
    private final MedicionDAO medicionDAO;

    public VentanaRegistrarMedicion() {
        usuarioDAO = new UsuarioDAOImpl();
        medicionDAO = new MedicionDAOImpl();

        comboClientes = new JComboBox<>();
        campoFecha = new JTextField(LocalDate.now().toString());
        campoEsfera = new JTextField();
        campoCilindro = new JTextField();
        campoEje = new JTextField();
        campoAdicion = new JTextField();

        configurarVentana();
        construirInterfaz();
        cargarClientes();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Registrar medición");
        setSize(600, 540);
        setMinimumSize(new Dimension(500, 480));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout(15, 15));
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titulo = new JLabel("REGISTRAR MEDICIÓN");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(AZUL);

        JPanel formulario = new JPanel(new GridLayout(6, 2, 12, 15));
        formulario.setBackground(BLANCO);

        agregarCampo(formulario, "Cliente:", comboClientes);
        agregarCampo(formulario, "Fecha (AAAA-MM-DD):", campoFecha);
        agregarCampo(formulario, "Esfera:", campoEsfera);
        agregarCampo(formulario, "Cilindro:", campoCilindro);
        agregarCampo(formulario, "Eje (0–180):", campoEje);
        agregarCampo(formulario, "Adición (opcional):", campoAdicion);

        JButton botonRegistrar = new JButton("REGISTRAR MEDICIÓN");
        botonRegistrar.setBackground(TURQUESA);
        botonRegistrar.setForeground(AZUL);
        botonRegistrar.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.addActionListener(e -> registrarMedicion());

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(formulario, BorderLayout.CENTER);
        fondo.add(botonRegistrar, BorderLayout.SOUTH);

        setContentPane(fondo);
    }

    private void agregarCampo(
            JPanel panel, String texto, java.awt.Component campo) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(AZUL);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panel.add(etiqueta);
        panel.add(campo);
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();

        List<Usuario> clientes = usuarioDAO.listarClientes();

        for (Usuario cliente : clientes) {
            comboClientes.addItem(new ClienteOpcion(
                    cliente.getIdUsuario(),
                    cliente.getNombre()));
        }

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay clientes registrados.",
                    "Registro de mediciones",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void registrarMedicion() {
        ClienteOpcion cliente =
                (ClienteOpcion) comboClientes.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un cliente.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(
                    campoFecha.getText().trim());

            double esfera = Double.parseDouble(
                    campoEsfera.getText().trim().replace(',', '.'));

            double cilindro = Double.parseDouble(
                    campoCilindro.getText().trim().replace(',', '.'));

            int eje = Integer.parseInt(campoEje.getText().trim());

            Double adicion = null;

            if (!campoAdicion.getText().trim().isEmpty()) {
                adicion = Double.parseDouble(
                        campoAdicion.getText().trim().replace(',', '.'));
            }

            if (eje < 0 || eje > 180) {
                JOptionPane.showMessageDialog(
                        this,
                        "El eje debe estar entre 0 y 180.",
                        "Valor inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Medicion medicion = new Medicion();
            medicion.setIdUsuario(cliente.getIdUsuario());
            medicion.setFechaMedicion(fecha);
            medicion.setEsfera(esfera);
            medicion.setCilindro(cilindro);
            medicion.setEje(eje);
            medicion.setAdicion(adicion);

            if (medicionDAO.registrar(medicion)) {
                JOptionPane.showMessageDialog(
                        this,
                        "La medición se registró correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la medición.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato AAAA-MM-DD.",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Revisa los valores numéricos de la medición.",
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        campoFecha.setText(LocalDate.now().toString());
        campoEsfera.setText("");
        campoCilindro.setText("");
        campoEje.setText("");
        campoAdicion.setText("");
        comboClientes.setSelectedIndex(-1);
    }

    private static class ClienteOpcion {

        private final int id;
        private final String nombre;

        public ClienteOpcion(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }

        public int getIdUsuario() {
            return id;
        }
    }
}