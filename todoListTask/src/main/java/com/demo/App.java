package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12740874";
    private static final String USER = "sql12740874";
    private static final String PASSWORD = "VJwrnPQZMm";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database.");

            while (true) {
                System.out.println("\n1. Add Todo");
                System.out.println("2. View Todos");
                System.out.println("3. Mark Todo as Completed");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  

                switch (choice) {
                    case 1:
                        System.out.print("Enter the task: ");
                        String task = scanner.nextLine();
                        addTodo(connection, task);
                        break;
                    case 2:
                        viewTodos(connection);
                        break;
                    case 3:
                        System.out.print("Enter the ID of the todo to mark as completed: ");
                        int id = scanner.nextInt();
                        markTodoAsCompleted(connection, id);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        connection.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTodo(Connection connection, String task) throws SQLException {
        String sql = "INSERT INTO todos (task) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task);
            stmt.executeUpdate();
            System.out.println("Todo added successfully.");
        }
    }

    private static void viewTodos(Connection connection) throws SQLException {
        String sql = "SELECT * FROM todos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nTodo List:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String task = rs.getString("task");
                boolean status = rs.getBoolean("status");
                System.out.printf("ID: %d, Task: %s, Completed: %s%n", id, task, status ? "Yes" : "No");
            }
        }
    }

    private static void markTodoAsCompleted(Connection connection, int id) throws SQLException {
        String sql = "UPDATE todos SET status = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Todo marked as completed.");
            } else {
                System.out.println("Todo not found.");
            }
        }
    }
}




