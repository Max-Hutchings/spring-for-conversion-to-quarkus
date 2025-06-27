package com.copilot.springstart.demo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/books";
    }

    private static Long createdBookId;

    @Test
    @Order(1)
    void testCreateBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        ResponseEntity<Book> response = restTemplate.postForEntity(getBaseUrl(), book, Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        createdBookId = response.getBody().getId();
    }

    @Test
    @Order(2)
    void testGetAllBooks() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(getBaseUrl(), Book[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(3)
    void testGetBookById() {
        ResponseEntity<Book> response = restTemplate.getForEntity(getBaseUrl() + "/" + createdBookId, Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdBookId);
    }

    @Test
    @Order(4)
    void testUpdateBook() {
        Book update = new Book();
        update.setTitle("Updated Title");
        update.setAuthor("Updated Author");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> entity = new HttpEntity<>(update, headers);
        ResponseEntity<Book> response = restTemplate.exchange(getBaseUrl() + "/" + createdBookId, HttpMethod.PUT, entity, Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @Order(5)
    void testGetBooksByAuthor() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(getBaseUrl() + "/author/Updated Author", Book[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getAuthor()).isEqualTo("Updated Author");
    }

    @Test
    @Order(6)
    void testDeleteBook() {
        restTemplate.delete(getBaseUrl() + "/" + createdBookId);
        ResponseEntity<Book> response = restTemplate.getForEntity(getBaseUrl() + "/" + createdBookId, Book.class);
        assertThat(response.getBody()).isNull();
    }
}
