/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import com.lumen.optica.dao.VentaDAO;
import com.lumen.optica.impldao.VentaDAOImpl;
import com.lumen.optica.dao.DetalleVentaDAO;
import com.lumen.optica.impldao.DetalleVentaDAOImpl;
import com.lumen.optica.modelo.DetalleVenta;
import com.lumen.optica.modelo.Usuario;
import com.lumen.optica.modelo.Venta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import com.lumen.optica.dao.ProductoDAO;
import com.lumen.optica.impldao.ProductoDAOImpl;
import com.lumen.optica.modelo.Producto;

/**
 *
 * @author Alexander
 */
public class VentanaHistorialVentas extends JFrame {

    private final Color VERDE = new Color(21, 94, 82);
    private final Color VERDE_CLARO = new Color(32, 112, 96);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);

    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;

    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;

    private JLabel lblTotalVentas;

    public VentanaHistorialVentas() {
        setTitle("Lumen Óptica - Historial de ventas");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(FONDO);

        crearEncabezado();
        crearTablaVentas();
        crearTablaDetalles();
        crearPanelInferior();

        cargarVentas();
    }

    private void crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERDE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("HISTORIAL DE VENTAS");
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

    private void crearTablaVentas() {
        modeloVentas = new DefaultTableModel(
                new Object[]{
                    "N.º Venta", "Fecha", "Cliente", "Vendedor",
                    "Subtotal (L)", "Impuesto (L)", "Total (L)"
                }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaVentas = new JTable(modeloVentas);
        tablaVentas.setRowHeight(25);
        tablaVentas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        configurarTabla(tablaVentas);

        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tablaVentas.getSelectedRow() >= 0) {
                cargarDetallesSeleccionados();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaVentas);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Ventas registradas"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    private void crearTablaDetalles() {
        modeloDetalles = new DefaultTableModel(
                new Object[]{
                    "ID Producto", "Producto", "Cantidad",
                    "Precio unitario (L)", "Subtotal (L)"
                }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(24);
        configurarTabla(tablaDetalles);

        JScrollPane scroll = new JScrollPane(tablaDetalles);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Productos de la venta seleccionada"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new java.awt.Dimension(0, 220));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.SOUTH);
    }

    private void crearPanelInferior() {
        // El resumen y el botón de actualizar se colocan
        // dentro de una ventana auxiliar en la parte superior
        // de la tabla de detalles.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 8, 10));

        lblTotalVentas = new JLabel("Ventas registradas: 0");
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalVentas.setForeground(VERDE);

        JButton btnActualizar = crearBoton("ACTUALIZAR", VERDE);
        btnActualizar.addActionListener(e -> cargarVentas());

        panel.add(lblTotalVentas, BorderLayout.WEST);
        panel.add(btnActualizar, BorderLayout.EAST);

        // Se incorpora al encabezado del contenido mediante
        // el borde superior de la tabla de ventas.
        getContentPane().add(panel, BorderLayout.BEFORE_FIRST_LINE);
    }

    private void configurarTabla(JTable tabla) {
        tabla.getTableHeader().setBackground(VERDE);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
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

    private void cargarVentas() {
        modeloVentas.setRowCount(0);
        modeloDetalles.setRowCount(0);

        List<Venta> ventas = ventaDAO.listarTodas();

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Venta venta : ventas) {
            Usuario cliente = usuarioDAO.buscarPorId(
                    venta.getIdCliente());

            Usuario vendedor = usuarioDAO.buscarPorId(
                    venta.getIdVendedor());

            String nombreCliente = cliente != null
                    ? cliente.getNombre() : "No disponible";

            String nombreVendedor = vendedor != null
                    ? vendedor.getNombre() : "No disponible";

            String fecha = venta.getFechaVenta() != null
                    ? venta.getFechaVenta().format(formato) : "";

            modeloVentas.addRow(new Object[]{
                venta.getIdVenta(),
                fecha,
                nombreCliente,
                nombreVendedor,
                String.format("%.2f", venta.getSubtotal()),
                String.format("%.2f", venta.getImpuesto()),
                String.format("%.2f", venta.getTotal())
            });
        }

        lblTotalVentas.setText(
                "Ventas registradas: " + ventas.size());
    }

  private void cargarDetallesSeleccionados() {
    int fila = tablaVentas.getSelectedRow();

    if (fila < 0) {
        return;
    }

    int filaModelo = tablaVentas.convertRowIndexToModel(fila);

    int idVenta = Integer.parseInt(
            modeloVentas.getValueAt(filaModelo, 0).toString()
    );

    modeloDetalles.setRowCount(0);

    List<DetalleVenta> detalles =
            detalleDAO.listarPorVenta(idVenta);

    for (DetalleVenta detalle : detalles) {

        Producto producto =
                productoDAO.buscarPorId(detalle.getIdProducto());

        String nombreProducto = producto != null
                ? producto.getNombre()
                : "Producto #" + detalle.getIdProducto();

        modeloDetalles.addRow(new Object[]{
            detalle.getIdProducto(),
            nombreProducto,
            detalle.getCantidad(),
            String.format("%.2f", detalle.getPrecioUnitario()),
            String.format("%.2f", detalle.getSubtotal())
        });
    }

    if (detalles.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Esta venta no tiene productos registrados.",
                "Detalles de la venta",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
}