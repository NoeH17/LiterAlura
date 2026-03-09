package com.example.literalura.repository;

import com.example.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String name);

    @Query("""
        SELECT a FROM Author a
        LEFT JOIN FETCH a.books
        WHERE a.birthYear <= :year
        AND (a.deathYear IS NULL OR a.deathYear >= :year)
        """)
    List<Author> findAuthorsAliveInYear(@Param("year") Integer year);
}