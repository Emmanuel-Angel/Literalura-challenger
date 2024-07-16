package com.alurachallenger.literalura.service;

import com.alurachallenger.literalura.model.*;
import com.alurachallenger.literalura.repository.LibroRepository;

import java.util.*;
import java.util.stream.Collectors;

public class Consultas {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    private LibroRepository repositorio;
    List<Libro> libros;

    public Consultas(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void  muestraElMenu() {
        int opcion = -1;

        do {
            System.out.println("\nMenú de Consultas:\n" );

            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Lista de Autores vivos en determinado año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("6. Top 10 libros más descargados");
            System.out.println("7. Estadisticas de descargas");

            System.out.println("0. Salir");

            try {
                System.out.print("Ingrese una opción: ");
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibrosWeb();
                        break;
                    case 2:
                        consultaLibrosDB();
                        break;
                    case 3:
                        consultaAutoresDB();
                        break;
                    case 4:
                        busquedaAutores();
                        break;
                    case 5:
                        buscarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarTop10Libros();
                        break;
                    case 7:
                        estadistica();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese solo números.");
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada inválida.");
                teclado.nextLine();
            }
        } while (opcion != 0);
    }

    // 1) busqueda libro por titulo(titulo, autor, idioma, descargas) API
    private DatosLibro getDatosLibro() {

        System.out.println("Ingrese el Título del libro que desea buscar");

        var tituloLibro = teclado.nextLine().toLowerCase();
        var json = consumoAPI
                .obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor
                .obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda
                .resultados()
                .stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloLibro))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro libro = libroBuscado.get();

            // Verifica si el libro ya existe en el repositorio por su título
            Libro libroExistente = repositorio.findByTitulo(libro.titulo());
            if (libroExistente != null) {
                String idiomasLibro = String.join(", ", libro.idiomas());
                idiomasLibro = idiomasLibro.replace("[", "").replace("]", "");

                System.out.println("\n------- Libro Encontrado -------\n" +
                        "Libro: " + libro.titulo() + "\n" +
                        "Autor: " + libro.autores().get(0).nombre() + "\n" +
                        "Idioma: " + idiomasLibro + "\n" +
                        "N° Descargas: " + libro.numeroDeDescargas() + "\n");
                System.out.println("El libro '" + libro.titulo() + "' ya está existe en la base de datos.");
                return null; // Opcional: Puedes lanzar una excepción o manejarlo de otra manera según tu lógica
            }

            // Imprime información del libro encontrado (opcional)
            if (!libro.autores().isEmpty()) {
                String idiomasLibro = String.join(", ", libro.idiomas());
                idiomasLibro = idiomasLibro.replace("[", "").replace("]", "");

                System.out.println("\n------- Libro Encontrado -------\n" +
                        "Libro: " + libro.titulo() + "\n" +
                        "Autor: " + libro.autores().get(0).nombre() + "\n" +
                        "Idioma: " + idiomasLibro + "\n" +
                        "N° Descargas: " + libro.numeroDeDescargas() + "\n");
                } else {
                System.out.println("El libro " + libro.titulo() + " no tiene autores registrados.");
            }

            return libro;
        } else {
            System.out.println("Libro no encontrado");
        }
        return null;
    }

    // guarda en DB
    private void buscarLibrosWeb() {

        DatosLibro datos = getDatosLibro();
        if (datos != null) {
            Libro libro = new Libro(datos);
            repositorio.save(libro);
            System.out.println("datos   "+datos);
            System.out.println("libro   "+libro);
        } else {
        }
    }

    // Consulta libros en DB
    private void consultaLibrosDB() {

        libros = repositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {

                    String idiomasLibro = String.join(", ", libro.getIdioma());
                    idiomasLibro = idiomasLibro.replace("[", "").replace("]", "");

                    System.out.println("\n------- Libro  -------\n" +
                            "Libro: " + libro.getTitulo() + "\n" +
                            "Autor: " + libro.getAutorLibro() + "\n" +
                            "Idioma: " + idiomasLibro + "\n" +
                            "N° Descargas: " + libro.getDescargas() + "\n");
                });
    }

    // Consulta libros en DB
    private void consultaAutoresDB() {

        libros = repositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getAutorLibro))
                .forEach(autor ->
                        System.out.println("\n------- Autores  -------\n" +
                                "Autor: " + autor.getAutorLibro() + "\n" +
                                "Año Nacimiento: " + autor.getFechasDeNacimiento() + "\n" +
                                "Año Fallecimiento: " + autor.getFechasDeMuerte() + "\n"));
    }

    //consulta los libros más descargados en el API
    private void mostrarTop10Libros() {

        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        //Top 10 libros más descargados
        System.out.println("\n------ Top 10 libros más descargados --------\n");
        datos.resultados()
                .stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDeDescargas).reversed())
                .limit(10)
                .forEach((l -> {
                    System.out.println( l.titulo() + " -------1 " + l.autores().get(0).nombre());
                }));
    }

    // buscar autores en un periodo (fecha inicial ) en DB
    private  void busquedaAutores(){
        Scanner teclado = new Scanner(System.in);

        try {
            System.out.println("Ingrese Año: ");
            int inicio = Integer.parseInt(teclado.nextLine());

            List<Libro> autores = repositorio.findAutoresVivosEnAno(inicio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en: "+inicio);
            } else {
                System.out.println("Autores vivos en el año "+ inicio);
                autores.forEach(autor ->
                        System.out.println("\n------- Autores  -------\n" +
                                "Nombre: " + autor.getAutorLibro() +
                                "\nAño de Nacimiento: " + autor.getFechasDeNacimiento() +
                                "\nAño de Fallecimiento: " + autor.getFechasDeMuerte() + "\n")
                );
            }
        } catch (Exception e) {
            System.out.println("Error al buscar autores: " + e.getMessage());
        }
    }

    // consulta en DB
    private  void buscarLibrosPorIdioma(){
        Scanner teclado = new Scanner(System.in);

        try {
            System.out.println("Ingrese el idioma en el que desea buscar los libros (por ejemplo, 'en' para inglés, 'es' para español):");
            String idioma = teclado.nextLine();

            List<Libro> libros = repositorio.findLibrosByIdioma(idioma);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma " + idioma);
            } else {
                System.out.println("Libros en el idioma " + idioma + ":");
                libros.forEach(libro -> {
                    String idiomasLibro = String.join(", ", libro.getIdioma());
                    System.out.println("\n------- Libro  -------\n" +
                            "Libro: " + libro.getTitulo() + "\n" +
                            "Autor: " + libro.getAutorLibro() + "\n" +
                            "Idioma: " + idiomasLibro + "\n" +
                            "N° Descargas: " + libro.getDescargas() + "\n");
                });
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }

    }

    private  void estadistica (){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos
                .resultados()
                .stream()
                .filter(d -> d.numeroDeDescargas() >0 )
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDeDescargas));
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad máxima de descargas: "+ est.getMax());
        System.out.println("Cantidad mínima de descargas: " + est.getMin());
        System.out.println(" Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount());

    }

}
