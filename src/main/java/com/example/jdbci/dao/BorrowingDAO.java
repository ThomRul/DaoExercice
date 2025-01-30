package com.example.jdbci.dao;

import com.example.jdbci.ConnectionFactory;
import com.example.jdbci.models.Borrowing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAO implements BorrowingRepository {
    private static final int OVERDUE_DAYS = 14;
    @Override
    public List<Borrowing> getAll() {
        List<Borrowing> borrowings = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM borrowing")
        ) {
            while (rs.next()) {
                borrowings.add(new Borrowing(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getTimestamp("borrow_date").toLocalDateTime(),
                        rs.getTimestamp("return_date") != null ?
                                rs.getTimestamp("return_date").toLocalDateTime() : null,
                        rs.getBoolean("returned")
                ));
            }
            return borrowings;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Borrowing getOne(int id) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM borrowing WHERE id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Borrowing(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getTimestamp("borrow_date").toLocalDateTime(),
                            rs.getTimestamp("return_date") != null ?
                                    rs.getTimestamp("return_date").toLocalDateTime() : null,
                            rs.getBoolean("returned")
                    );
                }
                System.out.println("Exception : Borrowing doesn't exist");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean insert(Borrowing borrowing) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO borrowing (user_id, book_id, borrow_date, return_date, returned) VALUES (?, ?, ?, ?, ?)")
        ) {
            statement.setInt(1, borrowing.getUserId());
            statement.setInt(2, borrowing.getBookId());
            statement.setTimestamp(3, Timestamp.valueOf(borrowing.getBorrowDate()));
            statement.setTimestamp(4, borrowing.getReturnDate() != null ?
                    Timestamp.valueOf(borrowing.getReturnDate()) : null);
            statement.setBoolean(5, borrowing.isReturned());

            // Mettre à jour la disponibilité du livre
            if (statement.executeUpdate() > 0) {
                try (PreparedStatement bookUpdate = connection.prepareStatement(
                        "UPDATE book SET available = false WHERE id = ?")) {
                    bookUpdate.setInt(1, borrowing.getBookId());
                    return bookUpdate.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean update(int id, Borrowing borrowing) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE borrowing SET user_id = ?, book_id = ?, borrow_date = ?, return_date = ?, returned = ? WHERE id = ?")
        ) {
            statement.setInt(1, borrowing.getUserId());
            statement.setInt(2, borrowing.getBookId());
            statement.setTimestamp(3, Timestamp.valueOf(borrowing.getBorrowDate()));
            statement.setTimestamp(4, borrowing.getReturnDate() != null ?
                    Timestamp.valueOf(borrowing.getReturnDate()) : null);
            statement.setBoolean(5, borrowing.isReturned());
            statement.setInt(6, id);

            // Mettre à jour la disponibilité du livre si retourné
            if (statement.executeUpdate() > 0 && borrowing.isReturned()) {
                try (PreparedStatement bookUpdate = connection.prepareStatement(
                        "UPDATE book SET available = true WHERE id = ?")) {
                    bookUpdate.setInt(1, borrowing.getBookId());
                    return bookUpdate.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM borrowing WHERE id = ?")
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Borrowing> findByUser(int userId) {
        List<Borrowing> borrowings = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM borrowing WHERE user_id = ?")
        ) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    borrowings.add(new Borrowing(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getTimestamp("borrow_date").toLocalDateTime(),
                            rs.getTimestamp("return_date") != null ?
                                    rs.getTimestamp("return_date").toLocalDateTime() : null,
                            rs.getBoolean("returned")
                    ));
                }
            }
            return borrowings;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Borrowing> findOverdueBooks() {
        List<Borrowing> borrowings = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM borrowing WHERE returned = false AND " +
                                "borrow_date < CURRENT_TIMESTAMP - INTERVAL '14 days'")
        ) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    borrowings.add(new Borrowing(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getTimestamp("borrow_date").toLocalDateTime(),
                            rs.getTimestamp("return_date") != null ?
                                    rs.getTimestamp("return_date").toLocalDateTime() : null,
                            rs.getBoolean("returned")
                    ));
                }
            }
            return borrowings;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
