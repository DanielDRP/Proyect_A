package com.example.proyect_a;

/*
Clase que genera el objrto Nota
 */

public class Nota {
    private String titulo;
    private String texto;
    private Double latitud;
    private Double longitud;
    private String lugar;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Nota(String titulo, String texto, Double latitud, Double longitud, String lugar) {
        this.titulo = titulo;
        this.texto = texto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
    }
}
