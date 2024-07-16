package com.alurachallenger.literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private double descargas;
    private String autorLibro;
    @JoinColumn(name = "fechas_de_nacimiento")
    private Integer fechasDeNacimiento;
    @JoinColumn(name = "fechas_de_muerte")
    private Integer fechasDeMuerte;



public  Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idioma = String.valueOf(datosLibro.idiomas());
        this.descargas = OptionalDouble.of(Double.valueOf(datosLibro.numeroDeDescargas())).orElse(0);
        this.autorLibro = datosLibro.autores().get(0).nombre();
        this.fechasDeNacimiento = datosLibro.autores().get(0).fechaDeNacimiento();
        this.fechasDeMuerte = datosLibro.autores().get(0).fechaDeMuerte();
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", descargas=" + descargas + '\'' +
                ", autorLibro='" + autorLibro + '\'' +
                ", Nacimiento='" + fechasDeNacimiento + '\'' +
                ", Deceso='" + fechasDeMuerte + '\'';
    }

    public Integer getFechasDeMuerte() {
        return fechasDeMuerte;
    }

    public Integer getFechasDeNacimiento() {
        return fechasDeNacimiento;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }

    public double getDescargas() {
        return descargas;
    }

    public void setDescargas(double descargas) {
        this.descargas = descargas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

}
