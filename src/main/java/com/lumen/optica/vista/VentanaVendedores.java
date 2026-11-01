/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import com.lumen.optica.modelo.Usuario;
import com.lumen.optica.servicio.UsuarioServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Alexander
 */
public class VentanaVendedores extends JFrame {

    private final Color VERDE = new Color(21, 94, 82);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);
    private final Color BLANCO = Color.WHITE;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final UsuarioServicio usuarioServicio = new UsuarioServicio();

    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    private JTable tabla;
    private DefaultTableModel modelo;

    private int idVendedorSeleccionado = -1;

    public VentanaVendedores() {
        configurarVentana();
        construirInterfaz();
        cargarVendedores();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Gestión de vendedores");
        setSize(1000, 680);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(15, 15));
    }

    private void construirInterfaz() {
        JLabel titulo = new JLabel("GESTIÓN DE VENDEDORES",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE);
        titulo.setBorder(BorderFactory.createEmptyBorder(22, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setBackground(FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(5, 22, 20, 22));

        centro.add(crearFormulario(), BorderLayout.NORTH);
        centro.add(crearTabla(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBackground(BLANCO);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 232, 229)),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)));

        JLabel subtitulo = new JLabel("Datos del vendedor");
        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        subtitulo.setForeground(VERDE);
        contenedor.add(subtitulo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(0, 2, 12, 9));
        campos.setBackground(BLANCO);

        txtNombre = crearCampo();
        txtDni = crearCampo();
        txtTelefono = crearCampo();
        txtCorreo = crearCampo();
        txtContrasena = new JPasswordField();
        estilizarCampo(txtContrasena);

        agregarCampo(campos, "Nombre completo", txtNombre);
        agregarCampo(campos, "DNI", txtDni);
        agregarCampo(campos, "Teléfono", txtTelefono);
        agregarCampo(campos, "Correo", txtCorreo);
        agregarCampo(campos, "Contraseña (solo para nuevo vendedor)",
                txtContrasena);

        contenedor.add(campos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botones.setBackground(BLANCO);

        JButton registrar = crearBoton("REGISTRAR VENDEDOR");
        registrar.addActionListener(e -> registrarVendedor());

        JButton actualizar = crearBoton("ACTUALIZAR DATOS");
        actualizar.addActionListener(e -> actualizarVendedor());

        JButton eliminar = crearBoton("ELIMINAR VENDEDOR");
        eliminar.addActionListener(e -> eliminarVendedor());

        JButton limpiar = crearBoton("LIMPIAR");
        limpiar.addActionListener(e -> limpiarFormulario());

        botones.add(registrar);
        botones.add(actualizar);
        botones.add(eliminar);
        botones.add(limpiar);

        contenedor.add(botones, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearTabla() {
        String[] columnas = {"ID", "Nombre", "DNI", "Teléfono", "Correo", "Rol"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(27);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario();
            }
        });

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(FONDO);

        JLabel subtitulo = new JLabel("Vendedores registrados");
        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        subtitulo.setForeground(VERDE);
        panel.add(subtitulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton recargar = crearBoton("ACTUALIZAR LISTA");
        recargar.addActionListener(e -> cargarVendedores());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acciones.setOpaque(false);
        acciones.add(recargar);
        panel.add(acciones, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(250, 30));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo) {
        JPanel grupo = new JPanel(new BorderLayout(3, 3));
        grupo.setBackground(BLANCO);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(75, 90, 85));

        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);
        panel.add(grupo);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(VERDE);
        boton.setBackground(TURQUESA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void registrarVendedor() {
        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()
                || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completa todos los campos para registrar al vendedor.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean registrado = usuarioServicio.registrarUsuario(
                nombre, dni, telefono, correo, contrasena, "VENDEDOR");

        if (registrado) {
            JOptionPane.showMessageDialog(this,
                    "Vendedor registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarVendedores();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el vendedor. Verifica que el correo "
                    + "y el DNI no estén registrados y que los datos sean válidos.",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVendedor() {
        if (idVendedorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un vendedor de la tabla.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || dni.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completa nombre, DNI, teléfono y correo.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario vendedor = usuarioDAO.buscarPorId(idVendedorSeleccionado);
        if (vendedor == null || !"VENDEDOR".equals(vendedor.getRol())) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la cuenta de vendedor seleccionada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            cargarVendedores();
            limpiarFormulario();
            return;
        }

        vendedor.setNombre(nombre);
        vendedor.setDni(dni);
        vendedor.setTelefono(telefono);
        vendedor.setCorreo(correo);
        vendedor.setRol("VENDEDOR");

        if (usuarioDAO.actualizar(vendedor)) {
            JOptionPane.showMessageDialog(this,
                    "Datos del vendedor actualizados correctamente.",
                    "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarVendedores();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron actualizar los datos. Comprueba que el "
                    + "correo y el DNI no estén duplicados.",
                    "Error al actualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVendedor() {
        if (idVendedorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un vendedor de la tabla.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este vendedor? "
                + "La base de datos puede impedirlo si tiene ventas asociadas.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (usuarioDAO.eliminar(idVendedorSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                    "Vendedor eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarVendedores();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el vendedor. Es posible que tenga "
                    + "ventas u otros registros relacionados.",
                    "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarVendedores() {
        modelo.setRowCount(0);
        List<Usuario> vendedores = usuarioDAO.listarVendedores();

        for (Usuario vendedor : vendedores) {
            modelo.addRow(new Object[]{
                vendedor.getIdUsuario(),
                vendedor.getNombre(),
                vendedor.getDni(),
                vendedor.getTelefono(),
                vendedor.getCorreo(),
                vendedor.getRol()
            });
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        idVendedorSeleccionado =
                Integer.parseInt(modelo.getValueAt(fila, 0).toString());

        txtNombre.setText(modelo.getValueAt(fila, 1).toString());
        txtDni.setText(modelo.getValueAt(fila, 2).toString());
        txtTelefono.setText(modelo.getValueAt(fila, 3).toString());
        txtCorreo.setText(modelo.getValueAt(fila, 4).toString());
        txtContrasena.setText("");
    }

    private void limpiarFormulario() {
        idVendedorSeleccionado = -1;
        txtNombre.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        tabla.clearSelection();
    }
}

