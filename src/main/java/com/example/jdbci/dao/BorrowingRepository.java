package com.example.jdbci.dao;

import com.example.jdbci.models.Borrowing;

import java.util.List;

public interface BorrowingRepository {
    List<Borrowing> getAll();
    Borrowing getOne(int id);
    boolean insert(Borrowing borrowing);
    boolean update(int id, Borrowing borrowing);
    boolean delete(int id);
    List<Borrowing> findByUser(int userId);
    List<Borrowing> findOverdueBooks();
}
