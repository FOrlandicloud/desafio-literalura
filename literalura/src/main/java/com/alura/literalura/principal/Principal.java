package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repositorio.IAutoresRepository;
import com.alura.literalura.repositorio.ILibrosRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();

    private IAutoresRepository autoresRepository;
    private ILibrosRepository librosRepository;

    public Principal(IAutoresRepository autoresRepository, ILibrosRepository librosRepository) {
        this.autoresRepository = autoresRepository;
        this.librosRepository = librosRepository;
    }


    public void muestraMenu(){
        var opcion = -1;
        System.out.println("Bienvenido! Por favor selecciona una opcion: ");
        while(opcion != 0){
            var menu = """
                    1- Buscar libros por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Top 10 libros mas descargados
                    7- Obtener estadisticas
                    0- Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    agregarLibros();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresPorAnio();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 6:
                    top10Libros();
                    break;
                case 7:
                    estadisticas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;

                default:
                    System.out.println("Opcion no valida, intenta de nuevo");
            }
        }
    }

    private DatosCompletos obtenerDatosLibros() {
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        DatosCompletos datosLibros = conversor.obtenerDatos(json, DatosCompletos.class);
        return datosLibros;
    }

    private Libros crearLibro(DatosLibros datosLibros, Autores autor) {
        if (autor != null) {
            return new Libros(datosLibros, autor);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }

    private void agregarLibros() {
        System.out.println("Escribe el libro que deseas buscar: ");
        DatosCompletos datos = obtenerDatosLibros();
        if (!datos.resultados().isEmpty()) {
            DatosLibros datosLibro = datos.resultados().get(0);
            DatosAutores datosAutores = datosLibro.autor().get(0);
            Libros libro = null;
            Libros libroRepositorio = librosRepository.findByTitulo(datosLibro.titulo());
            if (libroRepositorio != null){
                System.out.println("Este libro ya se encuentra en la base de datos");
                System.out.println(libroRepositorio.toString());
            }else {
                Autores autorRepositorio = autoresRepository.findByNombreIgnoreCase(datosLibro.autor().get(0).nombre());
                if (autorRepositorio != null) {
                    libro = crearLibro(datosLibro, autorRepositorio);
                    librosRepository.save(libro);
                    System.out.println("--- LIBRO AGREGADO ---\n");
                    System.out.println(libro);
                } else {
                    Autores autor = new Autores(datosAutores);
                    autor = autoresRepository.save(autor);
                    libro = crearLibro(datosLibro, autor);
                    librosRepository.save(libro);
                    System.out.println("---- LIBRO AGREGADO ---- \n");
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("El libro consultado no se encuentra en la API de Gutendex, ingresa otro");
        }
    }

    private void librosRegistrados(){
        List<Libros> libros = librosRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("No se registran libros en la base de datos");
            return;
        }
        System.out.println("-----LIBROS REGISTRADOS-----\n");
        libros.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void autoresRegistrados(){
        List<Autores> autores = autoresRepository.findAll();
        if (autores.isEmpty()){
            System.out.println("No se registran autores en la base de datos");
            return;
        }
        System.out.println("-----AUTORES REGISTRADOS-----\n");
        autores.stream()
                .sorted(Comparator.comparing(Autores::getNombre))
                .forEach(System.out::println);
    }

    private void autoresPorAnio(){
        System.out.println("Ingresa el año en el que deseas buscar: ");
        var anio = teclado.nextInt();
        teclado.nextLine();
        if (anio < 0){
            System.out.println("El año ingresado no puede ser negativo, intenta nuevamente");
            return;
        }
        List<Autores> autoresPorAnio = autoresRepository.findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(anio, anio);
        if (autoresPorAnio.isEmpty()){
            System.out.println("No existen autores registrados para ese año");
            return;
        }
        System.out.println("-----AUTORES VIVOS EN EL AÑO " + anio + " SON: -----\n");
        autoresPorAnio.stream()
                .sorted(Comparator.comparing(Autores::getNombre))
                .forEach(System.out::println);
    }

    private void listarPorIdioma(){
        System.out.println("Ingresa las siglas que corresponde al idioma por el que deseas buscar: ");
        String menuIdioma = """
                es - Español
                en - Ingles
                fr - Frances
                pt - Portugues
                """;
        System.out.println(menuIdioma);
        var idioma = teclado.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")){
            System.out.println("Idioma no valido, intenta de nuevo");
            return;
        }
        List<Libros> librosPorIdioma = librosRepository.findByLenguajesContaining(idioma);
        if (librosPorIdioma.isEmpty()){
            System.out.println("No hay libros registrados en ese idioma");
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void top10Libros(){
        System.out.println("¿De donde deseas obtener los libros mas descargados?");
        String menuTop10 = """
                1 - Gutendex
                2 - Base de datos local
                """;
        System.out.println(menuTop10);
        var opcion = teclado.nextInt();
        teclado.nextLine();

        if (opcion == 1){
            System.out.println("----- LOS 10 LIBROS MAS DESCARGADOS EN GUTENDEX SON: -----\n");
            var json = consumoApi.obtenerDatos(URL_BASE);
            DatosCompletos datosCompletos = conversor.obtenerDatos(json, DatosCompletos.class);
            List<Libros> libros = new ArrayList<>();
            for (DatosLibros datosLibros : datosCompletos.resultados()){
                Autores autor = new Autores(datosLibros.autor().get(0));
                Libros libro = new Libros(datosLibros, autor);
                libros.add(libro);
            }
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getNumeroDeDescargas).reversed())
                    .limit(10)
                    .forEach(System.out::println);
        } else if (opcion == 2) {
            System.out.println("----- LOS 10 LIBROS MAS DESCARGADOS EN LA BASE DE DATOS SON: -----\n");
            List<Libros> libros = librosRepository.findAll();
            if (libros.isEmpty()){
                System.out.println("No hay libros registrados");
                return;
            }
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getNumeroDeDescargas).reversed())
                    .limit(10)
                    .forEach(System.out::println);
            
        } else {
            System.out.println("Opcion no valida");
        }
    }

    private void estadisticas(){
        System.out.println("¿De donde deseas obtener las estadisticas?");
        String menu = """
                1 - Gutendex
                2 - Base de datos
                """;
        System.out.println(menu);
        var opcion = teclado.nextInt();
        teclado.nextLine();

        if (opcion == 1){
            System.out.println("----- ESTADISTICAS DE DESCARGAS EN GUTENDEX -----\n");
            var json = consumoApi.obtenerDatos(URL_BASE);
            DatosCompletos datosCompletos = conversor.obtenerDatos(json, DatosCompletos.class);
            DoubleSummaryStatistics estadisticasApi = datosCompletos.resultados().stream()
                    .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
            System.out.println("Libro con mas descargas: " + estadisticasApi.getMax());
            System.out.println("Libro con menos descargas: " + estadisticasApi.getMin());
            System.out.println("Promedio de descargas: " + estadisticasApi.getAverage());
            System.out.println("\n");
        } else if (opcion == 2) {
            System.out.println("----- ESTADISTICAS DESCARGAS DE BASE DE DATOS -----\n");
            List<Libros> libros = librosRepository.findAll();
            if (libros.isEmpty()){
                System.out.println("No hay libros registrados");
                return;
            }
            DoubleSummaryStatistics estadisticasBase = libros.stream()
                    .collect(Collectors.summarizingDouble(Libros::getNumeroDeDescargas));
            System.out.println("Libro con mas descargas: " + estadisticasBase.getMax());
            System.out.println("Libro con menos descargas: " + estadisticasBase.getMin());
            System.out.println("Promedio de descargas: " + estadisticasBase.getAverage());
            System.out.println("\n");
        } else {
            System.out.println("Opcion no valida");
        }
    }

}
