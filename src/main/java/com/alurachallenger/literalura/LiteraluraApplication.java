package com.alurachallenger.literalura;

import com.alurachallenger.literalura.principal.Principal;
import com.alurachallenger.literalura.repository.LibroRepository;
import com.alurachallenger.literalura.service.Consultas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.x();
		Consultas consultas = new Consultas(repository);
		consultas.muestraElMenu();

	}
}
