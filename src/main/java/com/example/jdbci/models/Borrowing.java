package com.example.jdbci.models;

import java.time.LocalDateTime;

public class Borrowing {
    private int id;
    private int userId;
    private int bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private boolean returned;

    public Borrowing(int id, int userId, int bookId, LocalDateTime borrowDate, LocalDateTime returnDate, boolean returned) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public Borrowing(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = LocalDateTime.now();
        this.returned = false;
    }

    public Borrowing() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public static String queryCreateTable() {
        return "CREATE TABLE borrowing (\n" +
                "id SERIAL PRIMARY KEY,\n" +
                "user_id INT REFERENCES \"user\"(id),\n" +
                "book_id INT REFERENCES book(id),\n" +
                "borrow_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "return_date TIMESTAMP,\n" +
                "returned BOOLEAN DEFAULT false\n" +
                ");";
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", returned=" + returned +
                '}';
    }
}
