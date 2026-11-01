/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.vista;
import com.lumen.optica.dao.AroGuardadoDAO;
import com.lumen.optica.impldao.AroGuardadoDAOImpl;
import com.lumen.optica.modelo.Producto;
import com.lumen.optica.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
/**
 *
 * @author Alexander
 */
public class VentanaFavoritos extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private final Usuario usuario;
    private final AroGuardadoDAO favoritoDAO;
    private final JPanel panelProductos;

    public VentanaFavoritos(Usuario usuario) {
        this.usuario = usuario;
        favoritoDAO = new AroGuardadoDAOImpl();
        panelProductos = new JPanel(new GridLayout(0, 3, 15, 15));

        configurarVentana();
        construirInterfaz();
        cargarFavoritos();
    }

    private void configurarVentana() {
        setTitle("Lumen Óptica | Mis favoritos");
        setSize(1000, 700);
        setMinimumSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout(15, 15));
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("MIS FAVORITOS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(AZUL);

        JButton botonActualizar = crearBoton("Actualizar");
        botonActualizar.addActionListener(e -> cargarFavoritos());

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(BLANCO);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonActualizar, BorderLayout.EAST);

        panelProductos.setBackground(BLANCO);
        panelProductos.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane desplazamiento = new JScrollPane(panelProductos);
        desplazamiento.setBorder(BorderFactory.createEmptyBorder());
        desplazamiento.getViewport().setBackground(BLANCO);

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(desplazamiento, BorderLayout.CENTER);

        setContentPane(fondo);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TURQUESA);
        boton.setForeground(AZUL);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void cargarFavoritos() {
        List<Producto> favoritos =
                favoritoDAO.listarPorUsuario(usuario.getIdUsuario());

        panelProductos.removeAll();
        panelProductos.setLayout(new GridLayout(0, 3, 15, 15));

        if (favoritos.isEmpty()) {
            panelProductos.setLayout(new BorderLayout());

            JLabel mensaje = new JLabel(
                    "Todavía no tienes aros favoritos.",
                    SwingConstants.CENTER);
            mensaje.setFont(new Font("SansSerif", Font.PLAIN, 16));
            mensaje.setForeground(AZUL);

            panelProductos.add(mensaje, BorderLayout.CENTER);
        } else {
            for (Producto producto : favoritos) {
                panelProductos.add(crearTarjeta(producto));
            }
        }

        panelProductos.revalidate();
        panelProductos.repaint();
    }

    private JPanel crearTarjeta(Producto producto) {
        JPanel tarjeta = new JPanel(new BorderLayout(8, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 220, 230)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel nombre = new JLabel(
                producto.getNombre(), SwingConstants.CENTER);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        nombre.setForeground(AZUL);

        JLabel etiquetaImagen = new JLabel(
                "Imagen no disponible", SwingConstants.CENTER);
        etiquetaImagen.setPreferredSize(new Dimension(220, 160));
        etiquetaImagen.setForeground(new Color(120, 130, 145));

        String ruta = producto.getImagen();

        if (ruta != null && !ruta.trim().isEmpty()) {
            ImageIcon icono = null;
            String nombreArchivo = new java.io.File(ruta).getName();

            // Cargar desde recursos del .jar (src/main/resources/imagenes/)
            java.net.URL imgUrl = getClass().getResource("/imagenes/" + nombreArchivo);

            if (imgUrl != null) {
                icono = new ImageIcon(imgUrl);
            } else {
                java.io.File archivoFisico = new java.io.File(ruta);
                if (archivoFisico.exists()) {
                    icono = new ImageIcon(archivoFisico.getAbsolutePath());
                }
            }

            if (icono != null && icono.getIconWidth() > 0) {
                Image imagen = icono.getImage().getScaledInstance(220, 160, Image.SCALE_SMOOTH);
                etiquetaImagen.setText("");
                etiquetaImagen.setIcon(new ImageIcon(imagen));
            } else {
                etiquetaImagen.setText("Imagen no disponible");
                etiquetaImagen.setIcon(null);
            }
        }

        String marca = producto.getMarca() == null
                ? "Sin marca" : producto.getMarca();

        String material = producto.getMaterial() == null
                ? "No especificado" : producto.getMaterial();

        String forma = producto.getForma() == null
                ? "No especificada" : producto.getForma();

        String informacion = "<html><div style='text-align:center;'>"
                + "Marca: " + marca + "<br>"
                + "Material: " + material + "<br>"
                + "Forma: " + forma + "<br>"
                + "Precio: L "
                + String.format("%.2f", producto.getPrecio())
                + "<br>Existencias: " + producto.getStock()
                + "</div></html>";

        JLabel detalles = new JLabel(
                informacion, SwingConstants.CENTER);
        detalles.setForeground(AZUL);
        detalles.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel contenido = new JPanel(new BorderLayout(5, 8));
        contenido.setBackground(Color.WHITE);
        contenido.add(etiquetaImagen, BorderLayout.NORTH);
        contenido.add(detalles, BorderLayout.CENTER);

        JButton botonQuitar = crearBoton("Quitar de favoritos");

        botonQuitar.addActionListener(e -> quitarFavorito(producto));

        tarjeta.add(nombre, BorderLayout.NORTH);
        tarjeta.add(contenido, BorderLayout.CENTER);
        tarjeta.add(botonQuitar, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void quitarFavorito(Producto producto) {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres quitar «" + producto.getNombre()
                        + "» de tus favoritos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = favoritoDAO.quitar(
                usuario.getIdUsuario(),
                producto.getIdProducto());

        if (eliminado) {
            JOptionPane.showMessageDialog(
                    this,
                    "El aro se quitó de tus favoritos.",
                    "Mis favoritos",
                    JOptionPane.INFORMATION_MESSAGE);

            cargarFavoritos();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo quitar el aro. Inténtalo nuevamente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
