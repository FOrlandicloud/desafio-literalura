package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private int anioNacimiento;
    private int anioMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libros = new ArrayList<>();

    public Autores( DatosAutores datosAutores) {
        this.nombre = datosAutores.nombre();
        this.anioNacimiento = datosAutores.anioNacimiento();
        this.anioMuerte = datosAutores.anioMuerte();
    }

    public Autores() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public int getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(int anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public List<Libros> getLibros() {
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        StringBuilder librosTitulos = new StringBuilder();
        for (Libros libro : libros){
            librosTitulos.append(libro.getTitulo()).append(", ");
        }

        if (librosTitulos.length() > 0){
            librosTitulos.setLength(librosTitulos.length() -2);
        }

        return "--------------AUTOR-------------------" + "\n" +
                "Autor: " + nombre + "\n" +
                "Fecha de nacimiento: " + anioNacimiento + "\n" +
                "Fecha de fallecimiento: " + anioMuerte + "\n" +
                "Libros: " + libros + "\n";
    }
}
