package com.alurachallenger.literalura.principal;

import com.alurachallenger.literalura.repository.LibroRepository;
import com.alurachallenger.literalura.service.Consultas;

public class Principal {

    private LibroRepository repositorio;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }


    public void x (){

        Consultas consultas = new Consultas(repositorio);
        consultas.muestraElMenu();

    }

}
