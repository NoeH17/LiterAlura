package com.example.literalura.service;

import com.example.literalura.client.BookApiClient;
import com.example.literalura.dto.AuthorDTO;
import com.example.literalura.dto.BookDTO;
import com.example.literalura.dto.GutendexResponseDTO;
import com.example.literalura.model.Author;
import com.example.literalura.model.Book;
import com.example.literalura.repository.AuthorRepository;
import com.example.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final JsonParserService parser;
    private final BookApiClient client;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       JsonParserService parser,
                       BookApiClient client) {

        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.parser = parser;
        this.client = client;
    }

    public Book searchBookByTitle(String title){

        var existingBook = bookRepository.findByTitleIgnoreCase(title);

        if(existingBook.isPresent()){
            System.out.println("El libro ya está registrado en la base de datos.");
            return existingBook.get();
        }

        String url = "https://gutendex.com/books/?search=" + title.replace(" ", "%20");

        String json = client.fetchBooks(url);

        GutendexResponseDTO response =
                parser.parse(json, GutendexResponseDTO.class);

        if(response.results().isEmpty()){
            return null;
        }

        BookDTO bookDTO = response.results().get(0);

        List<Author> authors = bookDTO.authors()
                .stream()
                .map(this::convertAuthor)
                .toList();

        String language = bookDTO.languages().isEmpty()
                ? "unknown"
                : bookDTO.languages().get(0);

        Book book = new Book(
                bookDTO.id(),
                bookDTO.title(),
                language,
                authors
        );

        return bookRepository.save(book);
    }

    @Transactional
    public void getAllBooks(){

        List<Book> books = bookRepository.findAll();

        if(books.isEmpty()){
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n----- LIBROS REGISTRADOS -----");

        books.forEach(book -> {
            System.out.println("Título: " + book.getTitle());
            System.out.println("Idioma: " + book.getLanguage());

            System.out.println("Autores:");

            book.getAuthors().forEach(author ->
                    System.out.println(" - " + author.getName()));

            System.out.println("-----------------------------");
        });

    }

    private Author convertAuthor(AuthorDTO dto){

        var existingAuthor = authorRepository.findByNameIgnoreCase(dto.name());

        if(existingAuthor.isPresent()){
            return existingAuthor.get();
        }

        Author author = new Author(
                dto.name(),
                dto.birthYear(),
                dto.deathYear()
        );

        return authorRepository.save(author);
    }

    @Transactional
    public List<Book> getBooksByLanguage(String language){

        List<Book> books = bookRepository.findByLanguageIgnoreCase(language);

        books.forEach(book -> book.getAuthors().size());

        return books;
    }
}
