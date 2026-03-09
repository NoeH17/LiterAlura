package com.example.literalura;

import com.example.literalura.client.BookApiClient;
import com.example.literalura.dto.GutendexResponseDTO;
import com.example.literalura.model.Author;
import com.example.literalura.model.Book;
import com.example.literalura.service.AuthorService;
import com.example.literalura.service.BookService;
import com.example.literalura.service.JsonParserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class literaluraApplication implements CommandLineRunner {

	private final BookService bookService;
	private final AuthorService authorService;

	public literaluraApplication(BookService bookService,
								 AuthorService authorService) {
		this.bookService = bookService;
		this.authorService = authorService;
	}

	public static void main(String[] args) {
		SpringApplication.run(literaluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		showMenu();
	}

	private void showMenu() {

		Scanner scanner = new Scanner(System.in);
		int option = -1;

		while (option != 0) {

			System.out.println("""
					
					Elija la opción a través de su número:
					1- Buscar libro por titulo
					2- Listar libros registrados
					3- Listar autores registrados
					4- Listar autores vivos en un determinado año
					5- Listar libros por idioma
					0 - salir
					""");

			try {
				option = Integer.parseInt(scanner.nextLine());

				switch (option) {

					case 1:
						searchBookByTitle(scanner);
						break;

					case 2:
						listRegisteredBooks();
						break;

					case 3:
						listRegisteredAuthors();
						break;

					case 4:
						listAuthorsAliveInYear(scanner);
						break;

					case 5:
						listBooksByLanguage(scanner);
						break;

					case 0:
						System.out.println("Cerrando aplicación...");
						break;

					default:
						System.out.println("Opción inválida, intente nuevamente.");
				}

			} catch (NumberFormatException e) {
				System.out.println("Por favor ingrese un número válido.");
			}
		}
	}

	private void searchBookByTitle(Scanner scanner) {

		System.out.println("Ingrese el título del libro que desea buscar:");

		String title = scanner.nextLine();

		Book book = bookService.searchBookByTitle(title);

		if(book == null){
			System.out.println("Libro no encontrado.");
			return;
		}

		System.out.println("\n----- LIBRO -----");
		System.out.println("Título: " + book.getTitle());
	}

	private void listRegisteredBooks() {
		bookService.getAllBooks();
	}

	private void listRegisteredAuthors() {

		List<Author> authors = authorService.getAllAuthors();

		if (authors.isEmpty()) {
			System.out.println("No hay autores registrados.");
			return;
		}

		System.out.println("\n----- AUTORES REGISTRADOS -----");

		authors.forEach(author -> {
			System.out.println("Autor: " + author.getName());
			System.out.println("Año nacimiento: " + author.getBirthYear());
			System.out.println("Año fallecimiento: " + author.getDeathYear());
			System.out.println("-----------------------------");
		});
	}

	private void listAuthorsAliveInYear(Scanner scanner){

		System.out.println("Ingrese el año:");
		int year = scanner.nextInt();
		scanner.nextLine();

		List<Author> authors = authorService.getAuthorsAliveInYear(year);

		if(authors.isEmpty()){
			System.out.println("No se encontraron autores vivos en ese año.");
			return;
		}

		System.out.println("\n----- AUTORES VIVOS EN " + year + " -----");

		authors.forEach(author -> {

			System.out.println("Autor: " + author.getName());
			System.out.println("Nacimiento: " + author.getBirthYear());
			System.out.println("Fallecimiento: " + author.getDeathYear());

			System.out.println("Libros:");

			author.getBooks().forEach(book ->
					System.out.println(" - " + book.getTitle())
			);

			System.out.println("-----------------------------");

		});
	}

	private void listBooksByLanguage(Scanner scanner) {

		System.out.println("""
            Ingrese el idioma para buscar libros:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """);

		String language = scanner.nextLine();

		List<Book> books = bookService.getBooksByLanguage(language);

		if(books.isEmpty()){
			System.out.println("No se encontraron libros en ese idioma.");
			return;
		}

		System.out.println("\n----- LIBROS EN IDIOMA: " + language + " -----");

		books.forEach(book -> {
			System.out.println("Título: " + book.getTitle());
			System.out.println("Idioma: " + book.getLanguage());

			System.out.println("Autores:");
			book.getAuthors().forEach(author ->
					System.out.println(" - " + author.getName())
			);

			System.out.println("-----------------------------");
		});
	}
}