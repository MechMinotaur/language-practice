package com.mycompany.app;

import java.net.http.HttpClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.mycompany.app.controller.CustomerController;
import com.mycompany.app.controller.MainController;
import com.mycompany.app.model.CustomerDataAccessObject;
import com.mycompany.app.view.CustomerView;
import com.mycompany.app.view.EventView;

public class App {

    public static boolean CreateDatabase() {
        // NOTE: Connection and Statement are AutoCloseable.
        // Don't forget to close them both in order to avoid leaks.
        try (
                // create a database connection
                Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db"); Statement statement = connection.createStatement();) {
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
          social integer,
          firstName text not null,
          lastName text not null,
          email text,
          phoneNumber text
          );""";
            statement.executeUpdate(createTableSql);

            var insertCustomersSql = """
          insert into customer(id, social, firstName, lastName, email, phoneNumber)
          values
            (1, 111111111, 'Leo', 'Yui', 'leo.yui@email.com', '123-456-7890'),
            (2, 222222222, 'Bill', 'George', 'bill.george@email.com', null),
            (3, 333333333, 'Abcde', 'Hendrix', 'abcde.hendrix@email.com', '256-123-4567'),
            (4, 444444444, 'Lorum', 'Ipsum', 'lorum.ipsum@email.com', null);
          """;
            statement.executeUpdate(insertCustomersSql);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        var logger = Logger.getGlobal();

        if (!CreateDatabase()) {
            return;
        }

        var dao = new CustomerDataAccessObject(
                "jdbc:sqlite:sample.db",
                "http://localhost:5000/customers",
                HttpClient.newHttpClient(),
                new Gson());
        var customerView = new CustomerView(logger);
        var eventView = new EventView(logger);
        var customerController = new CustomerController(dao, customerView, eventView);

        var mainController = new MainController(customerController, eventView);

        mainController.StartMainLoop();
    }
}
