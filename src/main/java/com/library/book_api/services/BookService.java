package com.library.book_api.services;

import com.library.book_api.dto.BookCreateDTO;
import com.library.book_api.dto.BookResponseDTO;
import com.library.book_api.exceptions.BookNotFoundException;
import com.library.book_api.model.Book;
import com.library.book_api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookResponseDTO> findAllBooks(String author) {

        if (author == null) {
            return repository.findAll().stream().map(book -> new BookResponseDTO(book.getId(),book.getTitle(), book.getAuthor())).toList();
        }

        return repository.findAll().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .map(book -> new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor()))
                .toList();
    }

    public BookResponseDTO getBookById(String id) {
        return repository.findById(id)
                .map(book -> new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor()))
                .orElseThrow(() -> new BookNotFoundException("Livre pas trouvé avec l'ID : " + id));
    }

    @Transactional
    public String createBook(BookCreateDTO book) throws IllegalArgumentException {
        if(book.title() == null || book.title().isEmpty())
            throw new IllegalArgumentException("le titre est vide");

        Book newBook = new Book(UUID.randomUUID().toString(),book.title(),book.author(),0.00);
        repository.save(newBook);
        return newBook.getId();
    }

    public void deleteBook(String id) {
        repository.deleteById(id);
    }
}
