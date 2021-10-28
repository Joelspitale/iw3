package ar.edu.iua.iw3.modelo.dto;

import javax.persistence.*;

public class ProductoDTO {

    private String descripcion;

    private double precio;

    public ProductoDTO(String descripcion, double precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }




}
