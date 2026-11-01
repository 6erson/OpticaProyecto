/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.MedicionDAO;
import com.lumen.optica.impldao.MedicionDAOImpl;
import com.lumen.optica.modelo.Medicion;
import com.lumen.optica.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Alexander
 */
public class VentanaMediciones extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private final Usuario usuario;
    private final MedicionDAO medicionDAO;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    public VentanaMediciones(Usuario usuario) {
        this.usuario = usuario;
        medicionDAO = new MedicionDAOImpl();

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Fecha", "Esfera", "Cilindro", "Eje", "Adición"
                }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        configurarVentana();
        construirInterfaz();
        cargarMediciones();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Mis mediciones");
        setSize(850, 500);
        setMinimumSize(new java.awt.Dimension(650, 350));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout(15, 15));
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("MIS MEDICIONES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(AZUL);

        JButton botonActualizar = new JButton("Actualizar");
        botonActualizar.setBackground(TURQUESA);
        botonActualizar.setForeground(AZUL);
        botonActualizar.setFocusPainted(false);
        botonActualizar.addActionListener(e -> cargarMediciones());

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(BLANCO);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonActualizar, BorderLayout.EAST);

        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setForeground(AZUL);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13));
        tabla.getTableHeader().setForeground(AZUL);

        JScrollPane desplazamiento = new JScrollPane(tabla);

        JLabel nota = new JLabel(
                "Consulta tu historial de mediciones.",
                SwingConstants.LEFT);
        nota.setForeground(AZUL);

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(desplazamiento, BorderLayout.CENTER);
        fondo.add(nota, BorderLayout.SOUTH);

        setContentPane(fondo);
    }

    private void cargarMediciones() {
        modeloTabla.setRowCount(0);

        List<Medicion> mediciones =
                medicionDAO.listarPorUsuario(usuario.getIdUsuario());

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Medicion medicion : mediciones) {
            String fecha = medicion.getFechaMedicion()
                    .format(formatoFecha);

            String adicion = medicion.getAdicion() == null
                    ? "No aplica"
                    : String.format("%.2f", medicion.getAdicion());

            modeloTabla.addRow(new Object[]{
                fecha,
                String.format("%.2f", medicion.getEsfera()),
                String.format("%.2f", medicion.getCilindro()),
                medicion.getEje() + "°",
                adicion
            });
        }

        if (mediciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Todavía no tienes mediciones registradas.",
                    "Mis mediciones",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
