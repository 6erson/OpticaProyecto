/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.modelo.Usuario;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
/**
 *
 * @author Alexander
 */
public class PanelCliente extends JFrame {

    private final Usuario usuario;

    // Colores de la identidad visual de Lumen Óptica
    private final Color VERDE = new Color(21, 94, 82);
    private final Color VERDE_CLARO = new Color(32, 112, 96);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color FONDO = new Color(244, 244, 244);
    private final Color BLANCO = Color.WHITE;
    private final Color TEXTO_SECUNDARIO = new Color(95, 108, 105);

    private JButton botonInicio;
    private JButton botonCatalogo;
    private JButton botonFavoritos;
    private JButton botonMediciones;
    // para futuras versiones private JButton botonCompras;

    private JButton botonSeleccionado;

    public PanelCliente(Usuario usuario) {
        this.usuario = usuario;

        configurarVentana();
        construirInterfaz();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Panel del cliente");
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

        JPanel contenido = crearPanelPrincipal();

        add(contenido, BorderLayout.CENTER);

        seleccionarOpcion(botonInicio);
    }

    // ENCABEZADO SUPERIOR

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(VERDE);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(16, 25, 16, 25));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(
                new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("LUMEN ÓPTICA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 27));
        titulo.setForeground(BLANCO);

        String nombre = usuario != null
                && usuario.getNombre() != null
                ? usuario.getNombre() : "Cliente";

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

        cerrarSesion.addActionListener(e -> cerrarSesion());

        encabezado.add(textos, BorderLayout.CENTER);
        encabezado.add(cerrarSesion, BorderLayout.EAST);

        return encabezado;
    }

    // MENÚ LATERAL

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

        botonInicio = crearBotonMenu(
                "Inicio", new IconoMenu(1));

        botonCatalogo = crearBotonMenu(
                "Productos", new IconoMenu(2));

        botonFavoritos = crearBotonMenu(
                "Mis favoritos", new IconoMenu(3));

        botonMediciones = crearBotonMenu(
                "Mis mediciones", new IconoMenu(4));

       // para futuras versiones botonCompras = crearBotonMenu("Mis compras", new IconoMenu(5));

        botonInicio.addActionListener(e ->
                seleccionarOpcion(botonInicio));

        botonCatalogo.addActionListener(e -> {
            seleccionarOpcion(botonCatalogo);
            abrirCatalogo();
        });

        botonFavoritos.addActionListener(e -> {
            seleccionarOpcion(botonFavoritos);
            abrirFavoritos();
        });

        botonMediciones.addActionListener(e -> {
            seleccionarOpcion(botonMediciones);
            abrirMediciones();
        });

       // para futuras versiones botonCompras.addActionListener(e -> {seleccionarOpcion(botonCompras);mostrarPendiente("Mis compras");});

        menu.add(tituloMenu);
        menu.add(botonInicio);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonCatalogo);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonFavoritos);
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMediciones);
        // para futuras versiones menu.add(Box.createVerticalStrut(8));
        // para futuras versiones menu.add(botonCompras);
        menu.add(Box.createVerticalGlue());

        return menu;
    }

    // ESTILO DE LOS BOTONES DEL MENÚ

    private JButton crearBotonMenu(String texto, Icon icono) {

        JButton boton = new JButton(texto, icono);

        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(BLANCO);
        boton.setBackground(VERDE);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(14);
        boton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48));
        boton.setPreferredSize(new Dimension(195, 48));

        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 4, 0, 0, VERDE),
                BorderFactory.createEmptyBorder(
                        10, 12, 10, 8)));

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                if (boton != botonSeleccionado) {
                    boton.setBackground(VERDE_CLARO);
                }
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                if (boton != botonSeleccionado) {
                    boton.setBackground(VERDE);
                }
            }
        });

        return boton;
    }

    // CONTROL DE LA OPCIÓN SELECCIONADA

    private void seleccionarOpcion(JButton seleccionado) {

        JButton[] botones = {
            botonInicio,
            botonCatalogo,
            botonFavoritos,
            botonMediciones,
            //para futuras versiones botonCompras
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
                BorderFactory.createEmptyBorder(
                        10, 12, 10, 8)));
    }

    // PANEL PRINCIPAL

    private JPanel crearPanelPrincipal() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FONDO);
        panel.setBorder(
                BorderFactory.createEmptyBorder(35, 35, 30, 35));

        String nombre = usuario != null
                && usuario.getNombre() != null
                ? usuario.getNombre() : "Cliente";

        JLabel titulo = new JLabel("Bienvenido, " + nombre);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "Gestiona tus productos, mediciones y compras de forma sencilla.");

        descripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 14));
        descripcion.setForeground(TEXTO_SECUNDARIO);
        descripcion.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(descripcion);
        panel.add(Box.createVerticalStrut(30));

        // Banner promocional de Lumen Óptica
JPanel contenedorBanner = new JPanel(
        new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER));

contenedorBanner.setOpaque(false);

try {
    BufferedImage imagenOriginal = ImageIO.read(
            new File("imagenes/banner_promocional.png"));

    // Ajustar imagen conservando sus proporciones
    int anchoMaximo = 820;
    int altoMaximo = 430;

    double escala = Math.min(
            (double) anchoMaximo / imagenOriginal.getWidth(),
            (double) altoMaximo / imagenOriginal.getHeight());

    int nuevoAncho = (int) (
            imagenOriginal.getWidth() * escala);

    int nuevoAlto = (int) (
            imagenOriginal.getHeight() * escala);

    Image imagenEscalada = imagenOriginal.getScaledInstance(
            nuevoAncho,
            nuevoAlto,
            Image.SCALE_SMOOTH);

    JLabel banner = new JLabel(
            new ImageIcon(imagenEscalada));

    contenedorBanner.add(banner);

} catch (Exception e) {

    JLabel error = new JLabel(
            "No se pudo cargar el banner promocional.");

    error.setForeground(VERDE);
    contenedorBanner.add(error);

}

panel.add(contenedorBanner);

        return panel;
    }

    // DISEÑO DE CADA TARJETA

    private JPanel crearTarjeta(
            String titulo,
            String descripcion,
            String textoBoton,
            Icon icono,
            Runnable accion) {

        JPanel tarjeta = new JPanel(new BorderLayout(15, 15));
        tarjeta.setBackground(BLANCO);

        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 232, 229), 1),
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20)));

        JPanel informacion = new JPanel(
                new BorderLayout(15, 5));

        informacion.setOpaque(false);

        JLabel imagen = new JLabel(icono);
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        imagen.setPreferredSize(new Dimension(55, 55));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(
                new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(
                new Font("Segoe UI", Font.BOLD, 18));
        encabezado.setForeground(VERDE);

        JLabel detalle = new JLabel(
                "<html><body style='width: 210px'>"
                + descripcion + "</body></html>");

        detalle.setFont(
                new Font("Segoe UI", Font.PLAIN, 13));
        detalle.setForeground(TEXTO_SECUNDARIO);

        textos.add(encabezado);
        textos.add(Box.createVerticalStrut(12));
        textos.add(detalle);

        informacion.add(imagen, BorderLayout.WEST);
        informacion.add(textos, BorderLayout.CENTER);

        JButton boton = crearBotonAccion(textoBoton);
        boton.addActionListener(e -> accion.run());

        tarjeta.add(informacion, BorderLayout.CENTER);
        tarjeta.add(boton, BorderLayout.SOUTH);

        return tarjeta;
    }

    // BOTONES TURQUESA

    private JButton crearBotonAccion(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(VERDE);
        boton.setBackground(TURQUESA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(250, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    // NAVEGACIÓN A LAS VENTANAS EXISTENTES

    private void abrirCatalogo() {

        VentanaCatalogo catalogo = new VentanaCatalogo(usuario);
        catalogo.setLocationRelativeTo(this);
        catalogo.setVisible(true);
    }

    private void abrirFavoritos() {

        VentanaFavoritos ventana = new VentanaFavoritos(usuario);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirMediciones() {

        VentanaMediciones ventana = new VentanaMediciones(usuario);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    // SECCIÓN PENDIENTE

    private void mostrarPendiente(String opcion) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "La sección «" + opcion
                + "» se implementará en el siguiente paso.",
                "Lumen Óptica",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    // CERRAR SESIÓN

    private void cerrarSesion() {

        dispose();

        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
    }

    // ICONOS PERSONALIZADOS DEL MENÚ Y LAS TARJETAS

    private class IconoMenu implements Icon {

        private final int tipo;

        public IconoMenu(int tipo) {
            this.tipo = tipo;
        }

        @Override
        public int getIconWidth() {
            return 30;
        }

        @Override
        public int getIconHeight() {
            return 30;
        }

        @Override
        public void paintIcon(
                java.awt.Component componente,
                Graphics grafico,
                int x,
                int y) {

            Graphics2D g = (Graphics2D) grafico.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(componente.getForeground());
            g.setStroke(new BasicStroke(1.8f));

            switch (tipo) {

                case 1:
                    // Inicio: casa
                    g.drawLine(x + 4, y + 14, x + 15, y + 4);
                    g.drawLine(x + 15, y + 4, x + 26, y + 14);
                    g.drawRect(x + 7, y + 13, 16, 13);
                    g.drawRect(x + 13, y + 18, 5, 8);
                    break;

                case 2:
                    // Catálogo: lentes
                    g.drawOval(x + 2, y + 8, 11, 11);
                    g.drawOval(x + 17, y + 8, 11, 11);
                    g.drawLine(x + 13, y + 13, x + 17, y + 13);
                    g.drawLine(x + 2, y + 12, x, y + 10);
                    g.drawLine(x + 28, y + 12, x + 30, y + 10);
                    break;

                case 3:
                    // Favoritos: corazón
                    int[] px = {
                        x + 15, x + 4, x + 2, x + 3,
                        x + 15, x + 27, x + 28, x + 26
                    };
                    int[] py = {
                        y + 26, y + 15, y + 10, y + 7,
                        y + 15, y + 7, y + 10, y + 15
                    };
                    g.drawPolygon(px, py, px.length);
                    break;

                case 4:
                    // Mediciones: ojo
                    g.drawOval(x + 2, y + 8, 26, 14);
                    g.drawOval(x + 11, y + 11, 8, 8);
                    break;

                case 5:
                    // Compras: carrito
                    g.drawLine(x + 3, y + 5, x + 7, y + 5);
                    g.drawLine(x + 7, y + 5, x + 11, y + 21);
                    g.drawLine(x + 11, y + 21, x + 25, y + 21);
                    g.drawLine(x + 9, y + 11, x + 27, y + 11);
                    g.drawLine(x + 27, y + 11, x + 24, y + 19);
                    g.fillOval(x + 11, y + 24, 4, 4);
                    g.fillOval(x + 22, y + 24, 4, 4);
                    break;
            }

            g.dispose();
        }
    }
}