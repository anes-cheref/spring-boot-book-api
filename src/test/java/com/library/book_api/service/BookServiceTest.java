package com.library.book_api.service;


import com.library.book_api.dto.BookCreateDTO;
import com.library.book_api.dto.BookResponseDTO;
import com.library.book_api.model.Book;
import com.library.book_api.repository.BookRepository;
import com.library.book_api.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    void shouldCreateBook() {
        BookCreateDTO bookCreateDTO = new BookCreateDTO("Harry Potter","JK Rowling",12.0);
        when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String resultID = service.createBook(bookCreateDTO);
        assertNotNull(resultID);
        verify(repository).save(any(Book.class));
    }

    @Test
    void shouldGetAllBooks() {
        Book book = new Book("id1","Harry Potter","JK Rowling",12.0);
        List<Book> list = List.of(book);
        when(repository.findAll()).thenReturn(list);
        List<BookResponseDTO> l = service.findAllBooks(null);
        assertNotNull(l);
        assertFalse(l.isEmpty());
        assertEquals("Harry Potter", l.getFirst().title());
        verify(repository).findAll();
    }
}
