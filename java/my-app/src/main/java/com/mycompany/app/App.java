package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.mycompany.app.controller.CustomerController;
import com.mycompany.app.model.CustomerDataAccessObject;
import com.mycompany.app.view.CustomerView;

/**
 * Hello world!
 */
public class App {
  public static void CreateDatabase() {
    // NOTE: Connection and Statement are AutoCloseable.
    // Don't forget to close them both in order to avoid leaks.
    try (
        // create a database connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
        Statement statement = connection.createStatement();) {
      statement.setQueryTimeout(30); // set timeout to 30 sec.

      var deleteDbSql = """
          -- Source - https://stackoverflow.com/a
          -- Posted by Bernardo Ramos, modified by community. See post 'Timeline' for change history
          -- Retrieved 2025-11-08, License - CC BY-SA 4.0
          PRAGMA writable_schema = 1;
          DELETE FROM sqlite_master;
          PRAGMA writable_schema = 0;
          VACUUM;
          PRAGMA integrity_check;
          """;
      statement.executeUpdate(deleteDbSql);

      var createTableSql = """
          create table customer (
          id integer,
          firstName text not null,
          lastName text not null,
          email text,
          phoneNumber text
          );""";
      statement.executeUpdate(createTableSql);

      var insertCustomersSql = """
          insert into customer(id, firstName, lastName, email, phoneNumber)
          values
            (1, 'Leo', 'Yui', 'leo.yui@email.com', '123-456-7890'),
            (2, 'Bill', 'George', 'bill.george@email.com', null),
            (3, 'Abcde', 'Hendrix', 'abcde.hendrix@email.com', '256-123-4567'),
            (4, 'Lorum', 'Ipsum', 'lorum.ipsum@email.com', null);
          """;
      statement.executeUpdate(insertCustomersSql);

    } catch (SQLException e) {
      // if the error message is "out of memory",
      // it probably means no database file is found
      e.printStackTrace(System.err);
    }
  }

  public static void main(String[] args) {
    CreateDatabase();
    var dao = new CustomerDataAccessObject();
    var view = new CustomerView();
    var controller = new CustomerController(dao, view);

    var scanner = new Scanner(System.in);
    var execute = true;

    while (execute) {
      System.out.println("Enter 'X' to exit 'L' to list and 'U' to update database.");
      var command = scanner.nextLine().toUpperCase();

      switch (command) {
        case "X":
          execute = false;
          break;
        case "L":
          controller.displayAllCustomers();
          break;
        case "U":
          System.out.println("Request data from Python server.");
          break;
        default:
          System.out.println("Unknown command.");
          break;
      }

    }
    scanner.close();
  }
}
