package com.example.proyect_a;

public class Nota {
    private String titulo;
    private String texto;
    private String coordenadas;
    private String lugar;

    public Nota(String titulo, String texto, String coordenadas, String lugar) {
        this.titulo = titulo;
        this.texto = texto;
        this.coordenadas = coordenadas;
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

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

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }


}
