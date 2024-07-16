package com.alurachallenger.literalura.repository;

import com.alurachallenger.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    @Query("SELECT l FROM Libro l WHERE l.fechasDeNacimiento <= :inicio AND (l.fechasDeMuerte IS NULL OR l.fechasDeMuerte >= :inicio)")
    List<Libro> findAutoresVivosEnAno(@Param("inicio") int inicio);

    @Query("SELECT l FROM Libro l WHERE l.idioma = CONCAT('[', :idioma, ']')")
    List<Libro> findLibrosByIdioma(@Param("idioma") String idioma);
}
