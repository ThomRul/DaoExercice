package com.example.jdbci.dao;

import com.example.jdbci.ConnectionFactory;
import com.example.jdbci.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements BookRepository{
    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM book")
        ) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("available")
                ));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Book getOne(int id) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available")
                    );
                }
                System.out.println("Exception : Book doesn't exist");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean insert(Book book) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO book (title, author, isbn, available) VALUES (?, ?, ?, ?)")
        ) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setBoolean(4, book.isAvailable());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean update(int id, Book book) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE book SET title = ?, author = ?, isbn = ?, available = ? WHERE id = ?")
        ) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setBoolean(4, book.isAvailable());
            statement.setInt(5, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) {
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM book WHERE id = ?")
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM book WHERE author ILIKE ?")
        ) {
            statement.setString(1, "%" + author + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available")
                    ));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try (
                Connection connection = ConnectionFactory.connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM book WHERE available = true")
        ) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available")
                    ));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
