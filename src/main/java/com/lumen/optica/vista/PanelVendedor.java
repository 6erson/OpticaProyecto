/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.modelo.Usuario;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.lumen.optica.vista.VentanaHistorialVentas;
import com.lumen.optica.dao.UsuarioDAO;
import com.lumen.optica.impldao.UsuarioDAOImpl;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Alexander
 */
public class PanelVendedor extends JFrame {

    private final Usuario usuario;

    private final Color VERDE = new Color(21, 94, 82);
    private final Color VERDE_CLARO = new Color(32, 112, 96);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);
    private final Color BLANCO = Color.WHITE;
    private final Color TEXTO_SECUNDARIO = new Color(95, 108, 105);

    private final CardLayout tarjetas = new CardLayout();
    private JPanel contenido;

    private JButton botonMediciones;
    private JButton botonInventario;
    private JButton botonVentas;
    private JButton botonClientes;
    private JButton botonVendedores;

    public PanelVendedor(Usuario usuario) {
        this.usuario = usuario;

        configurarVentana();
        construirInterfaz();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Panel del vendedor");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);
    }

    private void construirInterfaz() {
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearMenuLateral(), BorderLayout.WEST);

        contenido = new JPanel(tarjetas);
        contenido.setBackground(FONDO);

        contenido.add(crearPanelMediciones(), "MEDICIONES");
        contenido.add(crearPanelInventario(), "INVENTARIO");
        contenido.add(crearPanelVentas(), "VENTAS");
        contenido.add(crearPanelClientes(), "CLIENTES");

        add(contenido, BorderLayout.CENTER);

        seleccionarOpcion(botonMediciones, "MEDICIONES");
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(VERDE);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(16, 25, 16, 25));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        
        JLabel titulo = new JLabel("LUMEN ÓPTICA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 27));
        titulo.setForeground(BLANCO);

        String nombre = usuario != null
                ? usuario.getNombre() : "Vendedor";

        JLabel bienvenida = new JLabel("Bienvenido, " + nombre);
        bienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bienvenida.setForeground(TURQUESA);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(7));
        textos.add(bienvenida);

        JButton cerrarSesion = new JButton("Cerrar sesión");
        cerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cerrarSesion.setForeground(VERDE);
        cerrarSesion.setBackground(TURQUESA);
        cerrarSesion.setFocusPainted(false);
        cerrarSesion.setBorderPainted(false);
        cerrarSesion.setPreferredSize(new Dimension(145, 45));
        cerrarSesion.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        cerrarSesion.addActionListener(e -> {
            dispose();
            new VentanaLogin().setVisible(true);
        });

        encabezado.add(textos, BorderLayout.CENTER);
        encabezado.add(cerrarSesion, BorderLayout.EAST);

        return encabezado;
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(VERDE);
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBorder(
                BorderFactory.createEmptyBorder(25, 12, 20, 12));

        JLabel tituloMenu = new JLabel("MENÚ PRINCIPAL");
        tituloMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloMenu.setForeground(TURQUESA);
        tituloMenu.setBorder(
                BorderFactory.createEmptyBorder(0, 12, 22, 0));
 tituloMenu.setAlignmentX(LEFT_ALIGNMENT);

        botonMediciones = crearBotonMenu(
                "Mediciones", new IconoMenu(1));
        botonInventario = crearBotonMenu(
                "Inventario", new IconoMenu(2));
        botonVentas = crearBotonMenu(
                "Ventas", new IconoMenu(3));
        botonClientes = crearBotonMenu(
                "Clientes", new IconoMenu(4));
        botonVendedores = crearBotonMenu(
                "Vendedores", new IconoMenu(5));

        botonMediciones.addActionListener(e ->
                seleccionarOpcion(botonMediciones, "MEDICIONES"));

        botonInventario.addActionListener(e -> abrirInventario());

        botonVentas.addActionListener(e ->
                seleccionarOpcion(botonVentas, "VENTAS"));

        botonClientes.addActionListener(e ->
                seleccionarOpcion(botonClientes, "CLIENTES"));

        botonVendedores.addActionListener(e -> abrirVendedores());

        menu.add(tituloMenu);
        menu.add(botonMediciones);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonInventario);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonVentas);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonClientes);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonVendedores);
        menu.add(Box.createVerticalGlue());

        return menu;
    }
    
    private void abrirInventario() {
        VentanaInventario ventana = new VentanaInventario();
        ventana.setVisible(true);
    }

    private void abrirVendedores() {
        VentanaVendedores ventana = new VentanaVendedores();
        ventana.setVisible(true);
    }

private JButton crearBotonMenu(String texto, Icon icono) {
        JButton boton = new JButton(texto, icono);

        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(BLANCO);
        boton.setBackground(VERDE);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(14);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        boton.setPreferredSize(new Dimension(195, 48));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 4, 0, 0, VERDE),
                BorderFactory.createEmptyBorder(10, 12, 10, 8)));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.getBackground().equals(VERDE)) {
                    boton.setBackground(VERDE_CLARO);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (boton != botonSeleccionado) {
                    boton.setBackground(VERDE);
                }
            }
        });

        return boton;
    }

    private JButton botonSeleccionado;

    private void seleccionarOpcion(JButton seleccionado, String tarjeta) {
        JButton[] botones = {
            botonMediciones, botonInventario, botonVentas, botonClientes
        };

        for (JButton boton : botones) {
            if (boton != null) {
                boton.setBackground(VERDE);
                 boton.setForeground(BLANCO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 4, 0, 0, VERDE),
                        BorderFactory.createEmptyBorder(
                                10, 12, 10, 8)));
            }
        }

        botonSeleccionado = seleccionado;
        seleccionado.setBackground(VERDE_CLARO);
        seleccionado.setForeground(TURQUESA);
        seleccionado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 4, 0, 0, TURQUESA),
                BorderFactory.createEmptyBorder(10, 12, 10, 8)));

        tarjetas.show(contenido, tarjeta);
    }

    private JPanel crearPanelBase(String titulo, String descripcion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FONDO);
        panel.setBorder(
                BorderFactory.createEmptyBorder(35, 35, 30, 35));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 25));
        encabezado.setForeground(VERDE);
        encabezado.setAlignmentX(CENTER_ALIGNMENT);

        JLabel detalle = new JLabel(descripcion);
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detalle.setForeground(TEXTO_SECUNDARIO);
        detalle.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(encabezado);
        panel.add(Box.createVerticalStrut(12));
        panel.add(detalle);
        panel.add(Box.createVerticalStrut(28));

        return panel;
    }

    private JPanel crearPanelMediciones() {
        JPanel panel = crearPanelBase(
                "Gestión de mediciones",
                "Registra y administra las mediciones visuales de los clientes.");
        
        panel.add(crearTarjetaInformativa(
                "Registro de mediciones",
                "Registra la graduación visual de cada cliente.",
                "Los datos se guardarán en el historial de mediciones."));

        panel.add(Box.createVerticalStrut(20));

        JButton registrar = crearBotonAccion("REGISTRAR MEDICIÓN");
        registrar.addActionListener(e ->
                new VentanaRegistrarMedicion().setVisible(true));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acciones.setOpaque(false);
        acciones.add(registrar);
        acciones.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(acciones);

        return panel;
    }

    private JPanel crearPanelInventario() {
        JPanel panel = crearPanelBase(
                "Gestión de inventario",
                "Administra los productos y las existencias de la óptica.");

        panel.add(crearTarjetaInformativa(
                "Inventario de productos",
                "Consulta y administra aros, lunas y accesorios.",
                "Conectaremos estas operaciones con la tabla PRODUCTO."));

        return panel;
    }

private JPanel crearPanelVentas() {
    JPanel panel = crearPanelBase(
            "Gestión de ventas",
            "Registra las compras realizadas por los clientes.");

    panel.add(crearTarjetaInformativa(
            "Registro de ventas",
            "Selecciona al cliente y los productos de su compra.",
            "El total se calcula automáticamente y el inventario "
            + "se actualiza al confirmar la venta."));

    panel.add(Box.createVerticalStrut(20));

    // Botón para registrar una venta
    JButton registrar = crearBotonAccion("REGISTRAR VENTA");
 registrar.addActionListener(e -> {
        VentanaVentas ventana = new VentanaVentas(usuario);
        ventana.setVisible(true);
    });

    // Botón para consultar el historial
    JButton btnHistorial =
            crearBotonAccion("HISTORIAL DE VENTAS");

    btnHistorial.addActionListener(e -> {
        VentanaHistorialVentas ventana =
                new VentanaHistorialVentas();
        ventana.setVisible(true);
    });

    // Panel que contiene ambos botones
    JPanel acciones =
            new JPanel(new FlowLayout(FlowLayout.CENTER));

    acciones.setOpaque(false);
    acciones.add(registrar);
    acciones.add(btnHistorial);
    acciones.setAlignmentX(CENTER_ALIGNMENT);

    panel.add(acciones);

    return panel;
}

 private JPanel crearPanelClientes() {
    JPanel panel = crearPanelBase(
            "Gestión de clientes",
            "Consulta los clientes registrados en Lumen Óptica.");

    String[] columnas = {
        "ID", "Nombre", "DNI", "Teléfono", "Correo"
    };

    DefaultTableModel modelo = new DefaultTableModel(
            columnas, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tabla = new JTable(modelo);
    tabla.setRowHeight(28);
    tabla.getTableHeader().setReorderingAllowed(false);
 JScrollPane scroll = new JScrollPane(tabla);
    scroll.setPreferredSize(new Dimension(780, 300));
    scroll.setMaximumSize(new Dimension(
            Integer.MAX_VALUE, 350));
    scroll.setAlignmentX(LEFT_ALIGNMENT);

    JButton actualizar = crearBotonAccion("ACTUALIZAR CLIENTES");

    actualizar.addActionListener(e -> {
        cargarClientesEnTabla(modelo);
    });

    JPanel acciones = new JPanel(
            new FlowLayout(FlowLayout.CENTER));
    acciones.setOpaque(false);
    acciones.add(actualizar);
    acciones.setAlignmentX(CENTER_ALIGNMENT);

    panel.add(scroll);
    panel.add(Box.createVerticalStrut(15));
    panel.add(acciones);

    cargarClientesEnTabla(modelo);

    return panel;
}

private void cargarClientesEnTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);

    UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    List<Usuario> clientes = usuarioDAO.listarClientes();

    for (Usuario cliente : clientes) {
        modelo.addRow(new Object[]{
            cliente.getIdUsuario(),
            cliente.getNombre(),
            cliente.getDni(),
            cliente.getTelefono(),
            cliente.getCorreo()
        });
    }
}

    private JPanel crearTarjetaInformativa(
            String titulo, String linea1, String linea2) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(BLANCO);
          tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 232, 229), 1),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        encabezado.setForeground(VERDE);

        JLabel descripcion1 = new JLabel(linea1);
        descripcion1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descripcion1.setForeground(TEXTO_SECUNDARIO);

        JLabel descripcion2 = new JLabel(linea2);
        descripcion2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descripcion2.setForeground(TEXTO_SECUNDARIO);

        tarjeta.add(encabezado);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(descripcion1);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(descripcion2);

        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        tarjeta.setAlignmentX(LEFT_ALIGNMENT);

        return tarjeta;
    }

    private JButton crearBotonAccion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(VERDE);
        boton.setBackground(TURQUESA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(250, 48));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    private class IconoMenu implements Icon {

        private final int tipo;

        public IconoMenu(int tipo) {
            this.tipo = tipo;
        }

        @Override
         public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 20;
        }

        @Override
        public void paintIcon(
                java.awt.Component componente,
                Graphics grafico, int x, int y) {

            Graphics2D g = (Graphics2D) grafico.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = componente.isEnabled()
                    ? componente.getForeground()
                    : Color.GRAY;

            g.setColor(color);
            g.setStroke(new BasicStroke(1.8f));

            switch (tipo) {
                case 1:
                    // Mediciones: ojo estilizado
                    g.drawOval(x + 2, y + 5, 16, 10);
                    g.fillOval(x + 8, y + 8, 5, 5);
                    break;

                case 2:
                    // Inventario: caja
                    g.drawRoundRect(x + 3, y + 3, 14, 14, 2, 2);
                    g.drawLine(x + 3, y + 8, x + 17, y + 8);
                    g.drawLine(x + 10, y + 3, x + 10, y + 8);
                    break;

                case 3:
                    // Ventas: gráfico de barras
                    g.drawLine(x + 3, y + 17, x + 18, y + 17);
                    g.drawRect(x + 4, y + 10, 3, 7);
                    g.drawRect(x + 9, y + 6, 3, 11);
                    g.drawRect(x + 14, y + 3, 3, 14);
                    break;
 case 4:
                    // Clientes: cabeza y hombros
                    g.drawOval(x + 7, y + 2, 6, 6);
                    g.drawArc(x + 3, y + 8, 14, 11, 0, 180);
                    break;

                case 5:
                    // Vendedores: cabeza y hombros con distintivo
                    g.drawOval(x + 7, y + 2, 6, 6);
                    g.drawArc(x + 3, y + 8, 14, 11, 0, 180);
                    g.drawLine(x + 2, y + 5, x + 5, y + 2);
                    g.drawLine(x + 15, y + 2, x + 18, y + 5);
                    break;

                default:
                    g.drawOval(x + 4, y + 4, 12, 12);
            }

            g.dispose();
        }
    }
}