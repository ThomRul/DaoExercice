package com.example.jdbci.dao;

import com.example.jdbci.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> getAll();
    Book getOne(int id);
    boolean insert(Book book);
    boolean update(int id, Book book);
    boolean delete(int id);
    List<Book> findByAuthor(String author);
    List<Book> findAvailableBooks();
}
