/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.modelo;
import java.time.LocalDate;
/**
 *
 * @author Alexander
 */
public class Medicion {

    private int idMedicion;
    private int idUsuario;
    private LocalDate fechaMedicion;
    private double esfera;
    private double cilindro;
    private int eje;
    private Double adicion;

    public Medicion() {
    }

    public Medicion(int idMedicion, int idUsuario,
                    LocalDate fechaMedicion, double esfera,
                    double cilindro, int eje, Double adicion) {
        this.idMedicion = idMedicion;
        this.idUsuario = idUsuario;
        this.fechaMedicion = fechaMedicion;
        this.esfera = esfera;
        this.cilindro = cilindro;
        this.eje = eje;
        this.adicion = adicion;
    }

    public int getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(int idMedicion) {
        this.idMedicion = idMedicion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFechaMedicion() {
        return fechaMedicion;
    }

    public void setFechaMedicion(LocalDate fechaMedicion) {
        this.fechaMedicion = fechaMedicion;
    }

    public double getEsfera() {
        return esfera;
    }

    public void setEsfera(double esfera) {
        this.esfera = esfera;
    }

    public double getCilindro() {
        return cilindro;
    }

    public void setCilindro(double cilindro) {
        this.cilindro = cilindro;
    }

    public int getEje() {
        return eje;
    }

    public void setEje(int eje) {
        this.eje = eje;
    }

    public Double getAdicion() {
        return adicion;
    }

    public void setAdicion(Double adicion) {
        this.adicion = adicion;
    }
}