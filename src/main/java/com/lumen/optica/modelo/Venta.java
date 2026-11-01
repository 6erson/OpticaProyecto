/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.modelo;
import java.time.LocalDateTime;
/**
 *
 * @author Alexander
 */
public class Venta {

    private int idVenta;
    private LocalDateTime fechaVenta;
    private int idCliente;
    private int idVendedor;
    private Integer idMedicion;
    private double subtotal;
    private double tasaImpuesto;
    private double impuesto;
    private double total;

    public Venta() {
    }

    public Venta(int idVenta, LocalDateTime fechaVenta,
                 int idCliente, int idVendedor,
                 Integer idMedicion, double subtotal,
                 double tasaImpuesto, double impuesto,
                 double total) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.idMedicion = idMedicion;
        this.subtotal = subtotal;
        this.tasaImpuesto = tasaImpuesto;
        this.impuesto = impuesto;
        this.total = total;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Integer getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(Integer idMedicion) {
        this.idMedicion = idMedicion;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTasaImpuesto() {
        return tasaImpuesto;
    }

    public void setTasaImpuesto(double tasaImpuesto) {
        this.tasaImpuesto = tasaImpuesto;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
