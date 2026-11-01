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
public class AroGuardado {

    private int idUsuario;
    private int idProducto;
    private LocalDateTime fechaGuardado;

    public AroGuardado() {
    }

    public AroGuardado(int idUsuario, int idProducto,
                       LocalDateTime fechaGuardado) {
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.fechaGuardado = fechaGuardado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public LocalDateTime getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDateTime fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }
}