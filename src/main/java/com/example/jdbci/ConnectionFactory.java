package com.example.jdbci;

import com.example.jdbci.models.Address;
import com.example.jdbci.models.Book;
import com.example.jdbci.models.Borrowing;
import com.example.jdbci.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/training_jdbc";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static void createDropTable(){
        try(
                Connection connection = connection();
                Statement statement = connection.createStatement();
                )
        {
            //drop les tables si jamais elles existent
            statement.execute("DROP TABLE IF EXISTS borrowing CASCADE");
            statement.execute("DROP TABLE IF EXISTS book CASCADE");
            statement.execute("DROP TABLE IF EXISTS address CASCADE");
            statement.execute("DROP TABLE IF EXISTS \"user\" CASCADE");
            System.out.println("Tables address et user vidées avec succès");

            //création des tables
            statement.executeUpdate(User.queryCreateTable());
            statement.executeUpdate(Address.queryCreateTable());
            statement.executeUpdate(Book.queryCreateTable());
            statement.executeUpdate(Borrowing.queryCreateTable());

        }catch(SQLException error){
            System.out.println(error.getMessage());
        }
    }
}
