package com.alurachallenger.literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "Autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeMuerte;
    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    public Autor(){}

    public Autor (DatosAutor datosAutor, Libro libro) {
        this.nombre = datosAutor.nombre();
        this.libro = libro;
        try{
            this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
            this.fechaDeMuerte = datosAutor.fechaDeMuerte();
        } catch (DateTimeParseException e){
            this.fechaDeNacimiento = null;
            this.fechaDeMuerte = null;
        }

    }

}
