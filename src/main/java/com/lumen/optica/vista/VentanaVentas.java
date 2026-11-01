/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.ProductoDAO;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.impldao.ProductoDAOImpl;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import com.lumen.optica.modelo.DetalleVenta;
import com.lumen.optica.modelo.Producto;
import com.lumen.optica.modelo.Usuario;
import com.lumen.optica.modelo.Venta;
import com.lumen.optica.servicio.VentaServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import com.lumen.optica.dao.MedicionDAO;
import com.lumen.optica.impldao.MedicionDAOImpl;
import com.lumen.optica.modelo.Medicion;
import java.time.format.DateTimeFormatter;
import com.lumen.optica.dao.AroGuardadoDAO;
import com.lumen.optica.impldao.AroGuardadoDAOImpl;

/**
 *
 * @author Alexander
 */
public class VentanaVentas extends JFrame {

    private final Color VERDE = new Color(21, 94, 82);
    private final Color VERDE_CLARO = new Color(32, 112, 96);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);

    private final Usuario vendedor;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final VentaServicio ventaServicio = new VentaServicio();
    private final MedicionDAO medicionDAO = new MedicionDAOImpl();
    private final AroGuardadoDAO aroGuardadoDAO = new AroGuardadoDAOImpl();

    private JComboBox<Medicion> cmbMediciones;

    private JComboBox<Usuario> cmbClientes;
    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;

    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal;
    private JLabel lblTotal;

    public VentanaVentas(Usuario vendedor) {
        this.vendedor = vendedor;

        setTitle("Lumen Óptica - Ventas");
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(FONDO);

        crearEncabezado();
        crearPanelSuperior();
        crearTabla();
        crearPanelInferior();

        cargarClientes();
        cargarProductos();
    }

    private void crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERDE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("REGISTRO DE VENTAS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("LUMEN ÓPTICA");
        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
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

 private void crearPanelSuperior() {
    JPanel panel = new JPanel(new GridLayout(0, 2, 12, 10));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Datos de la venta"),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));

    cmbClientes = new JComboBox<>();
    cmbProductos = new JComboBox<>();
    cmbMediciones = new JComboBox<>();

    cmbClientes.setRenderer(new javax.swing.DefaultListCellRenderer() {
        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> lista, Object valor,
                int indice, boolean seleccionado, boolean tieneFoco) {

            super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            if (valor instanceof Usuario) {
                Usuario u = (Usuario) valor;
                setText(u.getNombre() + " - DNI: " + u.getDni());
            }

            return this;
        }
    });

    cmbProductos.setRenderer(new javax.swing.DefaultListCellRenderer() {
        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> lista, Object valor,
                int indice, boolean seleccionado, boolean tieneFoco) {

            super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            if (valor instanceof Producto) {
                Producto p = (Producto) valor;
                setText(p.getNombre() + " - L "
                        + String.format("%.2f", p.getPrecio())
                        + " (Stock: " + p.getStock() + ")");
            }

            return this;
        }
    });

    cmbMediciones.setRenderer(new javax.swing.DefaultListCellRenderer() {
        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> lista, Object valor,
                int indice, boolean seleccionado, boolean tieneFoco) {

            super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            if (valor instanceof Medicion) {
                Medicion m = (Medicion) valor;

                String texto = "Fecha: " + m.getFechaMedicion()
                        + " | Esfera: " + m.getEsfera()
                        + " | Cilindro: " + m.getCilindro()
                        + " | Eje: " + m.getEje();

                if (m.getAdicion() != null) {
                    texto += " | Adición: " + m.getAdicion();
                }

                setText(texto);
            }

            return this;
        }
    });

    // Cambiar de cliente actualiza las mediciones disponibles.
    cmbClientes.addActionListener(e -> cargarMediciones());

    agregarCampo(panel, "Cliente:", cmbClientes);
    agregarCampo(panel, "Medición:", cmbMediciones);
    agregarCampo(panel, "Producto:", cmbProductos);

    spnCantidad = new JSpinner(
            new SpinnerNumberModel(1, 1, 100, 1));

JButton btnAgregar = crearBoton("AGREGAR PRODUCTO", VERDE);
btnAgregar.addActionListener(e -> agregarProducto());

JButton btnFavoritos = crearBoton("VER FAVORITOS", VERDE_CLARO);
btnFavoritos.addActionListener(e -> mostrarFavoritos());

agregarCampo(panel, "Cantidad:", spnCantidad);

JPanel accionesProductos = new JPanel(new GridLayout(1, 2, 8, 0));
accionesProductos.setBackground(Color.WHITE);
accionesProductos.add(btnAgregar);
accionesProductos.add(btnFavoritos);

agregarCampo(panel, "Acción:", accionesProductos);

    add(panel, BorderLayout.BEFORE_FIRST_LINE);
}

    private void agregarCampo(
            JPanel panel, String texto,
            java.awt.Component componente) {

        JPanel grupo = new JPanel(new BorderLayout(8, 0));
        grupo.setBackground(Color.WHITE);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));

        grupo.add(etiqueta, BorderLayout.WEST);
        grupo.add(componente, BorderLayout.CENTER);

        panel.add(grupo);
    }
    
    private void cargarMediciones() {
    if (cmbMediciones == null) {
        return;
    }

    cmbMediciones.removeAllItems();

    Usuario cliente = (Usuario) cmbClientes.getSelectedItem();

    if (cliente == null) {
        return;
    }

    List<Medicion> mediciones =
            medicionDAO.listarPorUsuario(cliente.getIdUsuario());

    for (Medicion medicion : mediciones) {
        cmbMediciones.addItem(medicion);
    }

    cmbMediciones.setSelectedIndex(-1);

    if (mediciones.isEmpty()) {
        System.out.println(
                "El cliente seleccionado no tiene mediciones registradas.");
    }
}

    private void crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID", "Producto", "Precio (L)",
                    "Cantidad", "Subtotal (L)"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setRowHeight(27);
        tablaDetalle.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);

        tablaDetalle.getTableHeader().setBackground(VERDE);
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaDetalle);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Productos de la venta"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 12, 10));

        JButton btnQuitar = crearBoton("QUITAR PRODUCTO",
                new Color(170, 50, 50));

        btnQuitar.addActionListener(e -> quitarProducto());

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.add(btnQuitar);

        lblSubtotal = new JLabel("Subtotal: L 0.00");
        lblTotal = new JLabel("TOTAL: L 0.00");

        lblSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(VERDE);

        JPanel importes = new JPanel(new GridLayout(2, 1, 0, 5));
        importes.setOpaque(false);
        importes.add(lblSubtotal);
        importes.add(lblTotal);

        JButton btnRegistrar = crearBoton("REGISTRAR VENTA", VERDE);
        btnRegistrar.addActionListener(e -> registrarVenta());

        JPanel derecha = new JPanel(new BorderLayout(12, 0));
        derecha.setOpaque(false);
        derecha.add(importes, BorderLayout.CENTER);
        derecha.add(btnRegistrar, BorderLayout.EAST);

        panel.add(izquierda, BorderLayout.WEST);
        panel.add(derecha, BorderLayout.EAST);

        add(panel, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

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

    private void cargarClientes() {
        DefaultComboBoxModel<Usuario> modelo =
                new DefaultComboBoxModel<>();

        List<Usuario> clientes = usuarioDAO.listarClientes();

        for (Usuario cliente : clientes) {
            modelo.addElement(cliente);
        }

        cmbClientes.setModel(modelo);
        
        cargarMediciones();

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay clientes registrados para realizar ventas.");
        }
    }

    private void cargarProductos() {
        DefaultComboBoxModel<Producto> modelo =
                new DefaultComboBoxModel<>();

        List<Producto> productos = productoDAO.listarActivos();

        for (Producto producto : productos) {
            if (producto.getStock() > 0) {
                modelo.addElement(producto);
            }
        }

        cmbProductos.setModel(modelo);
    }

    private void agregarProducto() {
        Producto producto =
                (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos disponibles con existencias.");
            return;
        }

        int cantidad = (Integer) spnCantidad.getValue();
        int filaExistente = buscarFila(producto.getIdProducto());

        int cantidadActual = 0;

        if (filaExistente >= 0) {
            cantidadActual = Integer.parseInt(
                    modeloTabla.getValueAt(filaExistente, 3).toString());
        }

        if (cantidadActual + cantidad > producto.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficientes existencias de "
                    + producto.getNombre() + ".");
            return;
        }

        if (filaExistente >= 0) {
            int nuevaCantidad = cantidadActual + cantidad;
            double subtotal = nuevaCantidad * producto.getPrecio();

            modeloTabla.setValueAt(nuevaCantidad, filaExistente, 3);
            modeloTabla.setValueAt(
                    String.format("%.2f", subtotal),
                    filaExistente, 4);
        } else {
            double subtotal = cantidad * producto.getPrecio();

            modeloTabla.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                String.format("%.2f", producto.getPrecio()),
                cantidad,
                String.format("%.2f", subtotal)
            });
        }

        calcularTotal();
    }

    private int buscarFila(int idProducto) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int id = Integer.parseInt(
                    modeloTabla.getValueAt(i, 0).toString());

            if (id == idProducto) {
                return i;
            }
        }

        return -1;
    }

    private void quitarProducto() {
        int fila = tablaDetalle.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto de la tabla.");
            return;
        }

        modeloTabla.removeRow(fila);
        calcularTotal();
    }

    private double obtenerSubtotal() {
        double subtotal = 0;

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            subtotal += Double.parseDouble(
                    modeloTabla.getValueAt(i, 4).toString());
        }

        return subtotal;
    }

    private void calcularTotal() {
        double subtotal = obtenerSubtotal();

        // En esta versión no se aplica impuesto.
        lblSubtotal.setText(String.format(
                "Subtotal: L %.2f", subtotal));

        lblTotal.setText(String.format(
                "TOTAL: L %.2f", subtotal));
    }
    
    private void mostrarFavoritos() {
    Usuario cliente = (Usuario) cmbClientes.getSelectedItem();

    if (cliente == null) {
        JOptionPane.showMessageDialog(this,
                "Selecciona primero un cliente.");
        return;
    }

    List<Producto> favoritos =
            aroGuardadoDAO.listarPorUsuario(cliente.getIdUsuario());

    List<Producto> disponibles = new ArrayList<>();

   for (Producto producto : favoritos) {
    if (producto.isActivo()
            && producto.getStock() > 0) {
        disponibles.add(producto);
    }
}

    if (disponibles.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Este cliente no tiene Productos favoritos disponibles "
                + "con existencias.");
        return;
    }

    JComboBox<Producto> cmbFavoritos = new JComboBox<>();

    for (Producto producto : disponibles) {
        cmbFavoritos.addItem(producto);
    }

    cmbFavoritos.setRenderer(
            new javax.swing.DefaultListCellRenderer() {
        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> lista, Object valor,
                int indice, boolean seleccionado, boolean tieneFoco) {

            super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            if (valor instanceof Producto) {
                Producto p = (Producto) valor;
                setText(p.getNombre() + " - L "
                        + String.format("%.2f", p.getPrecio())
                        + " (Stock: " + p.getStock() + ")");
            }

            return this;
        }
    });

    int respuesta = JOptionPane.showConfirmDialog(
            this,
            cmbFavoritos,
            "Productos favoritos de " + cliente.getNombre(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

    if (respuesta != JOptionPane.OK_OPTION) {
        return;
    }

    Producto favorito = (Producto) cmbFavoritos.getSelectedItem();

    if (favorito == null) {
        return;
    }

    // Seleccionar el Producto  favorito en el catálogo de productos.
    for (int i = 0; i < cmbProductos.getItemCount(); i++) {
        Producto producto = cmbProductos.getItemAt(i);

        if (producto.getIdProducto() == favorito.getIdProducto()) {
            cmbProductos.setSelectedIndex(i);
            agregarProducto();
            return;
        }
    }

    JOptionPane.showMessageDialog(this,
            "El producto favorito ya no está disponible en el catálogo.");
}

    private void registrarVenta() {
        Usuario cliente =
                (Usuario) cmbClientes.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente.");
            return;
        }
        
Medicion medicionSeleccionada =
        (Medicion) cmbMediciones.getSelectedItem();

// Determinar si algún producto requiere medición.
boolean requiereMedicion = false;

for (int i = 0; i < modeloTabla.getRowCount(); i++) {

    int idProducto = Integer.parseInt(
            modeloTabla.getValueAt(i, 0).toString());

    for (int j = 0; j < cmbProductos.getItemCount(); j++) {

        Producto producto = cmbProductos.getItemAt(j);

        if (producto.getIdProducto() == idProducto) {

            String tipo = producto.getTipo();

            if ("ARO".equalsIgnoreCase(tipo)
                    || "LUNA".equalsIgnoreCase(tipo)) {

                requiereMedicion = true;
            }

            break;
        }
    }
}

// Exigir medición solamente cuando corresponda.
if (requiereMedicion && medicionSeleccionada == null) {

    JOptionPane.showMessageDialog(this,
            "Selecciona una medición del cliente para registrar la venta.");

    return;
}

// Verificar la pertenencia solamente si existe una medición.
if (medicionSeleccionada != null
        && medicionSeleccionada.getIdUsuario()
        != cliente.getIdUsuario()) {

    JOptionPane.showMessageDialog(this,
            "La medición seleccionada no pertenece al cliente.");

    return;
}



        if (vendedor == null
                || !"VENDEDOR".equalsIgnoreCase(vendedor.getRol())) {
            JOptionPane.showMessageDialog(this,
                    "No se ha identificado una cuenta de vendedor válida.");
            return;
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Agrega al menos un producto a la venta.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Deseas registrar esta venta por "
                + lblTotal.getText() + "?",
                "Confirmar venta",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Venta venta = new Venta();
        venta.setIdCliente(cliente.getIdUsuario());
        venta.setIdVendedor(vendedor.getIdUsuario());
        if (medicionSeleccionada != null) {
    venta.setIdMedicion(medicionSeleccionada.getIdMedicion());
} else {
    venta.setIdMedicion(null);
}
        venta.setTasaImpuesto(0.0);

        List<DetalleVenta> detalles = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            DetalleVenta detalle = new DetalleVenta();

            detalle.setIdProducto(Integer.parseInt(
                    modeloTabla.getValueAt(i, 0).toString()));

            detalle.setCantidad(Integer.parseInt(
                    modeloTabla.getValueAt(i, 3).toString()));

            detalles.add(detalle);
        }

        boolean registrado =
                ventaServicio.registrarVenta(venta, detalles);

        if (registrado) {
            JOptionPane.showMessageDialog(this,
                    "Venta registrada correctamente.\n"
                    + "Número de venta: " + venta.getIdVenta()
                    + "\nTotal: L "
                    + String.format("%.2f", venta.getTotal()));

            modeloTabla.setRowCount(0);
            calcularTotal();
            cargarProductos();

        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la venta.\n"
                    + "Revisa las existencias y la consola de NetBeans.",
                    "Error al registrar",
                    JOptionPane.ERROR_MESSAGE);

            cargarProductos();
        }
    }
}
