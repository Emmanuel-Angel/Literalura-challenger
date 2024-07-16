package com.alurachallenger.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaDeNacimiento,
        @JsonAlias("death_year") Integer  fechaDeMuerte)
{

    public String nombre() {
        return nombre;
    }

    public Integer fechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public Integer fechaDeMuerte() {
        return fechaDeMuerte;
    }
}
