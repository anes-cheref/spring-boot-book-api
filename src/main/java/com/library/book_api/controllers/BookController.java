package com.library.book_api.controllers;


import com.library.book_api.dto.BookCreateDTO;
import com.library.book_api.dto.BookResponseDTO;
import com.library.book_api.exceptions.BookNotFoundException;
import com.library.book_api.model.Book;
import com.library.book_api.repository.BookRepository;
import com.library.book_api.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }


    @GetMapping
    public List<BookResponseDTO> getBooks(@RequestParam(required = false) String author) {
        return service.findAllBooks(author);
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable String id) {
        return service.getBookById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createBook( @Valid @RequestBody BookCreateDTO book){
        return service.createBook(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        service.deleteBook(id);
    }
}
