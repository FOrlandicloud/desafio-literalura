# Aplicación de Gestión de Libros y Autores

Esta aplicación permite buscar, registrar y consultar libros y autores utilizando la API **Gutendex** y una base de datos local. Está diseñada para gestionar datos de libros y autores, proporcionando estadísticas y opciones de consulta avanzada.

## Funcionalidades

- **Buscar libros por título**: Busca libros en Gutendex y permite registrarlos en la base de datos.
- **Listar libros registrados**: Muestra todos los libros almacenados localmente.
- **Listar autores registrados**: Muestra todos los autores almacenados localmente.
- **Listar autores vivos en un año específico**: Encuentra autores que vivían en un año dado.
- **Listar libros por idioma**: Filtra libros por idioma (español, inglés, francés, portugués).
- **Top 10 libros más descargados**: Muestra los libros más descargados de Gutendex o la base de datos local.
- **Obtener estadísticas**: Calcula estadísticas (máximo, mínimo, promedio) de descargas de libros.

## Requisitos

- **Java** 17 o superior.
- Dependencias externas:
  - Biblioteca para consumir APIs.
  - Biblioteca para procesar JSON.
  - Framework para el acceso a la base de datos (como Hibernate o JPA).

## Instalación

1. Clona este repositorio:
    ```bash
    git clone https://github.com/FOrlandicloud/desafio-literalura.git
    ```
2. Configura tu entorno de base de datos.
3. Asegúrate de tener configuradas las credenciales de conexión a la base de datos.
4. Compila y ejecuta el proyecto:
    ```bash
    javac Principal.java
    java Principal
    ```

## Uso

Al ejecutar la aplicación, se muestra un menú con las siguientes opciones:

```plaintext
1- Buscar libros por título
2- Listar libros registrados
3- Listar autores registrados
4- Listar autores vivos en un determinado año
5- Listar libros por idioma
6- Top 10 libros más descargados
7- Obtener estadísticas
0- Salir
## Arquitectura

La aplicación sigue los principios de diseño **MVC**:

- **Controlador Principal (`Principal`)**: Maneja la interacción del usuario y coordina las operaciones.
- **Servicios (`ConsumoAPI`, `ConvierteDatos`)**: Facilitan la integración con la API de Gutendex y la conversión de datos.
- **Repositorio (`IAutoresRepository`, `ILibrosRepository`)**: Realiza operaciones de persistencia y consultas a la base de datos.
- **Entidades (`Libros`, `Autores`)**: Representan los datos de negocio.

## Créditos

Esta aplicación utiliza la API pública de [Gutendex](https://gutendex.com/) para buscar libros.


