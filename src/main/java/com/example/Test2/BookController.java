package com.example.Test2;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/all")
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @GetMapping("/id/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PostMapping("/create_several")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Book> createSeveral(@RequestBody @NotNull List<Book> books) {
        List<Book> savedBooks = new ArrayList<>();
        books.stream().filter(b -> findByTitle(b.getTitle()).isEmpty()).forEach(b -> savedBooks.add(bookRepository.save(b)));
        return savedBooks;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @DeleteMapping("/delete_all")
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @PutMapping("/update/{id}")
    public Book updateBook(@RequestBody @NotNull Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
