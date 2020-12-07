package com.example.proyect_a;

import java.io.Serializable;

public class ListElement implements Serializable {

    public String titulo;
    public String Categoria;

    public ListElement(String titulo, String categoria) {
        this.titulo = titulo;
        Categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }
}
