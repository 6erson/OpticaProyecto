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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.lumen.optica.dao.AroGuardadoDAO;
import com.lumen.optica.impldao.AroGuardadoDAOImpl;
import com.lumen.optica.modelo.AroGuardado;
import com.lumen.optica.modelo.Usuario;
/**
 *
 * @author Alexander
 */
public class VentanaCatalogo extends JFrame {

    private final Color AZUL = new Color(18, 38, 88);
    private final Color TURQUESA = new Color(45, 214, 255);
    private final Color BLANCO = new Color(244, 244, 244);

    private final ProductoDAO productoDAO;
    private final JPanel panelProductos;

    private final JTextField campoBusqueda;
    private final JTextField campoPrecioMinimo;
    private final JTextField campoPrecioMaximo;

    private final JComboBox<String> comboMarca;
    private final JComboBox<String> comboMaterial;

    private List<Producto> productos;
    
    private final Usuario usuario;
    private final AroGuardadoDAO aroGuardadoDAO;

 public VentanaCatalogo(Usuario usuario) {
    this.usuario = usuario;
    this.aroGuardadoDAO = new AroGuardadoDAOImpl();

    productoDAO = new ProductoDAOImpl();
    panelProductos = new JPanel(new GridLayout(0, 3, 15, 15));
    campoBusqueda = new JTextField();
    campoPrecioMinimo = new JTextField();
    campoPrecioMaximo = new JTextField();
    comboMarca = new JComboBox<>();
    comboMaterial = new JComboBox<>();
    productos = new ArrayList<>();

    configurarVentana();
    construirInterfaz();
    cargarProductos();
}

    private void configurarVentana() {
        setTitle("Lumen Óptica | Catálogo de productos");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new BorderLayout(15, 15));
        fondo.setBackground(BLANCO);
        fondo.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("CATÁLOGO DE PRODUCTOS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(AZUL);

        // Primera fila: búsqueda por nombre.
        JPanel filaBusqueda = new JPanel(new BorderLayout(10, 0));
        filaBusqueda.setBackground(BLANCO);

        campoBusqueda.setFont(
                new Font("SansSerif", Font.PLAIN, 14));
        campoBusqueda.setPreferredSize(new Dimension(250, 35));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 220)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JButton botonBuscar = crearBoton("Buscar");
        JButton botonLimpiar = crearBoton("Limpiar");
        JButton botonActualizar = crearBoton("Actualizar");

        JPanel botonesBusqueda = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesBusqueda.setBackground(BLANCO);
        botonesBusqueda.add(botonBuscar);
        botonesBusqueda.add(botonLimpiar);
        botonesBusqueda.add(botonActualizar);

        filaBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        filaBusqueda.add(botonesBusqueda, BorderLayout.EAST);

        // Segunda fila: filtros.
        JPanel filaFiltros = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaFiltros.setBackground(BLANCO);

        configurarCombo(comboMarca);
        configurarCombo(comboMaterial);

        campoPrecioMinimo.setPreferredSize(
                new Dimension(85, 30));
        campoPrecioMaximo.setPreferredSize(
                new Dimension(85, 30));

        filaFiltros.add(crearEtiqueta("Marca:"));
        filaFiltros.add(comboMarca);

        filaFiltros.add(crearEtiqueta("Material:"));
        filaFiltros.add(comboMaterial);

        filaFiltros.add(crearEtiqueta("Precio mínimo L:"));
        filaFiltros.add(campoPrecioMinimo);

        filaFiltros.add(crearEtiqueta("Precio máximo L:"));
        filaFiltros.add(campoPrecioMaximo);

        JButton botonFiltrar = crearBoton("Aplicar filtros");
        filaFiltros.add(botonFiltrar);

        JPanel encabezado = new JPanel(new BorderLayout(5, 10));
        encabezado.setBackground(BLANCO);
        encabezado.add(titulo, BorderLayout.NORTH);

        JPanel controles = new JPanel(new GridLayout(2, 1, 0, 8));
        controles.setBackground(BLANCO);
        controles.add(filaBusqueda);
        controles.add(filaFiltros);

        encabezado.add(controles, BorderLayout.CENTER);

        panelProductos.setBackground(BLANCO);
        panelProductos.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane desplazamiento = new JScrollPane(panelProductos);
        desplazamiento.setBorder(BorderFactory.createEmptyBorder());
        desplazamiento.getViewport().setBackground(BLANCO);

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(desplazamiento, BorderLayout.CENTER);

        setContentPane(fondo);

        botonBuscar.addActionListener(e -> aplicarFiltros());
        campoBusqueda.addActionListener(e -> aplicarFiltros());
        botonFiltrar.addActionListener(e -> aplicarFiltros());

        botonLimpiar.addActionListener(e -> limpiarFiltros());

        botonActualizar.addActionListener(e -> cargarProductos());
    }

    private void configurarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        combo.setPreferredSize(new Dimension(130, 30));
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(AZUL);
        etiqueta.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        return etiqueta;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TURQUESA);
        boton.setForeground(AZUL);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

private void cargarProductos() {
    try {
        productos = new ArrayList<>();

        // Cargar aros activos
        productos.addAll(productoDAO.listarPorTipo("ARO"));

        // Cargar accesorios activos
        productos.addAll(productoDAO.listarPorTipo("ACCESORIO"));

        actualizarOpcionesFiltros();
        aplicarFiltros();

    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(
                this,
                "No se pudo cargar el catálogo. "
                + "Verifica la conexión con la base de datos.",
                "Error",
                JOptionPane.ERROR_MESSAGE);

        System.err.println(
                "Error al cargar catálogo: " + e.getMessage());
    }
}

    private void actualizarOpcionesFiltros() {
        String marcaSeleccionada =
                (String) comboMarca.getSelectedItem();

        String materialSeleccionado =
                (String) comboMaterial.getSelectedItem();

        TreeSet<String> marcas = new TreeSet<>(
                String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> materiales = new TreeSet<>(
                String.CASE_INSENSITIVE_ORDER);

        for (Producto producto : productos) {
            if (producto.getMarca() != null
                    && !producto.getMarca().trim().isEmpty()) {
                marcas.add(producto.getMarca().trim());
            }

            if (producto.getMaterial() != null
                    && !producto.getMaterial().trim().isEmpty()) {
                materiales.add(producto.getMaterial().trim());
            }
        }

        comboMarca.removeAllItems();
        comboMarca.addItem("Todas");

        for (String marca : marcas) {
            comboMarca.addItem(marca);
        }

        comboMaterial.removeAllItems();
        comboMaterial.addItem("Todos");

        for (String material : materiales) {
            comboMaterial.addItem(material);
        }

        seleccionarOpcion(comboMarca, marcaSeleccionada);
        seleccionarOpcion(comboMaterial, materialSeleccionado);
    }

    private void seleccionarOpcion(
            JComboBox<String> combo, String opcion) {

        if (opcion != null) {
            for (int i = 0; i < combo.getItemCount(); i++) {
                if (combo.getItemAt(i).equalsIgnoreCase(opcion)) {
                    combo.setSelectedIndex(i);
                    return;
                }
            }
        }

        combo.setSelectedIndex(0);
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText()
                .trim().toLowerCase();

        String marcaSeleccionada =
                (String) comboMarca.getSelectedItem();

        String materialSeleccionado =
                (String) comboMaterial.getSelectedItem();

        double precioMinimo = 0;
        double precioMaximo = Double.MAX_VALUE;

        try {
            if (!campoPrecioMinimo.getText().trim().isEmpty()) {
                precioMinimo = Double.parseDouble(
                        campoPrecioMinimo.getText().trim());
            }

            if (!campoPrecioMaximo.getText().trim().isEmpty()) {
                precioMaximo = Double.parseDouble(
                        campoPrecioMaximo.getText().trim());
            }

            if (precioMinimo < 0 || precioMaximo < 0) {
                throw new NumberFormatException();
            }

            if (precioMinimo > precioMaximo) {
                JOptionPane.showMessageDialog(
                        this,
                        "El precio mínimo no puede ser mayor "
                        + "que el precio máximo.",
                        "Rango de precios inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Introduce precios válidos, usando números. "
                    + "Puedes dejar un campo vacío.",
                    "Precio inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Producto> resultados = new ArrayList<>();

        for (Producto producto : productos) {
            String nombre = producto.getNombre() == null
                    ? "" : producto.getNombre().toLowerCase();

            String marca = producto.getMarca() == null
                    ? "" : producto.getMarca();

            String material = producto.getMaterial() == null
                    ? "" : producto.getMaterial();

            String tipo = producto.getTipo() == null
        ? "" : producto.getTipo().toLowerCase();
            
            String forma = producto.getForma() == null
        ? "" : producto.getForma();

        boolean coincideTexto =
        nombre.contains(texto)
        || marca.toLowerCase().contains(texto)
        || material.toLowerCase().contains(texto)
        || forma.toLowerCase().contains(texto)
        || tipo.toLowerCase().contains(texto);

            boolean coincideMarca =
                    marcaSeleccionada == null
                    || marcaSeleccionada.equals("Todas")
                    || marca.equalsIgnoreCase(marcaSeleccionada);

            boolean coincideMaterial =
                    materialSeleccionado == null
                    || materialSeleccionado.equals("Todos")
                    || material.equalsIgnoreCase(materialSeleccionado);

            boolean coincidePrecio =
                    producto.getPrecio() >= precioMinimo
                    && producto.getPrecio() <= precioMaximo;

           if (coincideTexto && coincideMarca
            && coincideMaterial && coincidePrecio) {
            resultados.add(producto);
        }
        }

        mostrarProductos(resultados);
    }

    private void limpiarFiltros() {
        campoBusqueda.setText("");
        campoPrecioMinimo.setText("");
        campoPrecioMaximo.setText("");

        if (comboMarca.getItemCount() > 0) {
            comboMarca.setSelectedIndex(0);
        }

        if (comboMaterial.getItemCount() > 0) {
            comboMaterial.setSelectedIndex(0);
        }

        mostrarProductos(productos);
    }

    private void mostrarProductos(List<Producto> lista) {
        panelProductos.removeAll();
        panelProductos.setLayout(new GridLayout(0, 3, 15, 15));

        if (lista.isEmpty()) {
            panelProductos.setLayout(new BorderLayout());

            JLabel mensaje = new JLabel(
                  "No se encontraron productos con esos criterios.",
                    SwingConstants.CENTER);
            mensaje.setForeground(AZUL);
            mensaje.setFont(
                    new Font("SansSerif", Font.PLAIN, 16));

            panelProductos.add(mensaje, BorderLayout.CENTER);
        } else {
            for (Producto producto : lista) {
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
        etiquetaImagen.setForeground(new Color(120, 130, 145));
        etiquetaImagen.setPreferredSize(new Dimension(220, 160));

        String ruta = producto.getImagen(); // Obtiene el valor almacenado en BD

        if (ruta != null && !ruta.trim().isEmpty()) {
            ImageIcon icono = null;

            // Extraer solo el nombre del archivo si viene con una ruta de carpetas
            String nombreArchivo = new java.io.File(ruta).getName();

            // Buscar la imagen en el classpath del .jar (src/main/resources/imagenes/)
            java.net.URL imgUrl = getClass().getResource("/imagenes/" + nombreArchivo);

            if (imgUrl != null) {
                icono = new ImageIcon(imgUrl);
            } else {
                // Fallback: intentar cargar como archivo físico
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

        JButton botonInfo = crearBoton("Ver detalles");

        botonInfo.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Producto: " + producto.getNombre()
                                + "\nMarca: " + marca
                                + "\nMaterial: " + material
                                + "\nForma: " + forma
                                + "\nPrecio: L "
                                + String.format("%.2f",
                                        producto.getPrecio())
                                + "\nExistencias: "
                                + producto.getStock(),
                        "Detalles del aro",
                        JOptionPane.INFORMATION_MESSAGE));
        
        JButton botonFavorito = crearBoton("♡ Guardar en favoritos");

botonFavorito.addActionListener(e -> {
    int idUsuario = usuario.getIdUsuario();
    int idProducto = producto.getIdProducto();

    if (aroGuardadoDAO.existe(idUsuario, idProducto)) {
        JOptionPane.showMessageDialog(
                this,
                "Este aro ya está en tus favoritos.",
                "Mis favoritos",
                JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    AroGuardado favorito = new AroGuardado();
    favorito.setIdUsuario(idUsuario);
    favorito.setIdProducto(idProducto);

    if (aroGuardadoDAO.guardar(favorito)) {
        JOptionPane.showMessageDialog(
                this,
                "Aro guardado en tus favoritos.",
                "Mis favoritos",
                JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(
                this,
                "No se pudo guardar el aro. Inténtalo nuevamente.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
});

       JPanel botones = new JPanel(new GridLayout(2, 1, 5, 5));
       botones.setBackground(Color.WHITE);
       botones.add(botonInfo);
       botones.add(botonFavorito);

       tarjeta.add(nombre, BorderLayout.NORTH);
       tarjeta.add(contenido, BorderLayout.CENTER);
       tarjeta.add(botones, BorderLayout.SOUTH);

        return tarjeta;
    }
}