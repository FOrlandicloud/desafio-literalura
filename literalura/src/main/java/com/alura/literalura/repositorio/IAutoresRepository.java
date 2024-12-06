package com.alura.literalura.repositorio;

import com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAutoresRepository extends JpaRepository<Autores, Long> {
    Autores findByNombreIgnoreCase(String nombre);

    List<Autores> findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(int anioInicial, int anioFinal);
}
