package com.caseo.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSetService {

//    private static final String URL = "jdbc:sqlite:database/app.db";
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL);
//    }
//}

    private static final String URL = "jdbc:postgresql://localhost:5432/PMLLPA";
    private static final String USER = "Admin";
    private static final String PASSWORD = "Dimon678";

    public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}