package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mycompany.app.controller.CustomerController;
import com.mycompany.app.model.CustomerDAO;
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
          insert into customer(id, firstName, lastName, email)
          values (1, 'leo', 'yui', 'leo.yui@email.com');
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
    var dao = new CustomerDAO();
    var view = new CustomerView();
    var controller = new CustomerController(dao, view);
    controller.displayAllCustomers();
  }
}
