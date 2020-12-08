package com.example.proyect_a;

import java.io.Serializable;

public class ListElement implements Serializable {

    public String titulo;
    public String lugar;

    public ListElement(String titulo, String lugar) {
        this.titulo = titulo;
        this.lugar = lugar;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
