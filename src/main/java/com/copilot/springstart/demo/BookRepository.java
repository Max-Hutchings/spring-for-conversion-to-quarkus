package com.copilot.springstart.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {


    // Using SpEL in @Query, which Quarkus does NOT support
    @Query("select b from Book b where b.author = :#{#author}")
    List<Book> findByAuthorWithSpEL(@Param("author") String author);
}

