package com.getir.repository;

import com.getir.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Book findBookByIsbn(String isbn);
}
