package com.getir.service;

import com.getir.entity.Book;
import com.getir.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    final
    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findBookByIsbn(String isbn){
        return bookRepository.findBookByIsbn(isbn);
    }
}
