/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.ProductoDAO;
import com.lumen.optica.impldao.ProductoDAOImpl;
import com.lumen.optica.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Alexander
 */
public class VentanaInventario extends JFrame {

    private final Color VERDE = new Color(21, 94, 82);
    private final Color VERDE_CLARO = new Color(32, 112, 96);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);

    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtMarca;
    private JTextField txtMaterial;
    private JTextField txtForma;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtImagen;
    private JCheckBox chkActivo;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private int idSeleccionado = 0;

    public VentanaInventario() {
        setTitle("Lumen Óptica - Inventario");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(FONDO);

        crearEncabezado();
        crearFormulario();
        crearTabla();
        crearBotonesInferiores();

        cargarProductos();
        mostrarAlertasStock();
    }

    private void crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERDE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("INVENTARIO DE PRODUCTOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 23));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("LUMEN ÓPTICA");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 12));
        subtitulo.setForeground(TURQUESA);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 5));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        JButton btnCerrar = crearBoton("CERRAR", VERDE_CLARO);
        btnCerrar.addActionListener(e -> dispose());

        panel.add(textos, BorderLayout.WEST);
        panel.add(btnCerrar, BorderLayout.EAST);

        add(panel, BorderLayout.NORTH);
    }

  private void crearFormulario() {
    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 8));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(VERDE),
                    "Datos del producto"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    txtNombre = new JTextField(15);
    cmbTipo = new JComboBox<>(
            new String[]{"ARO", "LUNA", "ACCESORIO"});
    txtMarca = new JTextField(15);
    txtMaterial = new JTextField(15);
    txtForma = new JTextField(15);
    txtPrecio = new JTextField(15);
    txtStock = new JTextField(15);
    txtImagen = new JTextField(15);

    chkActivo = new JCheckBox("Producto activo", true);
    chkActivo.setBackground(Color.WHITE);

    agregarCampo(panel, "Nombre:", txtNombre);
    agregarCampo(panel, "Tipo:", cmbTipo);
    agregarCampo(panel, "Marca:", txtMarca);
    agregarCampo(panel, "Material:", txtMaterial);
    agregarCampo(panel, "Forma:", txtForma);
    agregarCampo(panel, "Precio (L):", txtPrecio);
    agregarCampo(panel, "Existencias:", txtStock);
    agregarCampo(panel, "Ruta de imagen:", txtImagen);
    agregarCampo(panel, "Estado:", chkActivo);

    JButton btnRegistrar = crearBoton("REGISTRAR", VERDE);
    JButton btnActualizar = crearBoton("ACTUALIZAR", VERDE_CLARO);
    JButton btnNuevo = crearBoton("LIMPIAR", Color.GRAY);

    btnRegistrar.addActionListener(e -> registrarProducto());
    btnActualizar.addActionListener(e -> actualizarProducto());
    btnNuevo.addActionListener(e -> limpiarFormulario());

    panel.add(btnRegistrar);
    panel.add(btnActualizar);
    panel.add(new JLabel(""));
    panel.add(btnNuevo);

    add(panel, BorderLayout.WEST);
}

    private void agregarCampo(
            JPanel panel, String etiqueta, java.awt.Component campo) {
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label);
        panel.add(campo);
    }

    private void crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID", "Nombre", "Tipo", "Marca", "Material",
                    "Forma", "Precio (L)", "Stock", "Estado", "Imagen"
                }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setRowHeight(25);
        tablaProductos.getTableHeader().setBackground(VERDE);
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12));

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tablaProductos.getSelectedRow() != -1) {
                cargarProductoSeleccionado();
            }
        });
tablaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(45);
tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(170);
tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(85);
tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(100);
tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(90);
tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(60);
tablaProductos.getColumnModel().getColumn(8).setPreferredWidth(80);
tablaProductos.getColumnModel().getColumn(9).setPreferredWidth(180);
        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Productos registrados"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    private void crearBotonesInferiores() {
        JPanel panel = new JPanel();
        panel.setBackground(FONDO);

        JButton btnEliminar = crearBoton("ELIMINAR PRODUCTO",
                new Color(170, 50, 50));
        JButton btnActualizarLista = crearBoton("ACTUALIZAR LISTA",
                VERDE);

        btnEliminar.addActionListener(e -> eliminarProducto());
        btnActualizarLista.addActionListener(e -> {
            cargarProductos();
            mostrarAlertasStock();
        });

        panel.add(btnEliminar);
        panel.add(btnActualizarLista);

        add(panel, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 11));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(TURQUESA);
                boton.setForeground(VERDE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(color);
                boton.setForeground(Color.WHITE);
            }
        });

        return boton;
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);

        List<Producto> productos = productoDAO.listarTodos();

        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getTipo(),
                p.getMarca(),
                p.getMaterial(),
                p.getForma(),
                String.format("%.2f", p.getPrecio()),
                p.getStock(),
                p.isActivo() ? "Activo" : "Inactivo",
                p.getImagen()
            });
        }
    }

    private Producto obtenerDatosFormulario() {
        String nombre = txtNombre.getText().trim();
        String marca = txtMarca.getText().trim();
        String material = txtMaterial.getText().trim();
        String forma = txtForma.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (nombre.isEmpty() || marca.isEmpty()
                || material.isEmpty() || forma.isEmpty()
                || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completa todos los campos, excepto la ruta de imagen.");
            return null;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio y las existencias no pueden ser negativos.");
                return null;
            }

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setTipo(cmbTipo.getSelectedItem().toString());
            producto.setMarca(marca);
            producto.setMaterial(material);
            producto.setForma(forma);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setActivo(chkActivo.isSelected());

            String imagen = txtImagen.getText().trim();
            producto.setImagen(imagen.isEmpty() ? null : imagen);

            return producto;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser numérico y el stock un número entero.");
            return null;
        }
    }

    private void registrarProducto() {
        Producto producto = obtenerDatosFormulario();

        if (producto == null) {
            return;
        }

        if (productoDAO.registrar(producto)) {
            JOptionPane.showMessageDialog(this,
                    "Producto registrado correctamente.");
            limpiarFormulario();
            cargarProductos();
            mostrarAlertasStock();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el producto. Revisa la consola.");
        }
    }

    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString());

        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        cmbTipo.setSelectedItem(
                modeloTabla.getValueAt(fila, 2).toString());
        txtMarca.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtMaterial.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtForma.setText(modeloTabla.getValueAt(fila, 5).toString());
        txtPrecio.setText(
                modeloTabla.getValueAt(fila, 6).toString());
        txtStock.setText(modeloTabla.getValueAt(fila, 7).toString());

        chkActivo.setSelected(
                modeloTabla.getValueAt(fila, 8).toString()
                        .equals("Activo"));

        Object imagen = modeloTabla.getValueAt(fila, 9);
        txtImagen.setText(imagen == null ? "" : imagen.toString());
    }

    private void actualizarProducto() {
        if (idSeleccionado == 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto de la tabla.");
            return;
        }

        Producto producto = obtenerDatosFormulario();

        if (producto == null) {
            return;
        }

        producto.setIdProducto(idSeleccionado);

        if (productoDAO.actualizar(producto)) {
            JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente.");
            limpiarFormulario();
            cargarProductos();
            mostrarAlertasStock();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el producto.");
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona el producto que deseas eliminar.");
            return;
        }

        int idProducto = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString());

        String nombre = modeloTabla.getValueAt(fila, 1).toString();

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar \"" + nombre + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(idProducto)) {
                JOptionPane.showMessageDialog(this,
                        "Producto eliminado correctamente.");
                limpiarFormulario();
                cargarProductos();
                mostrarAlertasStock();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. Puede que el producto "
                        + "esté relacionado con ventas o favoritos.");
            }
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = 0;

        txtNombre.setText("");
        cmbTipo.setSelectedIndex(0);
        txtMarca.setText("");
        txtMaterial.setText("");
        txtForma.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtImagen.setText("");
        chkActivo.setSelected(true);

        tablaProductos.clearSelection();
    }

    private void mostrarAlertasStock() {
        List<Producto> productos =
                productoDAO.listarArosConStockBajo();

        if (productos.isEmpty()) {
            return;
        }

        StringBuilder mensaje = new StringBuilder(
                "Los siguientes aros tienen menos de 5 unidades:\n\n");

        for (Producto p : productos) {
            mensaje.append("• ")
                    .append(p.getNombre())
                    .append(" — Stock: ")
                    .append(p.getStock())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this,
                mensaje.toString(),
                "Alerta de inventario",
                JOptionPane.WARNING_MESSAGE);
    }
}