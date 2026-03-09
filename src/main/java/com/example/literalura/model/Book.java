package com.example.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book {

    @Id
    private Long id;

    private String title;

    private String language;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    public Book(){}

    public Book(Long id, String title, String language, List<Author> authors) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage(){
        return language;
    }

    public List<Author> getAuthors(){
        return authors;
    }
}