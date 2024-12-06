package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autores autor;

    @Column(name = "nombre_autor")
    private String nombreAutor;

    @Column(name = "lenguajes")
    private String lenguajes;
    private double numeroDeDescargas;

    public Libros() {
    }

    public Libros(DatosLibros datosLibros, Autores autor){
        this.titulo = datosLibros.titulo();
        setLenguajes(datosLibros.lenguajes());
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
        this.nombreAutor = autor.getNombre();
        this.autor = autor;
    }

    public Libros(String titulo, String nombre, String string, double v) {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public List<String> getLenguajes() {
        return Arrays.asList(lenguajes.split(","));
    }

    public void setLenguajes(List<String> lenguajes) {
        this.lenguajes = String.join(",", lenguajes);
    }


    public double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString(){
        return  "-----------------LIBRO-----------------" + "\n" +
                "Titulo: " + titulo + "\n" +
                "Autor: " + nombreAutor + "\n" +
                "Idioma: " + lenguajes + "\n" +
                "Numero de descargas: " + numeroDeDescargas + "\n" +
                "----------------------------------------" + "\n";
    }
}
