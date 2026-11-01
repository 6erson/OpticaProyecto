/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.modelo;

/**
 *
 * @author Alexander
 */
public class Producto {

    private int idProducto;
    private String nombre;
    private String tipo;
    private String marca;
    private String material;
    private String forma;
    private double precio;
    private int stock;
    private boolean activo;
    private String imagen;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String tipo,
                    String marca, String material, String forma,
                    double precio, int stock, boolean activo,
                    String imagen) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.marca = marca;
        this.material = material;
        this.forma = forma;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
        this.imagen = imagen;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}